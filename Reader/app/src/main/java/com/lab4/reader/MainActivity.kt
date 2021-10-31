package com.lab4.reader

import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.lab4.reader.fragments.NewsListFragment
import com.lab4.reader.receivers.NetworkReceiver
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var receiver: NetworkReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("Preferences", MODE_PRIVATE)

        url_edit.setText(sharedPreferences.getString("url", ""))

        receiver = NetworkReceiver(network_image)

        search_btn.setOnClickListener {
            sharedPreferences.edit().putString("url", url_edit.text.toString()).apply()
            replaceFragment(NewsListFragment.newInstance(url_edit.text.toString()))
        }

        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {

            replace(R.id.frame_layout, fragment, fragment.javaClass.simpleName)
            commit()
        }
    }
}