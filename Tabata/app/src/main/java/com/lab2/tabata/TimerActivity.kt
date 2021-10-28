package com.lab2.tabata

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lab2.tabata.data.Timer
import com.lab2.tabata.databinding.ActivityTimerBinding
import com.lab2.tabata.view_models.TimerViewModel
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity

class TimerActivity : LocaleAwareCompatActivity() {
    private val binding: ActivityTimerBinding by lazy {
        ActivityTimerBinding.inflate(layoutInflater)
    }

    private val viewModel: TimerViewModel by lazy {
        ViewModelProvider(this).get(TimerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.setTimer(intent.getSerializableExtra("timer") as Timer)

        binding.startStopBtn.setOnClickListener {
            if (viewModel.isRunning) {
                viewModel.pause()
                binding.startStopBtn.setImageResource(R.drawable.ic_play)
            } else {
                viewModel.startTimer()
                binding.startStopBtn.setImageResource(R.drawable.ic_pause)
            }
        }
        viewModel.currentText.observe(this, {
            binding.currText.text = it
        })
        viewModel.prevText.observe(this, {
            binding.prevText.text = it
        })
        viewModel.nextText.observe(this, {
            binding.nextText.text = it
        })
        viewModel.timeRemainingText.observe(this, {
            binding.time.text = it
        })
        binding.nextBtn.setOnClickListener {
            viewModel.next()
        }
        binding.prevBtn.setOnClickListener {
            viewModel.prev()
        }
        binding.replayBtn.setOnClickListener {
            finish()
            startActivity(intent)
        }
        binding.stopBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (viewModel.countDownTimer != null) viewModel.pause()
        finish()
    }


}