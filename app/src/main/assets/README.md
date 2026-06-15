Splash video assets are intentionally not committed to this repository because the PR system rejects binary files.

For release builds, place the launch video at this exact local path before packaging:

    app/src/main/assets/splash.mp4

The splash screen automatically falls back to the branded OpenAssist card when `splash.mp4` is not present, so the source tree remains buildable without the binary asset.
