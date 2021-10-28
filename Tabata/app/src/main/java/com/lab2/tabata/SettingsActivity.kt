package com.lab2.tabata

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
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
            Preference.OnPreferenceChangeListener { _, newValue ->

                sharedPreferences.edit().putBoolean("dark_theme", newValue as Boolean).apply()
                TimerApp.updateTheme(newValue)
                true
            }

        val fontSizePreference = settingsFragment.findPreference<ListPreference>("text_size")!!
        fontSizePreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->

                var sizeCoef = 0f
                when (newValue as String) {
                    "small" -> sizeCoef = 0.85f
                    "medium" -> sizeCoef = 1.0f
                    "large" -> sizeCoef = 1.15f
                }
                resources.configuration.fontScale = sizeCoef
                resources.displayMetrics.scaledDensity = resources.configuration.fontScale * resources.displayMetrics.density
                baseContext.resources.updateConfiguration(resources.configuration, DisplayMetrics())
                finish()
                startActivity(Intent(this, this::class.java))
                true
            }

        val localePreference = settingsFragment.findPreference<Preference>("lang")!!
        localePreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->

                if(newValue as String == "ru")
                    updateLocale(Locales.Russian)
                else
                    updateLocale(Locales.English)
                true
            }

        super.onResume()
    }

    private fun updateTheme(darkTheme: Boolean) {
        if (darkTheme) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}