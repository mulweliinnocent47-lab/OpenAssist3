package com.openassist.ui.support

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumButton
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor

private const val PrimarySupportEmail = "mulweliinnocent47@gmail.com"
private const val SecondarySupportEmail = "adivhahogta@gmail.com"
private const val AppVersion = "1.0.0"

@Composable
fun SupportTrustCenterScreen(onBack: () -> Unit, onNavigate: (OpenAssistDestination) -> Unit) {
    PremiumPage("Support & Trust", "Privacy, permissions, help, feedback, and user controls in one transparent center.", OpenAssistDestination.Settings, { if (it == OpenAssistDestination.Chat) onBack() else onNavigate(it) }) {
        PremiumCard(selected = true) {
            Text("OpenAssist v11", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Support, trust, privacy, onboarding, storage, conversations, and agent task management are now visible from one polished hub.", color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        val entries = listOf(
            Triple(OpenAssistDestination.FeedbackCenter, "Feedback Center", "Share feedback, suggestions, feature requests, and experience ratings."),
            Triple(OpenAssistDestination.ReportBug, "Report Bug", "Send issue details, reproduction steps, screenshots, device information, and app version."),
            Triple(OpenAssistDestination.HelpCenter, "Help Center", "Documentation status and testing notice."),
            Triple(OpenAssistDestination.AboutOpenAssist, "About OpenAssist", "Version, developer, roadmap, and changelog."),
            Triple(OpenAssistDestination.ContactSupport, "Contact Support", "Always-open support emails and response expectations."),
            Triple(OpenAssistDestination.PrivacyCenter, "Privacy Center", "Local-first data storage, provider warnings, delete data, and export roadmap."),
            Triple(OpenAssistDestination.PermissionsCenter, "Permissions Center", "Plain-language explanations for microphone, storage, automation, contacts, camera, and screen capture."),
            Triple(OpenAssistDestination.ConversationLibrary, "Conversation Library", "Search, pin, archive, delete, favorites, categories, and recent chats."),
            Triple(OpenAssistDestination.AgentCenter, "Agent Center", "Running tasks, history, logs, approvals, failures, and statistics."),
            Triple(OpenAssistDestination.WhatsNew, "What's New", "Version 1.0.0 highlights."),
            Triple(OpenAssistDestination.SmartOnboarding, "Smart Onboarding", "Dismissible popup-card tips for setup and discovery."),
            Triple(OpenAssistDestination.TipsRecommendations, "Tips & Recommendations", "Privacy, workspace, memory, knowledge, provider, and agent usage tips."),
            Triple(OpenAssistDestination.StorageAnalyzer, "Storage Analyzer", "Workspace, knowledge, memory, models, images, documents, cache, and cleanup controls."),
        )
        LazyColumn(Modifier.weight(1f)) {
            items(entries) { (destination, title, description) ->
                PremiumCard {
                    Text(title, color = premiumTextColor(), fontWeight = FontWeight.Bold)
                    Text(description, color = premiumMutedTextColor())
                    Spacer(Modifier.height(8.dp))
                    PremiumPill("Open", onClick = { onNavigate(destination) })
                }
            }
        }
    }
}

@Composable
fun FeedbackCenterScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val category = remember { mutableStateOf("Feedback") }
    val rating = remember { mutableStateOf("5") }
    FormPage("Feedback Center", "Feedback, suggestions, feature requests, and experience ratings are sent to $PrimarySupportEmail.", onBack) {
        Field("Title", title.value) { title.value = it }
        Field("Description", description.value) { description.value = it }
        Field("Category", category.value) { category.value = it }
        Field("Rating (1-5)", rating.value) { rating.value = it }
        PremiumButton("Submit") {
            sendSupportEmail(context, PrimarySupportEmail, "OpenAssist feedback: ${title.value}", "Category: ${category.value}\nRating: ${rating.value}/5\n\n${description.value}")
        }
    }
}

