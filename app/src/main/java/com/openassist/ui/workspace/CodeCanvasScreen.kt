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
import com.openassist.workspace.WorkspaceCatalog

@Composable
fun CodeCanvasScreen(onBack: () -> Unit, onPythonRunner: () -> Unit, onConsole: () -> Unit) {
    PremiumPage("Code Canvas", "Syntax highlighting, line numbers, copy, download, fullscreen, search, replace, auto-scroll, and collapsible sections.", OpenAssistDestination.CodeCanvas, { if (it == OpenAssistDestination.Workspace) onBack() }) {
        PremiumCard(selected = true) {
            Text("workspace/code/script.py", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Supported: ${WorkspaceCatalog.supportedCodeLanguages.joinToString()}", color = premiumMutedTextColor())
            Spacer(Modifier.height(12.dp))
            Text("1  def main():\n2      print('OpenAssist Code Canvas')\n3\n4  main()", color = premiumTextColor(), fontFamily = FontFamily.Monospace)
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            PremiumPill("Run Python", onClick = onPythonRunner)
            Spacer(Modifier.height(8.dp))
            PremiumPill("Open Console", onClick = onConsole)
        }
    }
}
