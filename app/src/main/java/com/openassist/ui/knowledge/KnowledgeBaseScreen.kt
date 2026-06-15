package com.openassist.ui.knowledge

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.knowledge.KnowledgeBaseCatalog
import com.openassist.knowledge.KnowledgeIndexEngine
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumButton
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor

@Composable
fun KnowledgeBaseScreen(onBack: () -> Unit, onNavigate: (OpenAssistDestination) -> Unit = {}) {
    val indexEngine = KnowledgeIndexEngine()
    val chunks = indexEngine.indexText("welcome", "OpenAssist can index PDFs, notes, code projects, images, workspace documents, and TXT files for source-aware search.")
    val results = indexEngine.search(chunks, "workspace code search")
    PremiumPage(
        title = "My Knowledge",
        subtitle = "Upload PDFs, DOCX, TXT, ZIPs, code projects, and images so OpenAssist can answer questions about them.",
        selected = OpenAssistDestination.KnowledgeBase,
        onNavigate = { if (it == OpenAssistDestination.Chat) onBack() else onNavigate(it) },
        action = { PremiumPill("Back", onClick = onBack) },
    ) {
        PremiumCard(selected = true) {
            Text("Supported uploads", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text(KnowledgeBaseCatalog.supportedUploads.joinToString(" • ") { it.label }, color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth()) {
            PremiumButton("Upload files") { }
            Spacer(Modifier.width(12.dp))
            PremiumButton("Ask My Knowledge") { }
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard(selected = true) {
            Text("Index engine", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text(indexEngine.summarizeIndex(chunks), color = premiumMutedTextColor())
            Text("Search preview: ${results.firstOrNull()?.chunk?.chunkId ?: "No match"}", color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        KnowledgeBaseCatalog.defaultCollections.forEach { collection ->
            PremiumCard(Modifier.padding(vertical = 4.dp)) {
                Text(collection.name, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text(collection.description, color = premiumMutedTextColor())
                Text("Sources: ${collection.sourceTypes.joinToString { it.label }}", color = premiumMutedTextColor())
            }
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Ingestion pipeline", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            KnowledgeBaseCatalog.ingestionSteps.forEachIndexed { index, step ->
                Text("${index + 1}. ${step.title}: ${step.description}", color = premiumMutedTextColor())
            }
        }
    }
}