@Composable
fun ReportBugScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val issueTitle = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val steps = remember { mutableStateOf("") }
    val expected = remember { mutableStateOf("") }
    val actual = remember { mutableStateOf("") }
    val deviceInfo = "${Build.MANUFACTURER} ${Build.MODEL} • Android ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})"
    FormPage("Report Bug", "Bug reports include App Version $AppVersion and are sent to $PrimarySupportEmail.", onBack) {
        Field("Issue Title", issueTitle.value) { issueTitle.value = it }
        Field("Description", description.value) { description.value = it }
        Field("Steps To Reproduce", steps.value) { steps.value = it }
        Field("Expected Behavior", expected.value) { expected.value = it }
        Field("Actual Behavior", actual.value) { actual.value = it }
        Text("Attach Screenshot: use your email app attachment button after Submit.", color = premiumMutedTextColor())
        Text("Include Device Information: $deviceInfo", color = premiumMutedTextColor())
        Text("Include App Version: $AppVersion", color = premiumMutedTextColor())
        PremiumButton("Submit Bug Report") {
            sendSupportEmail(context, PrimarySupportEmail, "OpenAssist bug: ${issueTitle.value}", "App Version: $AppVersion\nDevice Information: $deviceInfo\nAttach Screenshot: Please attach in email client.\n\nDescription:\n${description.value}\n\nSteps To Reproduce:\n${steps.value}\n\nExpected Behavior:\n${expected.value}\n\nActual Behavior:\n${actual.value}")
        }
    }
}

@Composable
fun HelpCenterScreen(onBack: () -> Unit) = SimpleInfoPage("Help Center", "Coming Soon", onBack, listOf("We are still testing OpenAssist and building documentation. A complete Help Center will be available in a future update."))

@Composable
fun AboutOpenAssistScreen(onBack: () -> Unit) = SimpleInfoPage(
    "About OpenAssist",
    "Application: OpenAssist • Version: $AppVersion • Developer: Mulweli Innocent",
    onBack,
    listOf(
        "OpenAssist is an AI Operating System that combines cloud AI, local AI, workspace management, memory, knowledge, MCP integrations, Android automation, and intelligent agents into one experience.",
        "No more OpenRouter-only experiences. OpenAssist is evolving to support Claude, GPT, Gemini, local AI models, and future providers from a single platform.",
        "Changelog $AppVersion: Initial Release, OpenRouter Integration, Local Models, Memory System, Knowledge Base, Workspace, MCP Platform, Desktop Bridge, Agent Engine, Universal Search, Command Palette, Android Automation.",
    ),
)

@Composable
fun ContactSupportScreen(onBack: () -> Unit) = SimpleInfoPage("Contact Support", "Status: Always Open", onBack, listOf("Support Emails: $PrimarySupportEmail, $SecondarySupportEmail", "We aim to respond as quickly as possible."))

@Composable
fun PrivacyCenterScreen(onBack: () -> Unit) = SimpleInfoPage(
    "Privacy Center",
    "OpenAssist does not store user data on external servers. Everything is stored locally on the user's device.",
    onBack,
    listOf(
        "Some third-party AI providers may process requests through their own infrastructure.",
        "Free cloud AI models may not be suitable for personal, private, financial, medical, or sensitive information. Review provider policies before sharing sensitive content.",
        "Memory: Stored Locally • Workspace Files: Stored Locally • Knowledge Files: Stored Locally • Downloaded Models: Stored Locally.",
        "Delete Data: Memories, Models, Knowledge Collections, Workspace Files, and Conversations.",
        "Export Data: Coming Soon.",
    ),
)

@Composable
fun PermissionsCenterScreen(onBack: () -> Unit) = ListPage("Permissions Center", "Every permission explains why it is needed, how it is used, whether it is optional, and the privacy impact.", onBack, PermissionDisclosure.items)

@Composable
fun ConversationLibraryScreen(onBack: () -> Unit) = SimpleInfoPage("Conversation Library", "Manage conversations.", onBack, listOf("Search Chats", "Pin Chats", "Archive Chats", "Delete Chats", "Recent Conversations", "Favorites", "Conversation Categories"))

@Composable
fun AgentCenterScreen(onBack: () -> Unit) = SimpleInfoPage("Agent Center", "Manage AI tasks.", onBack, listOf("Running Tasks", "Completed Tasks", "Failed Tasks", "Task History", "Task Details", "Execution Logs", "Approvals", "Agent Statistics"))

