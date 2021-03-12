package by.liauko.siarhei.app.today.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.activity.fragment.SettingsFragment

/**
 * Class managing settings screen
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initToolbar()

        supportFragmentManager.beginTransaction()
                .replace(R.id.settings_frame_container, SettingsFragment())
                .commit()
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.arrow_left)
        toolbar.setNavigationOnClickListener { finish() }
    }
}
