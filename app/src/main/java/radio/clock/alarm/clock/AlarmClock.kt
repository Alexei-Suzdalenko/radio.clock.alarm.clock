package radio.clock.alarm.clock
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_alarm_clock.*
import radio.clock.alarm.clock.utils.App
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AlarmClock : AppCompatActivity() {
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_clock)
        supportActionBar!!.hide()

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-7286158310312043/5060663877", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else { this.window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON) }


        off.setOnClickListener{
            if (App.player?.isPlaying == true) App.player!!.stop()
            if (App.mp.isPlaying)     App.mp.stop()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        Thread{
            try {
                while (true){
                    runOnUiThread { if (mInterstitialAd != null) { mInterstitialAd?.show(this) } }
                    Thread.sleep(3000)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start()


    }







}






