package com.example.quotevault.ui.vault.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.quotevault.data.model.Collection
import com.example.quotevault.ui.theme.ElectricViolet

@Composable
fun AddToCollectionDialog(
    collections: List<Collection>,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit,
    onCreateNewCollection: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth().heightIn(max = 450.dp),
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFF151515)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Add to Collection", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, null, tint = Color.Gray)
                    }
                }
                
                Spacer(Modifier.height(16.dp))
                
                // Option to create a new one directly
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCreateNewCollection() },
                    color = Color.Transparent
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Add, null, tint = ElectricViolet)
                        Spacer(Modifier.width(16.dp))
                        Text("Create New Collection", color = ElectricViolet, fontWeight = FontWeight.Bold)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.White.copy(alpha = 0.1f))

                LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                    if (collections.isEmpty()) {
                        item {
                            Text(
                                "No collections found",
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 24.dp).fillMaxWidth(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        items(collections) { collection ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelect(collection.id!!) }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Folder, null, tint = Color.Gray, modifier = Modifier.size(24.dp))
                                Spacer(Modifier.width(16.dp))
                                Text(collection.name, color = Color.White, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }
        }
    }
}
