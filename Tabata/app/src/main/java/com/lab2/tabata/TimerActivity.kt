package com.lab2.tabata

import android.os.Bundle
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.lab2.tabata.data.Timer
import com.lab2.tabata.databinding.ActivityTimerBinding
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

    private var statesCount = 0

    var isStarted: Boolean = false

    var statesNames = arrayListOf<String>()
    var statesTimes = arrayListOf<Int>()

    fun setTimer(timer: Timer) {
        this.timer = timer

        statesCount = timer.cycles * (timer.repeats * 2 + 1)
        timeValue = (timer.warmUpTime * 1000).toLong()
        timeText.value = getTime(timer.warmUpTime)
        currentState.value = resources.getString(R.string.warm_up)
        currentStateIndex = 2

        statesTimes.add(timer.warmUpTime)

        statesNames.add(resources.getString(R.string.warm_up))
        statesTimes.add(timer.warmUpTime)

        for (i in 0 until timer.cycles) {
            for (j in 0 until timer.repeats) {
                statesNames.add(resources.getString(R.string.work))
                statesNames.add(resources.getString(R.string.rest))
                statesTimes.add(timer.workTime)
                statesTimes.add(timer.restTime)
            }

            statesNames.add(resources.getString(R.string.cooldown))
            statesTimes.add(timer.cooldownTime)
        }
        statesNames.add(resources.getString(R.string.complete))
    }

    fun start() {
        isStarted = true
        countDownTimer = object : CountDownTimer(timeValue, 1000) {
            override fun onFinish() {

                if (currentStateIndex < 1) {
                    currentStateIndex = 1
                } else if (currentStateIndex - 2 > currentStateIndex) {
                    return
                }

                if (currentStateIndex - 2 == statesCount) {
                    currentState.value = resources.getString(R.string.complete)

                    currentStateIndex += 1
                } else {
                    currentState.value = statesNames[currentStateIndex]


                    timeValue = statesTimes[currentStateIndex].toLong() * 1000
                    timeText.value = getTimeText(timeValue)
                    this@TimerActivity.start()
                    currentStateIndex += 1
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                timeText.value = getTimeText(timeValue)
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

    private fun getTimeText(time: Long) = getTime(time.toInt() / 1000)

    private fun getTime(time: Int): String = (getMinutes(time) + ":" + getSec(time))

    private fun getMinutes(time: Int): String = addZero(time / 60)

    private fun getSec(time: Int): String = addZero(time - (time / 60).toInt() * 60)

    private fun addZero(time: Int): String = if (time < 10) {
        "0$time"
    } else {
        "$time"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (countDownTimer != null) {
            pause()
        }
        finish()
    }


}