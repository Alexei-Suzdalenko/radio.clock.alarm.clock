package radio.clock.alarm.clock.utils
import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer

class App: Application() {
    companion object{
        var player: SimpleExoPlayer? = null
        lateinit var mp: MediaPlayer
        lateinit var globalSharedPreferences: SharedPreferences
        lateinit var globalEditor: SharedPreferences.Editor
        val channel_id = "Radio Alarm Clock"

        fun play(c: Context){
            val puth = globalSharedPreferences.getString("url_radio", "http://89.179.72.53:8070/live").toString()
            if( player == null ){ player = SimpleExoPlayer.Builder( c ).build() }
            player!!.setMediaItem(MediaItem.fromUri( puth ))
            player!!.prepare()
            player!!.play()
        }
        fun stop(){ player?.pause() }
        fun mpPlay(c: Context){ mp.isLooping = true; mp.start() }
        fun mpStop(){
            if(mp.isPlaying) {
                mp.stop()
                mp.release() } } }

    @SuppressLint("CommitPrefEdits")
    override fun onCreate() {
        super.onCreate()
        player = SimpleExoPlayer.Builder( this ).build()
        val n        = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        mp           = MediaPlayer.create(this, n)
        createMyNotificationChannel()
        globalSharedPreferences = getSharedPreferences("one_tag", Context.MODE_PRIVATE)
        globalEditor = globalSharedPreferences.edit()
    }

    private fun createMyNotificationChannel(){
        val name = "Radio Alarm"
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val serChannel = NotificationChannel(channel_id, name, NotificationManager.IMPORTANCE_HIGH)
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serChannel)
        }
    }
}