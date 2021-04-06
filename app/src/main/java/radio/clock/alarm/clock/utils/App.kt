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
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class App: Application() {
    companion object{
        lateinit var player: SimpleExoPlayer
        lateinit var mp: MediaPlayer
        lateinit var globalSharedPreferences: SharedPreferences
        lateinit var globalEditor: SharedPreferences.Editor
        val channel_id = "Radio Alarm Clock"

        fun play(c: Context, s: String){
            player.playWhenReady = true
            player.prepare(
                ProgressiveMediaSource.Factory(DefaultDataSourceFactory(c, "Mozilla")).createMediaSource(Uri.parse(s)))
        }
        fun stop(){
            player.playWhenReady = false
        }
        fun mpPlay(c: Context){
            mp.isLooping = true
            mp.start()
        }
        fun mpStop(){
            if(mp.isPlaying) {
                mp.stop()
                mp.release()
            }
        }
    }

    @SuppressLint("CommitPrefEdits")
    override fun onCreate() {
        super.onCreate()
        player = ExoPlayerFactory.newSimpleInstance(this)
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