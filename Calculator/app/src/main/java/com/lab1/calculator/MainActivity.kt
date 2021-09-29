package com.lab1.calculator

import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lab1.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder


class MainActivity : AppCompatActivity() {

    private var isFloat = false
    private val basicFragment = BasicFragment()
    private val scienceFragment = ScienceFragment()
    private val inputViewModel :InputViewModel by viewModels()
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, basicFragment)
                commit()
            }
        }

        inputViewModel.currentInput.observe(this, Observer {
            binding.inputText.text = it.toString()
        })

        inputViewModel.currentIsFloat.observe(this, Observer {
            isFloat = it
        })
        val manager = this.packageManager
        val info = manager.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES)
        setTitle("Calculator ${info.versionName}")
    }

    fun onSwitchToScience(view: View){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, scienceFragment)
            commit()
        }
    }

    fun onSwitchToBasic(view: View){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, basicFragment)
            commit()
        }
    }

}