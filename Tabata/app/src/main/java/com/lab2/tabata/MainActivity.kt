package com.lab2.tabata

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.lab2.tabata.fragments.TimerListFragment
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity
import com.zeugmasolutions.localehelper.Locales
import java.util.*

class MainActivity : LocaleAwareCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(TimerListFragment.newInstance())

    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {

            replace(R.id.frame_layout, fragment, fragment.javaClass.simpleName)
            commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.tabata_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun setSharedPreferences(){
        sharedPreferences = getSharedPreferences("Preferences", MODE_PRIVATE)

        if (sharedPreferences.getBoolean("first", true))
            Locale.setDefault(Locale("en"))
        sharedPreferences.edit().putBoolean("first", false).apply()

        val darkTheme: Boolean = sharedPreferences.getBoolean("dark_theme", false)
        updateTheme(darkTheme)
    }

    private fun updateTheme(darkTheme: Boolean) {
        if (darkTheme) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}