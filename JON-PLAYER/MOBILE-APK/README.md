# JON Player — Mobile source

Independent Android app source. Requires JDK 17 and Gradle 8.9; build with `gradle assembleDebug` from this directory. Media3 ExoPlayer handles supported direct media/HLS/DASH URLs; embed pages are opened with WebView. Quality options limit adaptive stream resolution and do not transcode files. Hardware support determines HEVC/4K/8K playback.