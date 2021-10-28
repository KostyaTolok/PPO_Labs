package com.lab2.tabata.view_models

import android.app.Application
import android.content.res.Resources
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lab2.tabata.R
import com.lab2.tabata.data.Timer

class TimerViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var timer: Timer
    lateinit var res: Resources

    var currentText = MutableLiveData("Warm-up")

    var prevText = MutableLiveData("")
    var nextText = MutableLiveData("Work")

    var timeRemainingText = MutableLiveData("00:00")
    var timePercentRemaining = MutableLiveData(100)

    var countDownTimer: CountDownTimer? = null
    private var timeRemaining: Long = 0
    private var timeRemainingStatic = timeRemaining
    var currIndex = 0
    private var stagesCount = 0
    private val interval: Long = 1000
    var isRunning: Boolean = false

    var sequenceText = arrayListOf<String>()
    var sequenceTime = arrayListOf<Int>()

    private val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()
    private val soundPool = SoundPool.Builder().setAudioAttributes(audioAttributes).build()

    fun setTimer(timer: Timer) {
        this.timer = timer
        res = getApplication<Application>().resources
        res.updateConfiguration(res.configuration, res.displayMetrics)
        setInitValues()
        soundPool.load(getApplication<Application>().applicationContext, R.raw.notification, 1)
        createSequence()
    }

    private fun setInitValues() {
        stagesCount = timer.cycles * (timer.repeats * 2 + 1)
        timeRemaining = (timer.warmUpTime * 1000).toLong()
        timeRemainingStatic = timeRemaining
        timeRemainingText.value = getTime(timer.warmUpTime)
        currentText.value = res.getString(R.string.warm_up)
        nextText.value = res.getString(R.string.work)
        currIndex = 2
    }


    private fun createSequence() {
        sequenceText.add("")
        sequenceTime.add(timer.warmUpTime)

        sequenceText.add(res.getString(R.string.warm_up))
        sequenceTime.add(timer.warmUpTime)

        for (i in 0 until timer.cycles) {
            for (j in 0 until timer.repeats) {
                sequenceText.add(res.getString(R.string.work))
                sequenceText.add(res.getString(R.string.rest))
                sequenceTime.add(timer.workTime)
                sequenceTime.add(timer.restTime)
            }

            sequenceText.add(res.getString(R.string.cooldown))
            sequenceTime.add(timer.cooldownTime)
        }
        sequenceText.add("")
        sequenceText.add("")
    }

    fun pause() {
        isRunning = false
        countDownTimer?.cancel()
    }

    private fun getTimeRemainingText(time: Long) = getTime(time.toInt() / 1000)

    fun startTimer() {
        isRunning = true
        countDownTimer = object : CountDownTimer(timeRemaining, interval) {
            override fun onFinish() {
                soundPool.play(1, 1F, 1F, 1, 0, 1F)
                timePercentRemaining.value = 100

                if (currIndex < 1) currIndex = 1
                if (currIndex - 2 > stagesCount) return

                if (currIndex - 2 == stagesCount) {
                    currentText.value = res.getString(R.string.complete)
                    prevText.value = ""
                    currIndex += 1
                } else {
                    prevText.value = sequenceText[currIndex - 1]
                    currentText.value = sequenceText[currIndex]
                    nextText.value = sequenceText[currIndex + 1]

                    timeRemaining = sequenceTime[currIndex].toLong() * 1000
                    timeRemainingStatic = timeRemaining
                    timeRemainingText.value = getTimeRemainingText(timeRemaining)
                    startTimer()
                    currIndex += 1
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                timeRemainingText.value = getTimeRemainingText(timeRemaining)
                timePercentRemaining.value = ((timeRemaining * 100) / timeRemainingStatic).toInt()
                timeRemaining -= interval
            }
        }.start()
    }

    fun prev() {
        if (countDownTimer != null) {
            currIndex -= 2
            countDownTimer!!.cancel()
            countDownTimer!!.onFinish()
        }
    }

    fun next() {
        countDownTimer?.cancel()
        countDownTimer?.onFinish()
    }

    private fun getTime(time: Int): String = (getMinutes(time) + ":" + getSec(time))

    private fun getMinutes(time: Int): String = addZero(time / 60)

    private fun getSec(time: Int): String = addZero(time - (time / 60).toInt() * 60)

    private fun addZero(time: Int): String = if (time < 10) {
        "0$time"
    } else {
        "$time"
    }
}