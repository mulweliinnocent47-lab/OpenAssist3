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
fun ImageStudioScreen(onBack: () -> Unit) {
    PremiumPage("Image Studio", "Generate, edit, save, share, and manage images from the workspace.", OpenAssistDestination.ImageStudio, { if (it == OpenAssistDestination.Workspace) onBack() }) {
        PremiumCard(selected = true) {
            Text("Prompt input", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Describe an image, regenerate variations, upscale, download, share, or save to Workspace.", color = premiumMutedTextColor())
            Spacer(Modifier.height(12.dp))
            PremiumPill("Generate preview")
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Image editing canvas", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            listOf("Crop", "Resize", "Background removal", "Style transfer", "Object removal", "Object addition", "Text overlay").forEach {
                Text("• $it", color = premiumMutedTextColor())
            }
        }
    }
}
