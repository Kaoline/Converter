package kaoline.converter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kaoline.converter.ui.main.ConverterFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ConverterFragment.newInstance())
                .commitNow()
        }
    }
}