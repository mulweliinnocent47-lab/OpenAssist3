package com.openassist.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

object PremiumColors {
    val Blue = Color(0xFF2F63FF)
    val Indigo = Color(0xFF33457F)
    val Success = Color(0xFF167767)
    val Danger = Color(0xFF6E2730)
}

@Composable
fun premiumBackgroundBrush(): Brush = Brush.verticalGradient(listOf(Color(0xFFF7FAFF), Color(0xFFFFFFFF), Color(0xFFF4FBFF)))

@Composable
fun premiumSurfaceColor(): Color = Color.White

@Composable
fun premiumMutedSurfaceColor(): Color = Color(0xFFF2F5FF)

@Composable
fun premiumTextColor(): Color = Color(0xFF111827)

@Composable
fun premiumMutedTextColor(): Color = Color(0xFF677084)

@Composable
fun premiumBorderColor(): Color = Color(0xFFE1E7F5)

@Composable
fun PremiumPage(
    title: String,
    subtitle: String,
    selected: OpenAssistDestination? = null,
    onNavigate: (OpenAssistDestination) -> Unit = {},
    action: (@Composable () -> Unit)? = null,
    content: @Composable Column.() -> Unit,
) {
    Box(Modifier.fillMaxSize().background(premiumBackgroundBrush())) {
        Column(Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 28.dp)) {
            Text("9:41", color = premiumTextColor(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(28.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(title, color = premiumTextColor(), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
                    Text(subtitle, color = premiumMutedTextColor(), style = MaterialTheme.typography.titleMedium)
                }
                action?.invoke()
            }
            Spacer(Modifier.height(36.dp))
            Column(Modifier.weight(1f).padding(bottom = 16.dp), content = content)
            selected?.let {
                PremiumBottomNav(selected = it, onNavigate = onNavigate)
            }
        }
    }
}

@Composable
fun PremiumCard(modifier: Modifier = Modifier, selected: Boolean = false, content: @Composable Column.() -> Unit) {
    val color = if (selected) Color(0xFFEAF1FF) else premiumSurfaceColor()
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, if (selected) PremiumColors.Blue else premiumBorderColor(), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
    ) { Column(Modifier.padding(16.dp), content = content) }
}

@Composable
fun PremiumButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PremiumColors.Blue, contentColor = Color.White),
    ) { Text(text, fontWeight = FontWeight.Bold) }
}

@Composable
fun PremiumPill(text: String, modifier: Modifier = Modifier, color: Color = PremiumColors.Indigo, onClick: (() -> Unit)? = null) {
    val base = modifier.clip(RoundedCornerShape(50)).background(color).padding(horizontal = 16.dp, vertical = 9.dp)
    Text(text, color = Color.White, modifier = if (onClick == null) base else base.clickable { onClick() }, fontWeight = FontWeight.Bold)
}

@Composable
fun PremiumBottomNav(selected: OpenAssistDestination, onNavigate: (OpenAssistDestination) -> Unit) {
    Row(
        Modifier.fillMaxWidth().height(56.dp).clip(RoundedCornerShape(18.dp)).background(premiumSurfaceColor()).border(1.dp, premiumBorderColor(), RoundedCornerShape(18.dp)).padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        listOf(
            OpenAssistDestination.Chat to "Home",
            OpenAssistDestination.Workspace to "Work",
            OpenAssistDestination.KnowledgeBase to "Knowledge",
            OpenAssistDestination.ConnectionsHub to "Connect",
            OpenAssistDestination.Settings to "Settings",
        ).forEach { (destination, label) ->
            Box(
                Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(14.dp)).background(if (selected == destination) PremiumColors.Indigo else Color.Transparent).clickable { onNavigate(destination) },
                contentAlignment = Alignment.Center,
            ) { Text(label, color = if (selected == destination) Color.White else premiumMutedTextColor(), fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
fun LogoMark(modifier: Modifier = Modifier) {
    Box(modifier.size(38.dp).clip(RoundedCornerShape(12.dp)).background(PremiumColors.Blue), contentAlignment = Alignment.Center) {
        Box(Modifier.size(14.dp).clip(RoundedCornerShape(3.dp)).background(Color.White).border(3.dp, Color(0xFF8EB3FF), RoundedCornerShape(3.dp)))
    }
}
