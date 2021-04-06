package radio.clock.alarm.clock.utils
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.core.content.ContextCompat

class AutoStartAlarmClock : BroadcastReceiver() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var alarmManager: AlarmManager
    val time = System.currentTimeMillis().toLong()
    var dataTimeLong: Long = 0

    override fun onReceive(context: Context, intent: Intent) {
        sharedPreferences = context.getSharedPreferences("radio", Context.MODE_PRIVATE)
        alarmManager      = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        dataTimeLong  = sharedPreferences.getLong("dataTimeLong", 1)

        saveNewTime(context)

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val i = Intent(context, ServiceAlarmClock::class.java)
            i.action = "com.test.andsrvfg.AndSrvFgService"
            ContextCompat.startForegroundService(context ,i)
        }
    }

    private fun saveNewTime(context: Context) {
        if(time > dataTimeLong){
            dataTimeLong  = sharedPreferences.getLong("dataTimeLong", 1)
            val next = dataTimeLong + 86400000
            val edit = sharedPreferences.edit()
            edit.apply()
            val intent        = Intent(context, ReceiverAlarmClock::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, next, pendingIntent)
            } else { alarmManager.setExact(AlarmManager.RTC_WAKEUP, next, pendingIntent) }
            saveNewTime(context)
        }
    }
}