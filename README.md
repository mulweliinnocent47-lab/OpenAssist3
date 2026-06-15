# OpenAssist

OpenAssist is a native Android AI assistant prototype built with Kotlin, Jetpack Compose, MVVM, Retrofit, OkHttp, Coroutines, StateFlow, EncryptedSharedPreferences, and Material 3.

## Principles

- Bring your own OpenRouter API key.
- No backend, Firebase, Supabase, user accounts, cloud database, or subscription system.
- Store API keys, selected model, and preferences locally on the device.
- Route chat requests directly from Android to OpenRouter.
- Keep a small extensible tool layer for future Android actions and MCP server support.

## Implemented v1 Skeleton

- Onboarding screen for explaining OpenRouter costs and saving the API key locally.
- Settings screen for changing the API key and selected OpenRouter model.
- Chat screen with conversation history, loading state, and error display.
- OpenRouter repository using the Chat Completions endpoint.
- Tool interfaces plus initial device/app tools and confirmation-aware sensitive tool handling.

## OpenAssist v2 AI Modes

OpenAssist supports three AI modes:

- Cloud AI: uses OpenRouter with a user-supplied API key and OpenRouter model selection.
- Local AI: runs downloaded Hugging Face GGUF models offline on device through the planned llama.cpp Android provider.
- Hybrid AI: routes simple prompts to the local provider and complex prompts to OpenRouter based on user-configurable behavior.

Local model files are organized under app storage using this structure:

```text
OpenAssist/
├── models/
│   ├── gemma-2b.gguf
│   ├── smollm2.gguf
│   └── phi4-mini.gguf
├── cache/
├── downloads/
└── chat_history/
```

## OpenAssist v3 Autonomous Assistant & Safety System

OpenAssist v3 turns the prototype into a safety-first Android assistant. It can prepare app actions, summarize the visible screen, and provide a hold-Home voice overlay, but it must never execute sensitive actions silently.

### Safety core

- Sensitive actions always require user confirmation before execution.
- Messages, calls, file operations, payments, contacts, permissions, external sharing, and app automation stay under user control.
- File access is read-only by default. Advanced file access can be enabled in Settings, but delete, move, rename, replace, and bulk edit actions still require confirmation.

### Screen Intelligence Mode

Screen Intelligence is designed to read visible screen content only after a privacy warning and user-approved screen capture/accessibility setup. The feature can summarize the current screen, explain app content, answer questions about visible text, and extract OCR text for article or page summaries.

### Action and multi-step automation

OpenAssist can gather missing details before acting. For example, if the user asks to send a WhatsApp voice message, the assistant first asks what to say, prepares the action summary, and then shows final Approve/Cancel controls before any execution.

### Permission levels

1. Safe: read screen, open apps, and read notifications after approval.
2. Interactive: prepare messages, calls, file sharing, and app interactions with confirmation.
3. Advanced: file modifications, system automation, and accessibility actions with explicit approval.

### Assistant overlay, memory, and personality

OpenAssist is intended to be available from the Android assistant gesture or hold-Home action as a small animated voice-chat layer instead of forcing the full app to open. In-app memory lets users ask OpenAssist to remember preferences and tune the assistant personality while keeping control of stored memories.

## OpenAssist v5 Workspace, Canvas, Code Runner & Content Studio

OpenAssist v5 adds a workspace layer so large outputs do not overflow chat. Long code, generated projects, documents, images, archives, data files, and interactive content become workspace artifacts instead of oversized chat bubbles.

### Workspace system

The Workspace manages generated files, projects, images, documents, code, downloads, and recent creations. The assistant can create documents, code files, images, spreadsheets, presentations, diagrams, ZIP archives, and project folders under organized `workspace/` paths.

### Canvas behavior

Large content should open in a dedicated Canvas automatically. Code opens in Code Canvas, business plans open in Document Editor, websites and apps open in Project Explorer, images open in Image Studio, and run logs open in Output Console.

### Python runner safety

Python execution is designed around a sandbox approval step. By default, scripts cannot delete files, modify the system, use root access, access the network, or run in the background without approval. Before execution, OpenAssist shows the script name, estimated runtime, resource usage, and Approve/Cancel controls.

### Artifact system

Every generated item becomes an artifact: code, image, document, project, data, archive, spreadsheet, presentation, or diagram. Artifacts can be opened, renamed, duplicated, exported, deleted, and shared, with destructive actions still requiring confirmation.

### Content studio vision

OpenAssist should feel like ChatGPT plus Cursor, VS Code, Google Assistant, a file manager, an image generator, and a personal AI operating system in one premium Android workspace.

## OpenAssist v6 MCP Platform, Marketplace & Desktop Bridge

OpenAssist v6 introduces an MCP ecosystem so users can install, manage, and use external tools like apps. Built-in connections include GitHub, Google Drive, Notion, Discord, Telegram, Slack, Home Assistant, Google Calendar, Gmail, Dropbox, OneDrive, Jira, Linear, YouTube, Reddit, Spotify, OpenAI, OpenRouter, and Hugging Face.

### MCP marketplace and connections

The MCP Marketplace supports browsing, search, categories, ratings, verified badges, version information, updates, install, remove, enable, and disable actions. Normal users see simple Connections while advanced users can add custom HTTPS or local-network MCP endpoints, discover tools, import schemas, test health, inspect tools, and set manual permissions.

### MCP discovery and permissions

When a connection is added, OpenAssist discovers tools, reads schemas, imports capabilities, generates descriptions, builds permission requests, and displays available actions. Every MCP action runs under strict permission controls: Allow, Deny, Allow Once, Always Allow, and Revoke Later.

