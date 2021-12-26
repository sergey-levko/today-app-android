package by.liauko.siarhei.app.today.activity

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import by.liauko.siarhei.app.today.ApplicationConstants
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.databinding.ActivityWidgetConfigurationBinding
import by.liauko.siarhei.app.today.recyclerview.adapter.ColorsRecyclerViewAdapter
import by.liauko.siarhei.app.today.viewmodel.WidgetParametersViewModel
import by.liauko.siarhei.app.today.widget.updateAppWidget
import java.util.Calendar
import java.util.GregorianCalendar

/**
 * Widget configuration activity
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
class DayOfYearWidgetConfigureActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityWidgetConfigurationBinding
    private lateinit var model: WidgetParametersViewModel

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private var isDefaultTheme = true

    private var onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.circle_form -> {
                viewBinding.rectangleForm.setColorFilter(getColor(R.color.widgetConfigFormsDisabled))
                viewBinding.squircleForm.setColorFilter(getColor(R.color.widgetConfigFormsDisabled))
                (it as ImageView).setColorFilter(getColor(R.color.primary))
                viewBinding.widgetPreview.setImageResource(R.drawable.widget_background_circle)
                model.form = resources.getResourceEntryName(R.drawable.widget_background_circle)
            }
            R.id.rectangle_form -> {
                viewBinding.circleForm.setColorFilter(getColor(R.color.widgetConfigFormsDisabled))
                viewBinding.squircleForm.setColorFilter(getColor(R.color.widgetConfigFormsDisabled))
                (it as ImageView).setColorFilter(getColor(R.color.primary))
                viewBinding.widgetPreview.setImageResource(R.drawable.widget_background_rectangle)
                model.form = resources.getResourceEntryName(R.drawable.widget_background_rectangle)
            }
            R.id.squircle_form -> {
                viewBinding.circleForm.setColorFilter(getColor(R.color.widgetConfigFormsDisabled))
                viewBinding.rectangleForm.setColorFilter(getColor(R.color.widgetConfigFormsDisabled))
                (it as ImageView).setColorFilter(getColor(R.color.primary))
                viewBinding.widgetPreview.setImageResource(R.drawable.widget_background_squircle)
                model.form = resources.getResourceEntryName(R.drawable.widget_background_squircle)
            }
        }
    }

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        viewBinding = ActivityWidgetConfigurationBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            viewBinding.themeSwitch.text = getString(R.string.widget_configuration_system_colors_switch)
        } else {
            viewBinding.themeSwitch.text = getString(R.string.widget_configuration_default_theme_switch)
        }

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)
        
        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }
        
        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        model = ViewModelProvider(this).get(WidgetParametersViewModel::class.java)

        initToolbar()
        initElements()
    }

    private fun initToolbar() {
        viewBinding.widgetConfigurationToolbar.setNavigationIcon(R.drawable.arrow_left)
        viewBinding.widgetConfigurationToolbar.setNavigationContentDescription(R.string.navigation_back_button_description)
        viewBinding.widgetConfigurationToolbar.setNavigationOnClickListener { finish() }
        viewBinding.widgetConfigurationToolbar.inflateMenu(R.menu.menu_widget_configuration)
        viewBinding.widgetConfigurationToolbar.setOnMenuItemClickListener {
            val sharedPreferences = getSharedPreferences(getString(R.string.widget_shared_preferences_name), Context.MODE_PRIVATE)
            sharedPreferences.edit()
                .putBoolean("${appWidgetId}_is_default", isDefaultTheme)
                .putString("${appWidgetId}_form_name", model.form)
                .apply()
            if (!isDefaultTheme) {
                sharedPreferences.edit()
                    .putInt("${appWidgetId}_background_color", model.backgroundColor)
                    .putInt("${appWidgetId}_opacity", model.opacity)
                    .putInt("${appWidgetId}_text_color", model.textColor)
                    .apply()
            }

            val appWidgetManager = AppWidgetManager.getInstance(this)
            updateAppWidget(this, appWidgetManager, appWidgetId)
            
            val resultValue = Intent()
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            setResult(RESULT_OK, resultValue)
            finish()

            return@setOnMenuItemClickListener true
        }
    }

    private fun initElements() {
        viewBinding.dayPreviewText.text = GregorianCalendar.getInstance().get(Calendar.DAY_OF_YEAR).toString()
        viewBinding.customizationForm.visibility = if (viewBinding.themeSwitch.isChecked) View.GONE else View.VISIBLE

        viewBinding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewBinding.customizationForm.visibility = if (!isChecked) View.VISIBLE else View.GONE

            if (isChecked) {
                viewBinding.widgetPreview.setColorFilter(getColor(R.color.widgetBackground))
                viewBinding.dayPreviewText.setTextColor(getColor(R.color.widgetText))
            }

            isDefaultTheme = isChecked
        }

        // Widget preview initialization
        viewBinding.widgetPreview.setColorFilter(model.backgroundColor)
        viewBinding.dayPreviewText.setTextColor(model.textColor)
        viewBinding.widgetPreview.imageAlpha = model.opacity
        when(model.form) {
            resources.getResourceEntryName(R.drawable.widget_background_circle) -> {
                viewBinding.widgetPreview.setImageResource(R.drawable.widget_background_circle)
                viewBinding.circleForm.setColorFilter(getColor(R.color.primary))
            }
            resources.getResourceEntryName(R.drawable.widget_background_rectangle) -> {
                viewBinding.widgetPreview.setImageResource(R.drawable.widget_background_rectangle)
                viewBinding.rectangleForm.setColorFilter(getColor(R.color.primary))
            }
            resources.getResourceEntryName(R.drawable.widget_background_squircle) -> {
                viewBinding.widgetPreview.setImageResource(R.drawable.widget_background_squircle)
                viewBinding.squircleForm.setColorFilter(getColor(R.color.primary))
            }
        }
        viewBinding.circleForm.setOnClickListener(onClickListener)
        viewBinding.rectangleForm.setOnClickListener(onClickListener)
        viewBinding.squircleForm.setOnClickListener(onClickListener)

        // Opacity seek bar initialization
        viewBinding.opacitySeekBar.progress = ApplicationConstants.OPACITY_MAX_VALUE - model.opacity
        viewBinding.opacitySeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewBinding.widgetPreview.imageAlpha = ApplicationConstants.OPACITY_MAX_VALUE - progress
                model.opacity = ApplicationConstants.OPACITY_MAX_VALUE - progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // Color selector initialization
        val colors = resources.getIntArray(R.array.colors_picker_values)
        val rvAdapter = ColorsRecyclerViewAdapter(
            colors,
            object: ColorsRecyclerViewAdapter.RecyclerViewOnItemClickListener {
                override fun onItemClick(item: Int) {
                    if (viewBinding.backgroundRadioButton.isChecked) {
                        viewBinding.widgetPreview.setColorFilter(item)
                        model.backgroundColor = item
                    } else {
                        viewBinding.dayPreviewText.setTextColor(item)
                        model.textColor = item
                    }
                }
            }
        )
        viewBinding.colorsRv.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 10)
            adapter = rvAdapter
        }
        viewBinding.colorsRv.isNestedScrollingEnabled = false
    }
}
