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
fun FileViewerScreen(onBack: () -> Unit) {
    PremiumPage("File Viewer", "Preview generated files without breaking chat layout.", OpenAssistDestination.FileViewer, { if (it == OpenAssistDestination.ProjectExplorer) onBack() }) {
        PremiumCard(selected = true) {
            Text("workspace/projects/app/README.md", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("# Generated Project\n\nThis file is shown in a workspace viewer, not a chat bubble.", color = premiumTextColor(), fontFamily = FontFamily.Monospace)
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("File actions", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Open, rename, duplicate, export, delete, and share actions are artifact actions. Destructive actions still require confirmation.", color = premiumMutedTextColor())
            Spacer(Modifier.height(8.dp))
            PremiumPill("Download")
        }
    }
}
