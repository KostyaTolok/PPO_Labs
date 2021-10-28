package com.lab2.tabata.app

import androidx.appcompat.app.AppCompatDelegate
import com.zeugmasolutions.localehelper.LocaleAwareApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.util.*

class TimerApp : LocaleAwareApplication() {

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = getSharedPreferences("Preferences", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("first", true))
            Locale.setDefault(Locale("eng"))
        sharedPreferences.edit().putBoolean("first", false).apply()

        val darkTheme: Boolean = sharedPreferences.getBoolean("dark_theme", false)
        updateTheme(darkTheme)
    }

    companion object {
        fun updateTheme(darkTheme: Boolean) {
            if (darkTheme) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }
}