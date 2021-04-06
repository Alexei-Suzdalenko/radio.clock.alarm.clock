package radio.clock.alarm.clock
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import radio.clock.alarm.clock.utils.App
class Off : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_off)

        if(App.player.isPlaying) App.stop()
        if(App.mp.isPlaying)     App.mpStop()

        Toast.makeText(this, "OFF ALARM", Toast.LENGTH_LONG).show()

        finish()
    }
}