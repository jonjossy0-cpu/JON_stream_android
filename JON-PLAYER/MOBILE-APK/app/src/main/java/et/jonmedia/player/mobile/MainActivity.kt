package et.jonmedia.player.mobile

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@UnstableApi
class MainActivity : ComponentActivity() {
 private var player: ExoPlayer? = null
 private lateinit var playerView: PlayerView
 private lateinit var webView: WebView
 private lateinit var titleText: TextView
 private lateinit var statusText: TextView
 private lateinit var urlInput: EditText
 private lateinit var quality: Spinner
 private lateinit var playButton: Button
 private var full = false

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  window.statusBarColor = Color.rgb(10,32,67); window.navigationBarColor = Color.BLACK
  buildUi()
  player = ExoPlayer.Builder(this).build().also { p ->
   playerView.player = p
   p.addListener(object : Player.Listener {
    override fun onIsPlayingChanged(isPlaying: Boolean) { playButton.text = if (isPlaying) "Pause" else "Play" }
    override fun onPlayerError(error: PlaybackException) { statusText.text = "Playback error: " + error.errorCodeName }
   })
  }
 }
 private fun buildUi() {
  val root = LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setBackgroundColor(Color.rgb(8,16,31)); setPadding(12,10,12,8) }
  root.addView(TextView(this).apply { text="JON PLAYER"; textSize=22f; setTextColor(Color.WHITE); gravity=Gravity.CENTER_VERTICAL; setPadding(8,4,8,10) })
  titleText=TextView(this).apply { text="Ready to play"; textSize=16f; setTextColor(Color.WHITE); setPadding(8,6,8,4) }; root.addView(titleText)
  statusText=TextView(this).apply { text="● READY"; textSize=12f; setTextColor(Color.rgb(255,70,70)); setPadding(8,2,8,8) }; root.addView(statusText)
  urlInput=EditText(this).apply { hint="Paste video, HLS, DASH or embed URL"; setSingleLine(true); setTextColor(Color.WHITE); setHintTextColor(Color.LTGRAY); setPadding(10,4,10,4) }; root.addView(urlInput,LinearLayout.LayoutParams(-1,52))
  root.addView(Button(this).apply { text="Open URL"; setOnClickListener { openUrl(urlInput.text.toString().trim()) } })
  playerView=PlayerView(this).apply { useController=true; setBackgroundColor(Color.BLACK); controllerAutoShow=true }; root.addView(playerView,LinearLayout.LayoutParams(-1,0,1f))
  webView=WebView(this).apply { visibility=View.GONE; settings.javaScriptEnabled=true; settings.domStorageEnabled=true; webChromeClient=WebChromeClient(); webViewClient=WebViewClient() }; root.addView(webView,LinearLayout.LayoutParams(-1,0,1f))
  val row=LinearLayout(this).apply { gravity=Gravity.CENTER_VERTICAL; orientation=LinearLayout.HORIZONTAL }
  playButton=Button(this).apply { text="Play"; setOnClickListener { player?.let { if(it.isPlaying) it.pause() else it.play() } } }; row.addView(playButton,LinearLayout.LayoutParams(0,-2,1f))
  row.addView(Button(this).apply { text="Mute"; setOnClickListener { player?.let { it.volume=if(it.volume>0f) 0f else 1f; text=if(it.volume>0f) "Mute" else "Unmute" } } },LinearLayout.LayoutParams(0,-2,1f))
  row.addView(Button(this).apply { text="Full screen"; setOnClickListener { toggleFullscreen() } },LinearLayout.LayoutParams(0,-2,1f)); root.addView(row)
  quality=Spinner(this).apply {
   adapter=ArrayAdapter(this@MainActivity,android.R.layout.simple_spinner_dropdown_item,listOf("Auto quality","1080p max","720p max","480p max","360p max"))
   onItemSelectedListener=object:AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent:AdapterView<*>?) {}
    override fun onItemSelected(parent:AdapterView<*>?,view:View?,position:Int,id:Long) {
     val max=when(position){1->1080;2->720;3->480;4->360;else->Int.MAX_VALUE}
     player?.let { it.trackSelectionParameters=it.trackSelectionParameters.buildUpon().setMaxVideoSize(Int.MAX_VALUE,max).build() }
    }
   }
  }; root.addView(quality); setContentView(root)
 }
 private fun openUrl(url:String) {
  if(!url.startsWith("https://",true)&&!url.startsWith("http://",true)){statusText.text="Enter a valid http(s) URL";return}
  titleText.text=url.substringAfter("://").substringBefore('/').ifBlank{"JON Player"}
  val lower=url.lowercase()
  val direct=listOf(".m3u8",".mpd",".mp4",".mkv",".webm",".mp3",".aac",".m4a",".ts",".mov",".avi").any{lower.contains(it)}
  if(!direct){player?.pause();playerView.visibility=View.GONE;webView.visibility=View.VISIBLE;webView.loadUrl(url);statusText.text="● WEB PLAYER"}
  else {webView.stopLoading();webView.visibility=View.GONE;playerView.visibility=View.VISIBLE
   try {player?.setMediaItem(MediaItem.fromUri(url));player?.prepare();player?.play();statusText.text=if(lower.contains(".m3u8")||lower.contains(".mpd")) "● LIVE / STREAM" else "● VIDEO"}
   catch(e:Exception){statusText.text="Unable to open this media URL"}
  }
 }
 private fun toggleFullscreen() {
  full=!full
  if(full){requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);window.decorView.systemUiVisibility=5894}
  else{requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_VISIBLE}
 }
 override fun onStop(){super.onStop();player?.pause()}
 override fun onDestroy(){player?.release();player=null;webView.destroy();super.onDestroy()}
}
