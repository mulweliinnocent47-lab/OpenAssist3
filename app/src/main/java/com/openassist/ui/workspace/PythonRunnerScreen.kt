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
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor
import com.openassist.workspace.PythonRunRequest
import com.openassist.workspace.PythonSafetyPolicy

@Composable
fun PythonRunnerScreen(onBack: () -> Unit, onConsole: () -> Unit) {
    val request = PythonRunRequest(scriptName = "workspace/code/script.py")
    val policy = PythonSafetyPolicy()
    PremiumPage("Python Runner", "Run, stop, restart, view logs, download output, and save scripts in a restricted sandbox.", OpenAssistDestination.PythonRunner, { if (it == OpenAssistDestination.CodeCanvas) onBack() }) {
        PremiumCard(selected = true) {
            Text("Approval required before running", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text(policy.approvalSummary(request), color = premiumMutedTextColor())
            Spacer(Modifier.height(12.dp))
            Row {
                PremiumButton("Approve Run") { onConsole() }
                Spacer(Modifier.width(12.dp))
                PremiumButton("Cancel") { onBack() }
            }
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            listOf("Run", "Stop", "Restart", "View Logs", "Download Output", "Save Script").forEach {
                PremiumPill(it)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
