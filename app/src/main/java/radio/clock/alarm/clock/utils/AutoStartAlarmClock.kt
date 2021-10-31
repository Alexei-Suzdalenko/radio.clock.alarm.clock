package radio.clock.alarm.clock.utils
import android.annotation.SuppressLint
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
    var time = System.currentTimeMillis().toLong()
    var dataTimeLong: Long = 0

    override fun onReceive(context: Context, intent: Intent) {
        sharedPreferences = context.getSharedPreferences("radio", Context.MODE_PRIVATE)
        alarmManager      = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        dataTimeLong  = sharedPreferences.getLong("dataTimeLong", 1).toLong()

        saveNewTime(context)

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val i = Intent(context, ServiceAlarmClock::class.java)
            i.action = "com.test.andsrvfg.AndSrvFgService"
            ContextCompat.startForegroundService(context ,i)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun saveNewTime(context: Context) {
        dataTimeLong  = sharedPreferences.getLong("dataTimeLong", 1).toLong()
        time = System.currentTimeMillis().toLong()

        if(time > dataTimeLong){
            val next = dataTimeLong + 86400000
            val edit = sharedPreferences.edit()
            edit.putLong("dataTimeLong", next)
            edit.apply()

            saveNewTime(context)
        } else {
            val alarmTime = sharedPreferences.getLong("dataTimeLong", 1).toLong()
            val intent        = Intent(context, ReceiverAlarmClock::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
            } else { alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent) }
        }
    }
}