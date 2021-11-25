package com.lab2.tabata

import android.os.Bundle
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.lab2.tabata.data.Timer
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity
import kotlinx.android.synthetic.main.activity_timer.*

class TimerActivity : LocaleAwareCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        setTimer(intent.getSerializableExtra("timer") as Timer)

        currentState.observe(this, {
            curr_text.text = it
        })
        timeText.observe(this, {
            time.text = it
        })

        start_stop_btn.setOnClickListener {
            if (isStarted) {
                pause()
                start_stop_btn.setImageResource(R.drawable.ic_play)
            } else {
                start()
                start_stop_btn.setImageResource(R.drawable.ic_pause)
            }
        }
        replay_btn.setOnClickListener {
            finish()
            startActivity(intent)
        }
        stop_btn.setOnClickListener {
            onBackPressed()
        }
        next_btn.setOnClickListener {
            next()
        }
        prev_btn.setOnClickListener {
            previous()
        }
    }

    private lateinit var timer: Timer

    var currentState = MutableLiveData("")
    var currentStateIndex = 0

    var timeText = MutableLiveData("")

    var countDownTimer: CountDownTimer? = null
    private var timeValue: Long = 0

    var isStarted: Boolean = false

    var stateNames = arrayListOf<String>()
    var stateTimes = arrayListOf<Int>()

    fun setTimer(timer: Timer) {
        this.timer = timer

        timeValue = (timer.warmUpTime * 1000).toLong()
        timeText.value = getTimeText(timer.warmUpTime)
        currentState.value = resources.getString(R.string.warm_up)
        currentStateIndex = 1

        stateNames.add(resources.getString(R.string.warm_up))
        stateTimes.add(timer.warmUpTime)

        for (i in 0 until timer.cycles) {
            for (j in 0 until timer.repeats) {
                stateNames.add(resources.getString(R.string.work))
                stateNames.add(resources.getString(R.string.rest))
                stateTimes.add(timer.workTime)
                stateTimes.add(timer.restTime)
            }

            stateNames.add(resources.getString(R.string.cooldown))
            stateTimes.add(timer.cooldownTime)
        }
        stateNames.add(resources.getString(R.string.complete))
    }

    fun start() {
        isStarted = true
        countDownTimer = object : CountDownTimer(timeValue, 1000) {

            override fun onFinish() {

                if (currentStateIndex < 1) {
                    currentStateIndex = 0
                } else if (currentStateIndex > stateNames.size - 1) {
                    return
                }

                if (currentStateIndex == stateNames.size - 1) {
                    currentState.value = resources.getString(R.string.complete)

                    currentStateIndex += 1
                } else {
                    currentState.value = stateNames[currentStateIndex]

                    timeValue = stateTimes[currentStateIndex].toLong() * 1000
                    timeText.value = getTimeText(timeValue.toInt()/1000)
                    this@TimerActivity.start()
                    currentStateIndex += 1
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                timeText.value = getTimeText(timeValue.toInt()/1000)
                timeValue -= 1000
            }
        }.start()
    }

    fun previous() {
        if (countDownTimer != null) {
            currentStateIndex -= 2
            countDownTimer!!.cancel()
            countDownTimer!!.onFinish()
        }
    }

    fun next() {
        countDownTimer?.cancel()
        countDownTimer?.onFinish()
    }

    fun pause() {
        isStarted = false
        countDownTimer?.cancel()
    }

    private fun getTimeText(time: Int): String {
       return getMinutes(time) + ":" + getSeconds(time)
    }

    private fun getMinutes(time: Int): String {
        return addZero(time / 60)
    }

    private fun getSeconds(time: Int): String {
        return addZero(time - (time / 60) * 60)
    }

    private fun addZero(time: Int): String {
        return if (time < 10) {
            "0$time"
        } else {
            "$time"
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (countDownTimer != null) {
            pause()
        }
        finish()
    }


}