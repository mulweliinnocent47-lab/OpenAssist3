package com.openassist.ui.workspace

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor

@Composable
fun ProjectExplorerScreen(onBack: () -> Unit, onFileViewer: () -> Unit, onZipExport: () -> Unit) {
    PremiumPage("Project Explorer", "Organize generated apps, websites, assets, documentation, and roadmaps in project folders.", OpenAssistDestination.ProjectExplorer, { if (it == OpenAssistDestination.Workspace) onBack() }) {
        PremiumCard(selected = true) {
            Text("workspace/projects/app/", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("app/\n├── README.md\n├── src/\n├── assets/\n└── roadmap.md", color = premiumTextColor(), fontFamily = FontFamily.Monospace)
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Project actions", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Open files, preview generated outputs, and export the project as a ZIP archive.", color = premiumMutedTextColor())
            Spacer(Modifier.height(8.dp))
            PremiumPill("Open File", onClick = onFileViewer)
            Spacer(Modifier.height(8.dp))
            PremiumPill("Export ZIP", onClick = onZipExport)
        }
    }
}
