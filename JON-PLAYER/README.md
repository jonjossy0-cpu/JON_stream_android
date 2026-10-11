# JON Player

Independent media-player project. JON Stream repositories and APKs are not modified by this project.

- `MOBILE-APK/`: independent Android mobile app source/build.
- `ANDROID-TV-APK/`: independent Android TV app source/build.
- Both use Kotlin, AndroidX Media3 ExoPlayer, hardware decoder support, and WebView for embed pages such as NovaStream.

Codec/resolution playback depends on the Android device's decoder and the stream itself. 4K/8K is not guaranteed on hardware that cannot decode it. Quality choices constrain adaptive streams; they do not create qualities absent from the source.

See `MOBILE-APK/README.md` and `ANDROID-TV-APK/README.md` for build notes.