### Desktop Bridge MCP

Desktop Bridge lets Android OpenAssist connect to Windows, Linux, and macOS computers discovered on the local network or through secure remote endpoints such as Cloudflare Tunnel, Tailscale, custom domains, and HTTPS MCP endpoints. Supported actions include launching applications, running scripts, reading and writing files, executing commands, monitoring system status, transferring files, and remote workflows, all behind confirmation gates.

### MCP safety, memory, and workspace

MCP tools can create files, projects, images, documents, reports, and ZIP archives that appear automatically in Workspace. OpenAssist Memory can learn frequently used MCPs, tools, workflows, connected devices, and preferences. Sensitive MCP actions such as deleting files, sending emails, sending messages, running commands, executing scripts, making purchases, or modifying repositories must show tool name, MCP name, requested action, expected result, risk level, and Approve/Cancel controls.

## OpenAssist v7 Agent Engine

OpenAssist v7 adds a planning layer that turns a user goal into an auditable agent run:

```text
Goal
↓
Plan
↓
Tasks
↓
Tool Calls
↓
Confirmation
↓
Execution
↓
Result
```

For example, when a user asks OpenAssist to research local AI models and create a report, the Agent Engine can plan to search Hugging Face, compare model candidates, create a markdown report, save it to Workspace, ask for approval, and export a PDF. Tool calls remain confirmation-aware, especially for MCP, Workspace export, Android control, and file operations.

## OpenAssist v8 Knowledge Base

OpenAssist v8 introduces My Knowledge, where users can upload PDFs, DOCX files, TXT notes, ZIP archives, code projects, and images. The assistant can organize those sources into collections such as School Notes, OpenAssist Project, Work Documents, and Research Papers, then answer questions using the uploaded material.

Knowledge Base is designed to work with Memory and Workspace: uploaded files become searchable knowledge sources, Workspace artifacts can be indexed, and remembered preferences can help the assistant choose the right collection while keeping user-owned files under explicit control.

## OpenAssist v7 Voice Studio

Voice Studio adds a full text-to-speech system with Android TTS as the default provider. Users can optionally bring their own API keys for premium cloud voices while keeping control over cost and privacy.

Supported providers include Android TTS, OpenAI TTS, ElevenLabs, Google Neural TTS, Azure Neural TTS, and local downloadable voices. Voice Studio supports voice preview, a voice marketplace, voice downloads, speed control, pitch control, emotion control, language selection, male and female voices, offline voices, and cloud voices.

The voice architecture is:

```text
Speech Input
↓
AI Model
↓
Voice Engine
↓
Selected Voice Provider
↓
Audio Output
```

Local and offline voices are available for private playback. Cloud voices are disabled by default until the user supplies provider API keys and explicitly allows cloud voice use.

## OpenAssist v9 UX, Navigation & Experience Overhaul

OpenAssist v9 reorganizes the app into five primary tabs so users never feel lost: Home, Workspace, Knowledge, Connections, and Settings. Home becomes the main AI operating-system surface for assistant chat, voice input, quick actions, agent tasks, recent activity, suggested actions, and the active model.

Workspace owns everything OpenAssist creates: projects, documents, code, images, archives, downloads, artifacts, recent files, folders, tags, search, sorting, and favorites. Knowledge owns everything OpenAssist knows: Memory, Knowledge Base, uploaded files, learning insights, saved research, projects, and categories such as Personal, School, Work, Research, and Custom.

Connections owns external systems including OpenRouter, local models, MCP Marketplace, Desktop Bridge, connected services, and installed integrations. Settings owns appearance, voice, memory, permissions, privacy, storage, developer options, AI settings, safety settings, and accessibility.

Universal Search is available from Home and is designed to search chats, workspace files, projects, knowledge, memory, documents, images, models, MCP servers, connections, settings, and commands. A VS Code-style Command Palette supports commands such as «Create Project», «Generate Image», «Run Python», «Summarize Screen», «Connect GitHub», «Switch Model», «Open Workspace», «Browse MCP Marketplace», «New Knowledge Collection», and «Export Project».

The Agent Task Center makes planning visible by showing the goal, plan, progress, tools used, confirmation requests, and results. The optional Context Sidebar summarizes current model, memory status, agent status, workspace status, connected MCPs, desktop devices, and storage usage.

## OpenAssist v10 Core Platform Architecture

OpenAssist v10 turns the app into a unified AI operating system powered by shared engines: AI Engine, Agent Engine, Memory Engine, Workspace Engine, Knowledge Engine, MCP Engine, Android Automation Engine, Voice Engine, Search Engine, and Notification Engine. Future features should integrate through these engines instead of becoming disconnected screens.

A central event bus lets systems publish and subscribe to platform events such as Memory Updated, File Created, Project Created, MCP Installed, Agent Started, Agent Completed, and Model Downloaded. The unified context system gathers conversation context, relevant memory, knowledge results, workspace artifacts, connected MCPs, active models, current screen context, and agent state before the AI Engine answers.

The background task manager standardizes model downloads, knowledge indexing, workspace exports, agent tasks, MCP sync, Desktop Bridge sync, and other long-running jobs with pause, resume, cancel, retry, and progress tracking. The security layer requires permission, safety, confirmation, execution, and audit checks for sensitive actions like messages, calls, file changes, payments, external sharing, command execution, and MCP actions.

Local-only analytics can track most-used features, models, MCPs, commands, workspace activity, and agent activity without automatic uploads. Every major feature should be engine-powered, event-aware, searchable, auditable, and consistent across OpenRouter, local models, and hybrid mode.
