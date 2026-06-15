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

@Composable
fun DocumentEditorScreen(onBack: () -> Unit) {
    PremiumPage("Document Editor", "Write and export Markdown, TXT, PDF, DOCX, PPTX, CSV, XLSX, JSON, HTML, and ZIP-ready content.", OpenAssistDestination.DocumentEditor, { if (it == OpenAssistDestination.Workspace) onBack() }) {
        PremiumCard(selected = true) {
            Text("workspace/documents/business_plan.md", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Large documents open here automatically instead of rendering inside chat.", color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            PremiumPill("Export PDF")
            Spacer(Modifier.height(8.dp))
            PremiumPill("Download Markdown")
        }
    }
}
