package radio.clock.alarm.clock.utils
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import radio.clock.alarm.clock.AlarmClock
import radio.clock.alarm.clock.utils.App.Companion.globalSharedPreferences

class ReceiverAlarmClock : BroadcastReceiver() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var alarmManager: AlarmManager
    override fun onReceive(context: Context?, intent: Intent?) {

        startSound(context!!)
        val i = Intent(context.applicationContext, AlarmClock::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)
    }

    private fun startSound(context: Context){
        sharedPreferences = context.getSharedPreferences("radio", Context.MODE_PRIVATE)
        alarmManager      = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val dataTimeLong  = sharedPreferences.getLong("dataTimeLong", 1)
        val next = dataTimeLong + 86400000
        val edit = sharedPreferences.edit()
        edit.putLong("dataTimeLong", next); edit.apply()
        val intent        = Intent(context, ReceiverAlarmClock::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, next, pendingIntent)
        } else { alarmManager.setExact(AlarmManager.RTC_WAKEUP, next, pendingIntent) }

        val puth = globalSharedPreferences.getString("url_radio", "http://89.179.72.53:8070/live").toString()
        if (isInternetAvailable(context)){
            App.play(context, puth)
        } else App.mpPlay(context)
    }

    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return result
    }
}