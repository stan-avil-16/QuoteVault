package com.example.quotevault.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.quotevault.ui.theme.ElectricViolet

class QuoteWidget : GlanceAppWidget() {
    
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            QuoteWidgetContent()
        }
    }

    @Composable
    private fun QuoteWidgetContent() {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "“The best way to predict the future is to create it.”",
                style = TextStyle(color = ColorProvider(ElectricViolet))
            )
            Spacer(modifier = GlanceModifier.height(8.dp))
            Text(
                text = "— Peter Drucker",
                style = TextStyle(color = ColorProvider(androidx.compose.ui.graphics.Color.Gray))
            )
        }
    }
}
