# JON Player — Android TV source

Separate Android TV source tree and application ID (`et.jonmedia.player.tv`). Build with JDK 17 and Gradle 8.9 using `gradle assembleDebug`. TV remote focus and hardware decoder support vary by device; test on the target TV box before release. HEVC/H.265 and 4K/8K playback is hardware-dependent.