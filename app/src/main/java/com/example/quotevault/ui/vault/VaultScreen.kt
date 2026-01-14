package com.example.quotevault.ui.vault

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotevault.data.model.Collection
import com.example.quotevault.data.model.Quote
import com.example.quotevault.data.repository.CollectionRepository
import com.example.quotevault.data.repository.FavoriteRepository
import com.example.quotevault.ui.components.QuoteCard
import com.example.quotevault.ui.components.QuoteShareDialog
import com.example.quotevault.ui.theme.ElectricViolet
import com.example.quotevault.ui.vault.components.CreateCollectionDialog
import com.example.quotevault.ui.vault.components.CreateQuoteDialog
import com.example.quotevault.utils.ShareUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultScreen() {
    val favoriteRepository = remember { FavoriteRepository() }
    val collectionRepository = remember { CollectionRepository() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    
    var selectedTab by remember { mutableStateOf(0) }
    var favoriteQuotes by remember { mutableStateOf<List<Quote>>(emptyList()) }
    var collections by remember { mutableStateOf<List<Collection>>(emptyList()) }
    var selectedCollection by remember { mutableStateOf<Collection?>(null) }
    var quotesInCollection by remember { mutableStateOf<List<Quote>>(emptyList()) }
    
    var isLoading by remember { mutableStateOf(true) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var showCreateQuoteDialog by remember { mutableStateOf(false) }
    var quoteToEdit by remember { mutableStateOf<Quote?>(null) }

    var showShareDialog by remember { mutableStateOf(false) }
    var quoteToShare by remember { mutableStateOf<Quote?>(null) }

    val infiniteTransition = rememberInfiniteTransition(label = "vault_bg")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.05f, targetValue = 0.15f,
        animationSpec = infiniteRepeatable(tween(4000, easing = EaseInOutSine), RepeatMode.Reverse), label = "pulse"
    )

    fun refreshData() {
        scope.launch {
            // Only show full screen loader if we don't have data yet
            if (favoriteQuotes.isEmpty() && collections.isEmpty() && quotesInCollection.isEmpty()) {
                isLoading = true
            }
            try {
                if (selectedCollection != null) {
                    quotesInCollection = collectionRepository.getQuotesInCollection(selectedCollection!!.id!!)
                } else {
                    favoriteQuotes = favoriteRepository.getFavoriteQuotes()
                    collections = collectionRepository.getCollections()
                }
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(selectedTab, selectedCollection) {
        refreshData()
    }

    if (showShareDialog && quoteToShare != null) {
        QuoteShareDialog(
            quote = quoteToShare!!.text,
            author = quoteToShare!!.author,
            onDismiss = { showShareDialog = false }
        )
    }

    if (showCreateDialog) {
        CreateCollectionDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { name, desc ->
                showCreateDialog = false // Dismiss immediately for snappiness
                scope.launch {
                    collectionRepository.createCollection(name, desc ?: "")
                    snackbarHostState.showSnackbar("Collection created!")
                    refreshData()
                }
            }
        )
    }

    if (showCreateQuoteDialog && selectedCollection != null) {
        CreateQuoteDialog(
            initialText = quoteToEdit?.text ?: "",
            initialAuthor = quoteToEdit?.author ?: "",
            onDismiss = { 
                showCreateQuoteDialog = false
                quoteToEdit = null
            },
            onSave = { text, author ->
                val editing = quoteToEdit != null
                val editingId = quoteToEdit?.id
                showCreateQuoteDialog = false // Dismiss immediately
                quoteToEdit = null
                
                scope.launch {
                    if (editing && editingId != null) {
                        collectionRepository.updateCustomQuote(editingId, text, author)
                        snackbarHostState.showSnackbar("Quote updated")
                    } else {
                        collectionRepository.createCustomQuote(text, author, selectedCollection!!.id!!)
                        snackbarHostState.showSnackbar("Quote saved")
                    }
                    refreshData()
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
                .size(400.dp)
                .offset(x = (-150).dp, y = 100.dp)
                .background(ElectricViolet.copy(alpha = pulseAlpha), CircleShape)
                .blur(100.dp)
        )

        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                CenterAlignedTopAppBar(
                    title = { 
                        Text(
                            if (selectedCollection != null) selectedCollection!!.name else "My Vault", 
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        ) 
                    },
                    navigationIcon = {
                        if (selectedCollection != null) {
                            IconButton(onClick = { selectedCollection = null }) {
                                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = { refreshData() }) {
                            Icon(Icons.Default.Refresh, null, tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            floatingActionButton = {
                if (selectedCollection != null) {
                    ExtendedFloatingActionButton(
                        onClick = { showCreateQuoteDialog = true },
                        containerColor = ElectricViolet,
                        contentColor = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Add, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Add Quote", fontWeight = FontWeight.Bold)
                    }
                } else if (selectedTab == 1) {
                    ExtendedFloatingActionButton(
                        onClick = { showCreateDialog = true },
                        containerColor = ElectricViolet,
                        contentColor = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Add, null)
                        Spacer(Modifier.width(8.dp))
                        Text("New Collection", fontWeight = FontWeight.Bold)
                    }
                }
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                if (selectedCollection == null) {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.Transparent,
                        contentColor = ElectricViolet,
                        indicator = { tabPositions ->
                            if (selectedTab < tabPositions.size) {
                                TabRowDefaults.Indicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                    color = ElectricViolet
                                )
                            }
                        },
                        divider = {}
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Favorites", fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Collections", fontWeight = FontWeight.Bold) }
                        )
                    }
                }

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = ElectricViolet)
                    }
                } else {
                    AnimatedContent(targetState = selectedCollection != null || selectedTab == 0) { isDetailOrFav ->
                        if (isDetailOrFav) {
                            val quotesToShow = if (selectedCollection != null) quotesInCollection else favoriteQuotes
                            if (quotesToShow.isEmpty()) {
                                EmptyState(if (selectedCollection != null) "No quotes in this collection" else "No favorites yet")
                            } else {
                                LazyVerticalStaggeredGrid(
                                    columns = StaggeredGridCells.Fixed(2),
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalItemSpacing = 12.dp
                                ) {
                                    items(quotesToShow) { quote ->
                                        QuoteCard(
                                            quote = quote.text,
                                            author = quote.author,
                                            isFavorite = true,
                                            showActions = true,
                                            onFavoriteClick = if (selectedCollection == null) {
                                                {
                                                    scope.launch {
                                                        favoriteRepository.toggleFavorite(quote.id!!)
                                                        refreshData()
                                                    }
                                                }
                                            } else null,
                                            onShareClick = {
                                                quoteToShare = quote
                                                showShareDialog = true
                                            },
                                            onEditClick = if (selectedCollection != null && quote.user_id != null) {
                                                {
                                                    quoteToEdit = quote
                                                    showCreateQuoteDialog = true
                                                }
                                            } else null,
                                            onDeleteClick = if (selectedCollection != null) {
                                                {
                                                    scope.launch {
                                                        if (quote.user_id != null) {
                                                            collectionRepository.deleteCustomQuote(quote.id!!)
                                                        } else {
                                                            collectionRepository.removeQuoteFromCollection(selectedCollection!!.id!!, quote.id!!)
                                                        }
                                                        snackbarHostState.showSnackbar("Removed")
                                                        refreshData()
                                                    }
                                                }
                                            } else null
                                        )
                                    }
                                }
                            }
                        } else {
                            if (collections.isEmpty()) {
                                EmptyState("Create your first collection")
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(collections) { collection ->
                                        CollectionItem(collection) { selectedCollection = it }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CollectionItem(collection: Collection, onClick: (Collection) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(collection) },
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF151515),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(ElectricViolet.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Folder, null, tint = ElectricViolet)
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(collection.name, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                if (!collection.description.isNullOrBlank()) {
                    Text(collection.description, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = Color.DarkGray)
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.AutoAwesome, null, tint = Color.DarkGray, modifier = Modifier.size(64.dp))
        Spacer(Modifier.height(16.dp))
        Text(message, color = Color.Gray, fontWeight = FontWeight.Bold)
    }
}
