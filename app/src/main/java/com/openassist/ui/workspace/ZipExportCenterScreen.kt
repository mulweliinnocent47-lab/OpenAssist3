package com.openassist.ui.workspace

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumButton
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor

@Composable
fun ZipExportCenterScreen(onBack: () -> Unit) {
    PremiumPage("ZIP Export Center", "Package projects, documents, generated files, and assets into downloadable archives.", OpenAssistDestination.ZipExportCenter, { if (it == OpenAssistDestination.ProjectExplorer) onBack() }) {
        PremiumCard(selected = true) {
            Text("Export Summary", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Archive: workspace/archives/project_export.zip", color = premiumMutedTextColor())
            Text("Includes project structure, files, assets, documentation, and roadmap.", color = premiumMutedTextColor())
            Spacer(Modifier.height(12.dp))
            Row {
                PremiumButton("Create ZIP") { }
                Spacer(Modifier.width(12.dp))
                PremiumButton("Cancel") { onBack() }
            }
        }
    }
}
