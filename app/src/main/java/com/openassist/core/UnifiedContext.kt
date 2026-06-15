package com.openassist.core

data class UnifiedContextPackage(
    val conversationContext: List<String> = emptyList(),
    val relevantMemory: List<String> = emptyList(),
    val knowledgeResults: List<String> = emptyList(),
    val workspaceArtifacts: List<String> = emptyList(),
    val connectedMcps: List<String> = emptyList(),
    val activeModels: List<String> = emptyList(),
    val currentScreenContext: String? = null,
    val agentState: String? = null,
) {
    val sections: Map<String, List<String>> = mapOf(
        "Conversation Context" to conversationContext,
        "Relevant Memory" to relevantMemory,
        "Knowledge Results" to knowledgeResults,
        "Workspace Artifacts" to workspaceArtifacts,
        "Connected MCPs" to connectedMcps,
        "Active Models" to activeModels,
        "Current Screen Context" to listOfNotNull(currentScreenContext),
        "Agent State" to listOfNotNull(agentState),
    )
}

object UnifiedContextBuilder {
    fun sampleForPrompt(prompt: String): UnifiedContextPackage = UnifiedContextPackage(
        conversationContext = listOf(prompt, "Previous assistant answer"),
        relevantMemory = listOf("Prefers local-first privacy", "Uses OpenRouter when cloud mode is enabled"),
        knowledgeResults = listOf("School Notes", "OpenAssist Project"),
        workspaceArtifacts = listOf("workspace/documents/local_ai_models.md", "workspace/projects/openassist/"),
        connectedMcps = listOf("GitHub", "Google Drive", "Desktop Bridge"),
        activeModels = listOf("OpenRouter model", "Local GGUF fallback"),
        currentScreenContext = "Home tab with universal search and command palette",
        agentState = "No active confirmation pending",
    )
}
