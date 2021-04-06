package radio.clock.alarm.clock
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_set_radio_channel.*
import org.json.JSONObject
import radio.clock.alarm.clock.utils.App
import java.net.URL

class SetRadioChannel : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_radio_channel)
        title = " "
        MobileAds.initialize(this) {}
        val mAdView = findViewById<AdView>(R.id.adViewRadios)
        val adRequest = AdRequest.Builder().build()
        mAdView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                var size = 200
                val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                if( mAdView.height > 30 ) size = mAdView.height
                params.setMargins(0, 0, 0, size)
                listView.layoutParams = params
            }
        }
        mAdView.loadAd(adRequest)

        val currentCountry   = App.globalSharedPreferences.getString("country", "none").toString()
        title = currentCountry
        Toast.makeText(this, currentCountry, Toast.LENGTH_LONG).show()

        val one = Thread{
            try{ // https://www.radioguide.fm/
                val response = when (currentCountry) {
                    "USA" ->      { URL("https://android-soft.github.io/alarm_app/stations/usa.js").readText() }
                    "China" ->    { URL("https://android-soft.github.io/alarm_app/stations/china.js").readText() }
                    "France"  ->  { URL("https://android-soft.github.io/alarm_app/stations/france.js").readText() }
                    "Germany" ->  { URL("https://android-soft.github.io/alarm_app/stations/ger.js").readText() }
                    "Italy" ->    { URL("https://android-soft.github.io/alarm_app/stations/italy.js").readText() }
                    "Japon" ->    { URL("https://android-soft.github.io/alarm_app/stations/japon.js").readText() }
                    "Polska" ->   { URL("https://android-soft.github.io/alarm_app/stations/pl.js").readText() }
                    "Portugal" -> { URL("https://android-soft.github.io/alarm_app/stations/pr.js").readText() }
                    "Spain" ->    { URL("https://android-soft.github.io/alarm_app/stations/spain.js").readText() }
                    "Ukraine" ->  { URL("https://android-soft.github.io/alarm_app/stations/ua.js").readText() }
                    "Belorus" ->  { URL("https://android-soft.github.io/alarm_app/stations/bl.js").readText() }
                    else -> { // Russia
                        URL("https://android-soft.github.io/alarm_app/stations/rus.js").readText()
                    }
                }
                val resultUrl = JSONObject(response)
                val name      = resultUrl.getJSONArray("name")
                val na        = Array(name.length()){name.getString(it)}
                val uriLong   = resultUrl.getJSONArray("uri")
                val uri       = Array(uriLong.length()){uriLong.getString(it)}

                runOnUiThread{
                    val adapter = ArrayAdapter(this, R.layout.text, na)
                    listView.adapter = adapter
                    progressBar.visibility = View.GONE
                    listView.setOnItemClickListener{ _, view, position, _ ->
                        memoryStreamChannel(view, na[position], uri[position])
                    }
                }
            } catch (e: Exception){
                Log.d("tag", "" + e.message)}
        }
        one.start()

    }

    private fun memoryStreamChannel(view: View, name_radio: String, url_radio: String) {
        App.globalEditor.putString("name_radio", name_radio)
        App.globalEditor.putString("url_radio", url_radio)
        App.globalEditor.apply()
        Toast.makeText(this, name_radio, Toast.LENGTH_LONG).show()
        finish()
    }
}