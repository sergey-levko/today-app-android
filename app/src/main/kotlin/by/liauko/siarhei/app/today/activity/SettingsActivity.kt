package by.liauko.siarhei.app.today.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.activity.fragment.SettingsFragment
import by.liauko.siarhei.app.today.databinding.ActivitySettingsBinding

/**
 * Class managing settings screen
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.toolbar.setNavigationIcon(R.drawable.arrow_left)
        viewBinding.toolbar.setNavigationContentDescription(R.string.settings_back_button_description)
        viewBinding.toolbar.setNavigationOnClickListener { finish() }

        supportFragmentManager.beginTransaction()
                .replace(R.id.settings_frame_container, SettingsFragment())
                .commit()
    }
}
