package com.openassist.ui.workspace

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor
import com.openassist.workspace.WorkspaceCatalog

@Composable
fun ArtifactManagerScreen(onBack: () -> Unit) {
    PremiumPage("Artifact Manager", "Open, rename, duplicate, export, delete, and share every generated artifact.", OpenAssistDestination.ArtifactManager, { if (it == OpenAssistDestination.Workspace) onBack() }) {
        WorkspaceCatalog.starterArtifacts.forEach { artifact ->
            PremiumCard {
                Text("${artifact.type.label}: ${artifact.name}", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text(artifact.path, color = premiumMutedTextColor())
                Spacer(Modifier.height(8.dp))
                PremiumPill("Open")
                Spacer(Modifier.height(6.dp))
                PremiumPill("Export")
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}
