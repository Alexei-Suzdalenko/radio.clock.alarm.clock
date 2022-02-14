package radio.clock.alarm.clock.utils
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat
import radio.clock.alarm.clock.MainActivity
import radio.clock.alarm.clock.R
import java.util.*

class ServiceAlarmClock : Service() {
    lateinit var sharedPreferences: SharedPreferences
    var dataTimeString = ""
    var title          = ""
    var name_radio     = ""
    var offSound       = ""
    private val binder = LocalBinder()

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("radio", Context.MODE_PRIVATE)
        dataTimeString    = sharedPreferences.getString("dataTimeString", "Error").toString()
        name_radio  = App.globalSharedPreferences.getString("name_radio", "").toString()
        title             = dataTimeString
        offSound          = "Off Sound"
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, MainActivity::class.java)

        var goToIndexPrepare: Intent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            goToIndexPrepare = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            goToIndexPrepare.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        } else {
            goToIndexPrepare = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            goToIndexPrepare.data = Uri.parse("package:$packageName");
        }
        val pendingIntent: PendingIntent
        val goToIndex: PendingIntent
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            pendingIntent      = PendingIntent.getActivity(this, 0, notificationIntent, FLAG_MUTABLE)
            goToIndex          = PendingIntent.getActivity(this, 1, goToIndexPrepare, FLAG_MUTABLE)
        } else{
            pendingIntent      = PendingIntent.getActivity(this, 0, notificationIntent, 0)
            goToIndex          = PendingIntent.getActivity(this, 1, goToIndexPrepare, 0)
        }
        val notification       = NotificationCompat.Builder(this, App.channel_id)
            .setContentTitle(title)
            .setContentText(name_radio)
            .setSmallIcon(R.mipmap.ic_launcher)
            .addAction(0, offSound, pendingIntent)
            .addAction(0, "Off Notification", goToIndex)
            .setContentIntent(goToIndex)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        startForeground(123, notification)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    inner  class LocalBinder : Binder() {
        fun getService(): ServiceAlarmClock = this@ServiceAlarmClock
    }

    private val mGenerator = Random()

    val randomNumber: Int get() = mGenerator.nextInt(100)

}