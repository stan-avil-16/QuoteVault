package com.example.quotevault.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotevault.ui.theme.ElectricViolet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteCard(
    quote: String,
    author: String,
    isFavorite: Boolean = false,
    showActions: Boolean = true,
    onFavoriteClick: (() -> Unit)? = null,
    onShareClick: (() -> Unit)? = null,
    onAddClick: (() -> Unit)? = null,
    onEditClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF151515)
        ),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "“$quote”",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                lineHeight = 24.sp,
                fontSize = 15.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "— $author",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (showActions) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        if (onEditClick != null) {
                            IconButton(onClick = onEditClick, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.Edit, "Edit", tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                            }
                        }
                        if (onDeleteClick != null) {
                            IconButton(onClick = onDeleteClick, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.Delete, "Delete", tint = Color.Red.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                            }
                        }
                        if (onAddClick != null) {
                            IconButton(onClick = onAddClick, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.BookmarkAdd, "Add", tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
                            }
                        }
                        if (onShareClick != null) {
                            IconButton(onClick = onShareClick, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.Share, "Share", tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
                            }
                        }
                        if (onFavoriteClick != null) {
                            IconButton(onClick = onFavoriteClick, modifier = Modifier.size(28.dp)) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (isFavorite) ElectricViolet else Color.White.copy(alpha = 0.6f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
