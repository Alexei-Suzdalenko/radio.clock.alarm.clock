package radio.clock.alarm.clock
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_setting_country.*
import org.json.JSONArray
import org.json.JSONObject
import radio.clock.alarm.clock.utils.App
import radio.clock.alarm.clock.utils.CountryAdapter

class SetCountry : AppCompatActivity() {
    val URL_PUTH = "https://android-soft.github.io/alarm_app/contries.txt"
    var jsonResponses: MutableList<JSONObject> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_country)
        title = " "

        MobileAds.initialize(this) {}
        val mAdView = findViewById<AdView>(R.id.adViewStations)
        val adRequest = AdRequest.Builder().build()
        mAdView.adListener = object: AdListener(){
            override fun onAdLoaded() {
                var size = 200
                val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                if( mAdView.height > 30 ) size = mAdView.height
                params.setMargins(0, 0, 0, size)
                listViewCountry.layoutParams = params
            }
        }
        mAdView.loadAd(adRequest)

        getDataToServer()
    }

    private fun getDataToServer(){
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET, URL_PUTH,
            { response ->
                var jsonObject: JSONObject?
                jsonResponses.clear()
                try {
                    jsonObject = JSONObject(response)
                    val array: JSONArray = jsonObject.get("data") as JSONArray
                    for (i in 0 until array.length()) {
                        jsonObject = JSONObject(array.get(i).toString())
                        jsonResponses.add(jsonObject)
                    }
                    runOnUiThread {
                        val adapter = CountryAdapter(this, jsonResponses)
                        listViewCountry.adapter = adapter
                        progressBarCountry.visibility = View.GONE
                        listViewCountry.setOnItemClickListener { _, _, position, _ ->
                            App.globalEditor.putString("country", jsonResponses[position].getString("name")
                            ); App.globalEditor.commit()
                            Toast.makeText(
                                this,
                                jsonResponses[position].getString("name"),
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(Intent(this, SetRadioChannel::class.java)); finish()
                        }
                    }
                } catch (e: Exception) { }
            },
        )
        { error ->  Log.d("tag", "result = ${error.toString()}")
        }
        requestQueue.add(stringRequest)
    }
}