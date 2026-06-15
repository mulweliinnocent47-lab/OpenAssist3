#!/usr/bin/env bash
set -euo pipefail

if git ls-files | rg '\.(zip|png|mp4|jpg|jpeg|gif|webp|apk|aab)$|OpenAssist\(u\)\.zip' >/dev/null; then
  echo "Release check failed: binary assets are tracked. Keep PRs source-only."
  git ls-files | rg '\.(zip|png|mp4|jpg|jpeg|gif|webp|apk|aab)$|OpenAssist\(u\)\.zip'
  exit 1
fi

git diff --check

echo "Release checks passed."
