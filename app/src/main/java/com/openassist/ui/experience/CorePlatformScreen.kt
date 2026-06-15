package com.openassist.ui.experience

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.core.BackgroundTaskCatalog
import com.openassist.core.CorePlatformRegistry
import com.openassist.core.LocalAnalyticsCatalog
import com.openassist.core.PlatformEventCatalog
import com.openassist.core.SecurityLayer
import com.openassist.core.UnifiedContextBuilder
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor

@Composable
fun CorePlatformScreen(onBack: () -> Unit) {
    val context = UnifiedContextBuilder.sampleForPrompt("Research local AI models")
    PremiumPage(
        title = "Core Platform",
        subtitle = "A shared architecture where every OpenAssist feature integrates through engines, events, context, tasks, security, and local analytics.",
        selected = OpenAssistDestination.Settings,
        action = { PremiumPill("Back", onClick = onBack) },
    ) {
        PremiumCard(selected = true) {
            Text("Core engines", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            CorePlatformRegistry.engines.forEach { engine ->
                Text("• ${engine.id.label}: ${engine.responsibility}", color = premiumMutedTextColor())
            }
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Event bus examples", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            PlatformEventCatalog.examples.forEach { event -> Text("${event.type.label}: ${event.summary}", color = premiumMutedTextColor()) }
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Unified context package", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            context.sections.forEach { (section, values) -> if (values.isNotEmpty()) Text("$section: ${values.joinToString()}", color = premiumMutedTextColor()) }
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Background tasks", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            BackgroundTaskCatalog.examples.forEach { task -> Text("${task.title}: ${task.status.label} ${task.progressPercent}%", color = premiumMutedTextColor()) }
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Security layer", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Every sensitive action requires permission, safety, confirmation, execution, and audit checks.", color = premiumMutedTextColor())
            Text(SecurityLayer.sensitiveActions.joinToString { it.label }, color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard(Modifier.padding(bottom = 12.dp)) {
            Text("Local-only analytics", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text(LocalAnalyticsCatalog.privacyRule, color = premiumMutedTextColor())
            LocalAnalyticsCatalog.trackedLocallyOnly.forEach { metric -> Text("${metric.name} • ${metric.scope}", color = premiumMutedTextColor()) }
        }
    }
}
