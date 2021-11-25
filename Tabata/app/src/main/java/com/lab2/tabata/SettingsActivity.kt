package com.lab2.tabata

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import com.lab2.tabata.app.TimerApp
import com.lab2.tabata.fragments.SettingsFragment
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity
import com.zeugmasolutions.localehelper.Locales

class SettingsActivity : LocaleAwareCompatActivity() {
    private lateinit var settingsFragment: SettingsFragment
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        settingsFragment = SettingsFragment()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_frame, settingsFragment)
            .commit()

    }

    override fun onResume() {

        val themePreference = settingsFragment.findPreference<Preference>("dark_theme")!!

        themePreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, preference ->

                sharedPreferences.edit().putBoolean("dark_theme", preference as Boolean).apply()
                TimerApp.updateTheme(preference)
                true
            }

        val localePreference = settingsFragment.findPreference<Preference>("language")!!

        localePreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, preference ->

                if(preference as String == "ru")
                    updateLocale(Locales.Russian)
                else
                    updateLocale(Locales.English)
                true
            }

        val fontSizePreference = settingsFragment.findPreference<ListPreference>("font_size")!!

        fontSizePreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, preference ->

                val scaleCoefficient = TypedValue()
                when (preference as String) {
                    "small" -> resources.getValue(R.dimen.smallScaleCoefficient, scaleCoefficient, true)
                    "medium" -> resources.getValue(R.dimen.mediumScaleCoefficient, scaleCoefficient, true)
                    "large" -> resources.getValue(R.dimen.largeScaleCoefficient, scaleCoefficient, true)
                }

                resources.configuration.fontScale = scaleCoefficient.float
                resources.displayMetrics.scaledDensity = resources.configuration.fontScale * resources.displayMetrics.density
                baseContext.resources.configuration.updateFrom(resources.configuration)

                finish()
                startActivity(Intent(this, this::class.java))
                true
            }

        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}