package radio.clock.alarm.clock.utils
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.json.JSONObject
import radio.clock.alarm.clock.R

class CountryAdapter(private val c: Context, private val jsonResponses: MutableList<JSONObject>): BaseAdapter() {
    override fun getCount(): Int {
        return jsonResponses.size
    }

    override fun getItem(item: Int): Any {
        return jsonResponses.get(item)
    }

    override fun getItemId(item: Int): Long {
        return item.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val layotInflater   = LayoutInflater.from(c)
        val rowMain         = layotInflater.inflate(R.layout.country_list , p2, false)

        val imageStation     = rowMain.findViewById<ImageView>(R.id.imageCountry)
        Picasso.get().load(jsonResponses[p0].getString("image")).into(imageStation)
        val textStation     = rowMain.findViewById<TextView>(R.id.textViewCountry)
        textStation.text = jsonResponses[p0].getString("name")

        return rowMain
    }
}