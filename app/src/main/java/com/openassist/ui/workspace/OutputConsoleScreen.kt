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
fun OutputConsoleScreen(onBack: () -> Unit) {
    PremiumPage("Output Console", "View Python logs, errors, run status, and downloadable outputs.", OpenAssistDestination.OutputConsole, { if (it == OpenAssistDestination.PythonRunner) onBack() }) {
        PremiumCard(selected = true) {
            Text("Run logs", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("$ python workspace/code/script.py\nOpenAssist Code Canvas\nProcess finished safely.", color = premiumTextColor(), fontFamily = FontFamily.Monospace)
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Console actions", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Stop, restart, clear logs, download output, or save the script back to Workspace.", color = premiumMutedTextColor())
            Spacer(Modifier.height(8.dp))
            PremiumPill("Download Output")
        }
    }
}
