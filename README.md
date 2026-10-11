# JON Player — HTML Player

This is the HTML-first JON Player prototype, separate from all JON Stream repositories and APKs.

## Features in this initial version
- Responsive blue / white / red JON Media Service design
- HTML5 video/audio playback for browser-supported direct media URLs and local files
- HLS playback through hls.js, with quality selection when the manifest provides variants
- MPEG-DASH playback through dash.js, with available bitrate/quality options
- Web player / embed URL mode, including compatible NovaStream embed pages
- Play/pause, mute, fullscreen, live status and media title
- Separate native Android source folders remain under `JON-PLAYER/MOBILE-APK/` and `JON-PLAYER/ANDROID-TV-APK/`

## Run
Open `index.html` in a modern browser or host this repository with GitHub Pages. HLS/DASH playback usually needs HTTPS hosting because browser CORS and mixed-content rules apply.

## Important limits
HTML cannot guarantee playback of every video format. Container and codec support depends on the browser and operating system. H.264 is widely supported; H.265/HEVC, MKV/AVI, HDR, 4K and 8K depend on browser/device hardware decoding. Some embeds disallow iframe playback. Quality selection only exposes renditions provided by the stream; it cannot manufacture 4K/8K from a lower-resolution source.

This is source code, not a tested or signed APK. Test real streams and target devices before release.
