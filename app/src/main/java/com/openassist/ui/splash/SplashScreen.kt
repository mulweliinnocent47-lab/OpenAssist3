package com.openassist.ui.splash

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.openassist.ui.navigation.LogoMark
import com.openassist.ui.navigation.PremiumButton
import com.openassist.ui.navigation.premiumBackgroundBrush
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumSurfaceColor
import com.openassist.ui.navigation.premiumTextColor
import kotlinx.coroutines.delay

private const val SplashVideoAsset = "splash.mp4"
private const val SplashDurationMs = 5_000L

@Composable
fun SplashScreen(onContinue: () -> Unit) {
    val context = LocalContext.current
    val hasSplashAsset = remember { runCatching { context.assets.openFd(SplashVideoAsset).close() }.isSuccess }

    LaunchedEffect(Unit) {
        delay(SplashDurationMs)
        onContinue()
    }

    Box(Modifier.fillMaxSize().background(premiumBackgroundBrush()).padding(24.dp)) {
        Text("9:41", color = premiumTextColor(), fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.TopStart).padding(top = 20.dp))
        Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
            Column(
                Modifier.fillMaxWidth().height(360.dp).clip(RoundedCornerShape(32.dp)).background(premiumSurfaceColor()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                if (hasSplashAsset) {
                    AndroidView(
                        modifier = Modifier.fillMaxWidth().height(240.dp).clip(RoundedCornerShape(28.dp)),
                        factory = { viewContext ->
                            VideoView(viewContext).apply {
                                setVideoURI(Uri.parse("file:///android_asset/$SplashVideoAsset"))
                                setOnPreparedListener { player ->
                                    player.isLooping = true
                                    player.setVolume(0f, 0f)
                                    start()
                                }
                            }
                        },
                    )
                } else {
                    LogoMark(Modifier.size(96.dp))
                }
                Spacer(Modifier.height(18.dp))
                LogoMark(Modifier.size(56.dp))
                Spacer(Modifier.height(12.dp))
                Text("OpenAssist", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text("Your AI. Your Models.", color = premiumMutedTextColor())
                Text("Launching in 5 seconds", color = premiumMutedTextColor())
            }
        }
        Text("Version 1.0", color = premiumMutedTextColor(), modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 64.dp))
        PremiumButton("Skip", Modifier.align(Alignment.BottomCenter), onContinue)
    }
}
