package by.liauko.siarhei.app.today.activity

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.core.view.size
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.model.WidgetParameters
import by.liauko.siarhei.app.today.widget.createAppWidget
import com.google.android.material.switchmaterial.SwitchMaterial
import java.util.Calendar
import java.util.GregorianCalendar

/**
 * Widget configuration activity
 *
 * @author Siarhei Liauko
 * @since 1.0.0
 */
class DayOfYearWidgetConfigureActivity : Activity() {

    private val opacityMaxValue = 255

    private lateinit var preview: ImageView
    private lateinit var previewText: TextView
    private lateinit var circleForm: ImageView
    private lateinit var rectangleForm: ImageView
    private lateinit var squircleForm: ImageView
    private lateinit var widgetParameters: WidgetParameters

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    private var onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.circle_form -> {
                rectangleForm.setColorFilter(getColor(R.color.onBackground))
                squircleForm.setColorFilter(getColor(R.color.onBackground))
                (it as ImageView).setColorFilter(getColor(R.color.primary))
                preview.setImageResource(R.drawable.widget_background_circle)
                widgetParameters.form = R.drawable.widget_background_circle
            }
            R.id.rectangle_form -> {
                circleForm.setColorFilter(getColor(R.color.onBackground))
                squircleForm.setColorFilter(getColor(R.color.onBackground))
                (it as ImageView).setColorFilter(getColor(R.color.primary))
                preview.setImageResource(R.drawable.widget_background_rectangle)
                widgetParameters.form = R.drawable.widget_background_rectangle
            }
            R.id.squircle_form -> {
                circleForm.setColorFilter(getColor(R.color.onBackground))
                rectangleForm.setColorFilter(getColor(R.color.onBackground))
                (it as ImageView).setColorFilter(getColor(R.color.primary))
                preview.setImageResource(R.drawable.widget_background_squircle)
                widgetParameters.form = R.drawable.widget_background_squircle
            }
        }
    }

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_widget_configure)

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

        widgetParameters = WidgetParameters(
            R.drawable.widget_background_circle,
            getColor(R.color.widgetBackground),
            opacityMaxValue,
            getColor(R.color.primary)
        )

        initToolbar()
        initElements()
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.widget_configuration_toolbar)
        toolbar.setNavigationIcon(R.drawable.arrow_left)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.inflateMenu(R.menu.menu_widget_configuration)
        toolbar.setOnMenuItemClickListener {
            val appWidgetManager = AppWidgetManager.getInstance(this)
            createAppWidget(this, appWidgetManager, appWidgetId, widgetParameters)
            
            val resultValue = Intent()
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            setResult(RESULT_OK, resultValue)
            finish()

            return@setOnMenuItemClickListener true
        }
    }

    private fun initElements() {
        preview = findViewById(R.id.widget_preview)

        previewText = findViewById(R.id.day_preview_text)
        previewText.text = GregorianCalendar.getInstance().get(Calendar.DAY_OF_YEAR).toString()
        
        val customizationForm = findViewById<ScrollView>(R.id.customization_form)
        customizationForm.visibility = View.GONE
        
        findViewById<SwitchMaterial>(R.id.theme_switch).setOnCheckedChangeListener { _, isChecked ->
            customizationForm.visibility = if (!isChecked) View.VISIBLE else View.GONE

            if (isChecked) {
                preview.setImageResource(R.drawable.widget_background_circle)
                preview.setColorFilter(getColor(R.color.widgetBackground))
                previewText.setTextColor(getColor(R.color.primary))
                circleForm.setColorFilter(getColor(R.color.primary))
                rectangleForm.setColorFilter(getColor(R.color.onBackground))
                squircleForm.setColorFilter(getColor(R.color.onBackground))
            }
        }

        val grid = findViewById<GridLayout>(R.id.colors_grid)
        val colors = resources.getIntArray(R.array.colors_picker_values)
        for (i in 0 until grid.size) {
            val imageView = grid[i] as ImageView
            imageView.setColorFilter(colors[i])
            imageView.tag = colors[i]
            imageView.setOnClickListener {
                val color = it.tag as Int
                if (findViewById<RadioButton>(R.id.background_radio_button).isChecked) {
                    preview.setColorFilter(color)
                    widgetParameters.backgroundColor = color
                } else {
                    previewText.setTextColor(color)
                    widgetParameters.textColor = color
                }
            }
        }
        
        circleForm = findViewById(R.id.circle_form)
        circleForm.setColorFilter(getColor(R.color.primary))
        circleForm.setOnClickListener(onClickListener)

        rectangleForm = findViewById(R.id.rectangle_form)
        rectangleForm.setOnClickListener(onClickListener)

        squircleForm = findViewById(R.id.squircle_form)
        squircleForm.setOnClickListener(onClickListener)

        findViewById<SeekBar>(R.id.opacity_seek_bar).setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                preview.imageAlpha = opacityMaxValue - progress
                widgetParameters.opacity = opacityMaxValue - progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }
}
