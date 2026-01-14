package com.example.quotevault.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Picture
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.quotevault.ui.theme.ElectricViolet
import com.example.quotevault.utils.ShareUtils

@Composable
fun QuoteShareDialog(
    quote: String,
    author: String,
    onDismiss: () -> Unit
) {
    var selectedStyle by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val density = LocalDensity.current
    
    // We'll use a Picture to record the drawing of our styled card
    val picture = remember { Picture() }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .clip(RoundedCornerShape(32.dp)),
            color = Color(0xFF151515)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Share Quote Card",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, null, tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // THE PREVIEW CARD (This is what we will "capture")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(24.dp))
                ) {
                    // We use a custom Canvas to draw the content into the Picture object
                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width.toInt()
                        val height = size.height.toInt()
                        
                        val canvas = picture.beginRecording(width, height)
                        drawIntoCanvas { composeCanvas ->
                            // Map selected style to design
                            val paint = android.graphics.Paint()
                            
                            when (selectedStyle) {
                                0 -> { // Premium Dark
                                    canvas.drawColor(android.graphics.Color.parseColor("#0F0F0F"))
                                    // Draw Border
                                    paint.color = android.graphics.Color.parseColor("#A855F7")
                                    paint.style = android.graphics.Paint.Style.STROKE
                                    paint.strokeWidth = 10f
                                    canvas.drawRoundRect(20f, 20f, width - 20f, height - 20f, 60f, 60f, paint)
                                }
                                1 -> { // Vibrant Gradient
                                    val gradient = android.graphics.LinearGradient(
                                        0f, 0f, width.toFloat(), height.toFloat(),
                                        android.graphics.Color.parseColor("#A855F7"),
                                        android.graphics.Color.parseColor("#4C1D95"),
                                        android.graphics.Shader.TileMode.CLAMP
                                    )
                                    paint.shader = gradient
                                    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
                                }
                                2 -> { // Minimalist Light
                                    canvas.drawColor(android.graphics.Color.WHITE)
                                }
                            }
                            
                            // Re-init paint for text
                            paint.reset()
                            paint.isAntiAlias = true
                            paint.textAlign = android.graphics.Paint.Align.CENTER
                            paint.color = if (selectedStyle == 2) android.graphics.Color.BLACK else android.graphics.Color.WHITE
                            
                            // Draw Quote
                            paint.textSize = 60f
                            paint.typeface = android.graphics.Typeface.create(android.graphics.Typeface.SERIF, android.graphics.Typeface.BOLD_ITALIC)
                            
                            val textX = width / 2f
                            var textY = height / 2.5f
                            
                            // Simple text wrapping (could be improved)
                            val words = quote.split(" ")
                            var line = ""
                            for (word in words) {
                                if (paint.measureText(line + word) < width * 0.8f) {
                                    line += "$word "
                                } else {
                                    canvas.drawText("“$line”", textX, textY, paint)
                                    line = "$word "
                                    textY += 80f
                                }
                            }
                            canvas.drawText("“$line”", textX, textY, paint)
                            
                            // Draw Author
                            paint.textSize = 40f
                            paint.typeface = android.graphics.Typeface.create(android.graphics.Typeface.SANS_SERIF, android.graphics.Typeface.NORMAL)
                            canvas.drawText("— $author", textX, textY + 100f, paint)
                            
                            // Branded watermark
                            paint.textSize = 30f
                            paint.alpha = 100
                            canvas.drawText("Shared via QuoteVault", textX, height - 60f, paint)
                        }
                        picture.endRecording()
                        
                        // Draw the picture onto the screen canvas for preview
                        drawIntoCanvas { canvas ->
                            canvas.nativeCanvas.drawPicture(picture)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Style Selector
                Text("Select Style", color = Color.Gray, style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StyleOption(0, "Dark", selectedStyle == 0) { selectedStyle = 0 }
                    StyleOption(1, "Vibrant", selectedStyle == 1) { selectedStyle = 1 }
                    StyleOption(2, "Light", selectedStyle == 2) { selectedStyle = 2 }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        // Capture the Picture as a Bitmap
                        val bitmap = Bitmap.createBitmap(
                            picture.width,
                            picture.height,
                            Bitmap.Config.ARGB_8888
                        )
                        val canvas = Canvas(bitmap)
                        canvas.drawPicture(picture)
                        
                        ShareUtils.shareImage(context, bitmap)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet)
                ) {
                    Text("Share Image Card", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StyleOption(index: Int, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(
                    when (index) {
                        0 -> SolidColor(Color(0xFF0F0F0F))
                        1 -> Brush.linearGradient(listOf(ElectricViolet, Color(0xFF4C1D95)))
                        else -> SolidColor(Color.White)
                    }
                )
                .border(2.dp, if (isSelected) ElectricViolet else Color.Transparent, CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            label,
            color = if (isSelected) ElectricViolet else Color.Gray,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
