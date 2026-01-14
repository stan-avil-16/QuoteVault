package com.example.quotevault.ui.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.quotevault.data.model.Collection
import com.example.quotevault.data.model.Quote
import com.example.quotevault.data.repository.AuthRepository
import com.example.quotevault.data.repository.CollectionRepository
import com.example.quotevault.data.repository.FavoriteRepository
import com.example.quotevault.data.repository.QuoteRepository
import com.example.quotevault.ui.components.ProfileDialog
import com.example.quotevault.ui.components.QuoteCard
import com.example.quotevault.ui.components.QuoteShareDialog
import com.example.quotevault.ui.theme.ElectricViolet
import com.example.quotevault.ui.vault.components.AddToCollectionDialog
import com.example.quotevault.ui.vault.components.CreateCollectionDialog
import com.example.quotevault.utils.ShareUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val quoteRepository = remember { QuoteRepository() }
    val favoriteRepository = remember { FavoriteRepository() }
    val collectionRepository = remember { CollectionRepository() }
    val authRepository = remember { AuthRepository() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var quotes by remember { mutableStateOf<List<Quote>>(emptyList()) }
    var filteredQuotes by remember { mutableStateOf<List<Quote>>(emptyList()) }
    var collections by remember { mutableStateOf<List<Collection>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedCategory by remember { mutableStateOf("For You") }
    val categories = listOf("For You", "Wisdom", "Motivation", "Success", "Love", "Humor")

    // Search State
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // Dialog States
    var showShareDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }
    var quoteToProcess by remember { mutableStateOf<Quote?>(null) }

    val userAvatar = remember(showProfileDialog) { 
        authRepository.currentUser?.userMetadata?.get("avatar_url")?.toString()?.replace("\"", "") ?: ""
    }

    // --- ANIMATION STATE ---
    val infiniteTransition = rememberInfiniteTransition(label = "living_home")
    val driftX by infiniteTransition.animateFloat(
        initialValue = -50f, targetValue = 50f,
        animationSpec = infiniteRepeatable(tween(15000, easing = LinearEasing), RepeatMode.Reverse), label = "x"
    )
    val driftY by infiniteTransition.animateFloat(
        initialValue = -30f, targetValue = 30f,
        animationSpec = infiniteRepeatable(tween(12000, easing = LinearEasing), RepeatMode.Reverse), label = "y"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.05f, targetValue = 0.15f,
        animationSpec = infiniteRepeatable(tween(4000, easing = EaseInOutSine), RepeatMode.Reverse), label = "g"
    )

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                quotes = quoteRepository.getQuotes()
                filteredQuotes = quotes
                collections = collectionRepository.getCollections()
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(selectedCategory, searchQuery, quotes) {
        filteredQuotes = quotes.filter { quote ->
            val categoryMatch = selectedCategory == "For You" || quote.category == selectedCategory
            val searchMatch = searchQuery.isEmpty() || 
                             quote.text.contains(searchQuery, ignoreCase = true) || 
                             quote.author.contains(searchQuery, ignoreCase = true)
            categoryMatch && searchMatch
        }
    }

    if (showShareDialog && quoteToProcess != null) {
        QuoteShareDialog(
            quote = quoteToProcess!!.text,
            author = quoteToProcess!!.author,
            onDismiss = { showShareDialog = false }
        )
    }

    if (showProfileDialog) {
        ProfileDialog(
            onDismiss = { showProfileDialog = false },
            onLogout = {
                // This will trigger the logout flow in navigation
            }
        )
    }

    if (showAddDialog && quoteToProcess != null) {
        AddToCollectionDialog(
            collections = collections,
            onDismiss = { showAddDialog = false },
            onSelect = { collectionId ->
                showAddDialog = false // Snap!
                scope.launch {
                    collectionRepository.addQuoteToCollection(collectionId, quoteToProcess!!.id!!)
                    snackbarHostState.showSnackbar("Added to Collection")
                }
            },
            onCreateNewCollection = {
                showAddDialog = false
                showCreateDialog = true
            }
        )
    }

    if (showCreateDialog) {
        CreateCollectionDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { name, desc ->
                showCreateDialog = false // Snap!
                scope.launch {
                    collectionRepository.createCollection(name, desc ?: "")
                    collections = collectionRepository.getCollections()
                    if (quoteToProcess != null) showAddDialog = true
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050505))
    ) {
        Box(
            modifier = Modifier
                .size(500.dp)
                .offset(x = driftX.dp + 100.dp, y = driftY.dp - 100.dp)
                .background(ElectricViolet.copy(alpha = glowAlpha), CircleShape)
                .blur(120.dp)
        )

        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        if (isSearchActive) {
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                                placeholder = { Text("Search quotes...", color = Color.Gray) },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                            )
                        } else {
                            Text(
                                "QuoteVault",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { 
                            isSearchActive = !isSearchActive
                            if (!isSearchActive) searchQuery = ""
                        }) {
                            Icon(if (isSearchActive) Icons.Default.Close else Icons.Default.Search, null, tint = Color.White)
                        }
                        if (!isSearchActive) {
                            Box(
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(ElectricViolet.copy(alpha = 0.2f))
                                    .clickable { showProfileDialog = true },
                                contentAlignment = Alignment.Center
                            ) {
                                if (userAvatar.isNotEmpty()) {
                                    AsyncImage(
                                        model = userAvatar,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = ElectricViolet)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    // HERO CARD (Only show when not searching)
                    if (!isSearchActive && searchQuery.isEmpty()) {
                        item {
                            if (quotes.isNotEmpty()) {
                                val qotd = quotes.first()
                                var isFav by remember { mutableStateOf(false) }
                                LaunchedEffect(qotd.id) { isFav = favoriteRepository.isFavorite(qotd.id!!) }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(450.dp)
                                        .padding(20.dp)
                                        .clip(RoundedCornerShape(32.dp))
                                ) {
                                    AsyncImage(
                                        model = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&q=80&w=1000",
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))))
                                    )
                                    
                                    Column(
                                        modifier = Modifier.fillMaxSize().padding(24.dp),
                                        verticalArrangement = Arrangement.Bottom
                                    ) {
                                        Surface(
                                            color = Color.White.copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                "QUOTE OF THE DAY",
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 1.sp
                                            )
                                        }
                                        
                                        Spacer(modifier = Modifier.height(16.dp))
                                        
                                        Text(
                                            text = "\"${qotd.text}\"",
                                            style = MaterialTheme.typography.headlineMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            lineHeight = 34.sp
                                        )
                                        
                                        Spacer(modifier = Modifier.height(12.dp))
                                        
                                        Text(
                                            text = "— ${qotd.author}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.White.copy(alpha = 0.7f)
                                        )
                                        
                                        Spacer(modifier = Modifier.height(24.dp))
                                        
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                            IconButton(onClick = { 
                                                quoteToProcess = qotd
                                                showShareDialog = true
                                            }) {
                                                Icon(Icons.Default.Share, null, tint = Color.White)
                                            }
                                            IconButton(onClick = { 
                                                scope.launch { 
                                                    val wasFav = isFav
                                                    favoriteRepository.toggleFavorite(qotd.id!!)
                                                    isFav = !wasFav
                                                    if (!wasFav) {
                                                        snackbarHostState.showSnackbar("Added to Vault")
                                                    }
                                                }
                                            }) {
                                                Icon(if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null, tint = if (isFav) ElectricViolet else Color.White)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Categories Row (Only show when not searching)
                    if (!isSearchActive && searchQuery.isEmpty()) {
                        item {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(categories) { category ->
                                    val isSelected = selectedCategory == category
                                    Surface(
                                        modifier = Modifier.clickable { selectedCategory = category },
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) ElectricViolet else Color(0xFF151515),
                                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                                    ) {
                                        Text(
                                            text = category,
                                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
                                            color = if (isSelected) Color.White else Color.Gray,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                if (searchQuery.isNotEmpty()) "Search Results" else "Curated for You", 
                                style = MaterialTheme.typography.titleLarge, 
                                fontWeight = FontWeight.Bold, 
                                color = Color.White
                            )
                            if (searchQuery.isEmpty()) {
                                Text("See All", style = MaterialTheme.typography.labelLarge, color = ElectricViolet, modifier = Modifier.clickable { })
                            }
                        }
                    }

                    // FEED
                    items(if (isSearchActive || searchQuery.isNotEmpty()) filteredQuotes else filteredQuotes.drop(1)) { quote ->
                        var isFav by remember { mutableStateOf(false) }
                        LaunchedEffect(quote.id) { isFav = favoriteRepository.isFavorite(quote.id!!) }

                        QuoteCard(
                            quote = quote.text,
                            author = quote.author,
                            isFavorite = isFav,
                            onFavoriteClick = {
                                scope.launch {
                                    val wasFav = isFav
                                    favoriteRepository.toggleFavorite(quote.id!!)
                                    isFav = !wasFav
                                    if (!wasFav) {
                                        snackbarHostState.showSnackbar("Added to Vault")
                                    }
                                }
                            },
                            onShareClick = { 
                                quoteToProcess = quote
                                showShareDialog = true
                            },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}