@Composable
fun WhatsNewScreen(onBack: () -> Unit) = SimpleInfoPage("What's New", "Version $AppVersion — Welcome to OpenAssist.", onBack, listOf("OpenRouter Integration", "Local AI Models", "Workspace", "Knowledge Base", "Memory System", "MCP Platform", "Desktop Bridge", "Universal Search", "Agent Engine"))

@Composable
fun SmartOnboardingScreen(onBack: () -> Unit) = ListPage("Smart Onboarding", "Lightweight popup cards are concise and dismissible.", onBack, listOf("Welcome To OpenAssist", "Choose Your AI Provider", "Configure Memory", "Enable Android Automation", "Download Local Models", "Connect MCP Services", "Discover Workspace", "Learn About Privacy"))

@Composable
fun TipsRecommendationsScreen(onBack: () -> Unit) = ListPage("Tips & Recommendations", "Suggested ways to use OpenAssist safely and effectively.", onBack, listOf("Use local models for better privacy.", "Review permissions before enabling automation.", "Keep important projects inside Workspace.", "Use Memory for long-term projects.", "Use Knowledge Collections for research and school work.", "Review AI provider privacy policies before sharing sensitive information.", "Use Agent Mode for complex multi-step tasks."))

@Composable
fun StorageAnalyzerScreen(onBack: () -> Unit) = ListPage("Storage Analyzer", "Understand and clean local OpenAssist storage.", onBack, listOf("Workspace Storage", "Knowledge Storage", "Memory Storage", "Downloaded Models", "Generated Images", "Documents", "Cache", "Storage Breakdown", "Largest Files", "Delete Cache", "Clean Temporary Files", "Model Storage Overview"))

@Composable
private fun FormPage(title: String, subtitle: String, onBack: () -> Unit, content: @Composable () -> Unit) {
    PremiumPage(title, subtitle, OpenAssistDestination.Settings, { if (it == OpenAssistDestination.Chat) onBack() }) {
        LazyColumn(Modifier.weight(1f)) {
            item { PremiumCard { content() } }
        }
    }
}

@Composable
private fun SimpleInfoPage(title: String, subtitle: String, onBack: () -> Unit, entries: List<String>) {
    PremiumPage(title, subtitle, OpenAssistDestination.Settings, { if (it == OpenAssistDestination.Chat) onBack() }) {
        LazyColumn(Modifier.weight(1f)) {
            items(entries) { entry ->
                PremiumCard { Text(entry, color = premiumTextColor(), fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
private fun ListPage(title: String, subtitle: String, onBack: () -> Unit, entries: List<String>) {
    PremiumPage(title, subtitle, OpenAssistDestination.Settings, { if (it == OpenAssistDestination.Chat) onBack() }) {
        LazyColumn(Modifier.weight(1f)) {
            items(entries) { entry ->
                PremiumCard {
                    Text(entry, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                    Text("Designed to help users understand, trust, and control OpenAssist.", color = premiumMutedTextColor())
                }
            }
        }
    }
}

@Composable
private fun Field(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(value = value, onValueChange = onChange, modifier = Modifier.fillMaxWidth(), label = { Text(label) })
    Spacer(Modifier.height(10.dp))
}

private fun sendSupportEmail(context: android.content.Context, to: String, subject: String, body: String) {
    val uri = Uri.parse("mailto:$to")
    val intent = Intent(Intent.ACTION_SENDTO, uri).apply {
        putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }
    context.startActivity(Intent.createChooser(intent, "Send OpenAssist support email"))
}

private object PermissionDisclosure {
    val items = listOf(
        "Microphone — Used for voice commands and speech recognition. Optional. Audio is only used when voice input is enabled.",
        "Storage — Used to save workspace files, downloaded AI models, generated images, and documents. Optional but required for workspace features.",
        "Notifications — Used for agent progress and task completion alerts. Optional and user controlled.",
        "Accessibility Service — Used for Android automation, screen understanding, and application interaction. Optional with high privacy impact; enable only when needed.",
        "Contacts — Used for messaging and communication features. Optional and only used after approval.",
        "Messaging Features — Used to prepare SMS or app messages. Optional; sensitive sends require confirmation.",
        "Phone Calls — Used to prepare calls. Optional; calling requires approval.",
        "Camera — Used for image analysis. Optional; images remain local unless sent to a selected provider.",
        "Screen Capture — Used for screen understanding. Optional with visible consent and privacy warning.",
    )
}
