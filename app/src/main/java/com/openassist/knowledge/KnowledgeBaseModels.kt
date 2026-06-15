package com.openassist.knowledge

import com.openassist.workspace.ArtifactType

enum class KnowledgeSourceType(val label: String) {
    Pdf("PDF"),
    Docx("DOCX"),
    Txt("TXT"),
    Zip("ZIP"),
    CodeProject("Code Project"),
    Image("Image"),
}

data class KnowledgeCollection(
    val id: String,
    val name: String,
    val description: String,
    val sourceTypes: List<KnowledgeSourceType>,
    val linkedArtifactTypes: List<ArtifactType> = emptyList(),
)

data class KnowledgeIngestionStep(
    val title: String,
    val description: String,
)

object KnowledgeBaseCatalog {
    val supportedUploads = KnowledgeSourceType.values().toList()

    val defaultCollections = listOf(
        KnowledgeCollection("school-notes", "School Notes", "Class notes, assignments, lecture PDFs, and study guides.", listOf(KnowledgeSourceType.Pdf, KnowledgeSourceType.Docx, KnowledgeSourceType.Txt)),
        KnowledgeCollection("openassist-project", "OpenAssist Project", "Source code, ZIP exports, design docs, and implementation plans.", listOf(KnowledgeSourceType.CodeProject, KnowledgeSourceType.Zip, KnowledgeSourceType.Txt), listOf(ArtifactType.Project, ArtifactType.Archive)),
        KnowledgeCollection("work-documents", "Work Documents", "Private docs and images for work Q&A with explicit user control.", listOf(KnowledgeSourceType.Pdf, KnowledgeSourceType.Docx, KnowledgeSourceType.Image)),
        KnowledgeCollection("research-papers", "Research Papers", "Papers, citations, summaries, and extracted tables.", listOf(KnowledgeSourceType.Pdf, KnowledgeSourceType.Txt)),
    )

    val ingestionSteps = listOf(
        KnowledgeIngestionStep("Upload", "User chooses PDFs, DOCX, TXT, ZIPs, code projects, or images."),
        KnowledgeIngestionStep("Extract", "OpenAssist extracts text, metadata, filenames, and safe previews."),
        KnowledgeIngestionStep("Index", "Content is chunked and connected to Memory and Workspace artifacts."),
        KnowledgeIngestionStep("Ask", "The assistant answers questions with source-aware context from My Knowledge."),
    )
}

data class KnowledgeChunk(
    val sourceId: String,
    val chunkId: String,
    val text: String,
    val keywords: Set<String>,
)

data class KnowledgeSearchResult(val chunk: KnowledgeChunk, val score: Int)

class KnowledgeIndexEngine {
    fun indexText(sourceId: String, text: String, chunkSize: Int = 420): List<KnowledgeChunk> = text
        .chunked(chunkSize)
        .mapIndexed { index, chunk ->
            KnowledgeChunk(sourceId, "$sourceId-$index", chunk, tokenize(chunk).take(24).toSet())
        }

    fun search(chunks: List<KnowledgeChunk>, query: String): List<KnowledgeSearchResult> {
        val queryTerms = tokenize(query).toSet()
        return chunks.map { chunk -> KnowledgeSearchResult(chunk, chunk.keywords.count { it in queryTerms }) }
            .filter { it.score > 0 }
            .sortedByDescending { it.score }
    }

    fun summarizeIndex(chunks: List<KnowledgeChunk>): String =
        "${chunks.size} chunks indexed from ${chunks.map { it.sourceId }.distinct().size} source(s)."

    private fun tokenize(text: String): List<String> = text.lowercase()
        .split(Regex("[^a-z0-9]+"))
        .filter { it.length > 2 }
}
