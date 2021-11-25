package com.lab2.tabata.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.lab2.tabata.R
import com.lab2.tabata.data.Timer
import com.lab2.tabata.data.TimersDatabase
import com.lab2.tabata.filters.TimerInputFilter
import kotlinx.android.synthetic.main.fragment_timer_edit.*
import kotlinx.coroutines.launch

class TimerEditFragment : BaseFragment() {

    private var timerId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            timerId = requireArguments().getInt("timerId", -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timer_edit, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TimerEditFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (timerId != -1) {
            launch {
                context?.let {
                    val timer = TimersDatabase.getDatabase(it).timerDao().getTimer(timerId)

                    timer_title.setText(timer.name, TextView.BufferType.EDITABLE)
                    warm_up_min.setText(getMinutes(timer.warmUpTime))
                    warm_up_s.setText(getSeconds(timer.warmUpTime))
                    work_min.setText(getMinutes(timer.workTime))
                    work_s.setText(getSeconds(timer.workTime))
                    rest_min.setText(getMinutes(timer.restTime))
                    rest_s.setText(getSeconds(timer.restTime))
                    cooldown_min.setText(getMinutes(timer.cooldownTime))
                    cooldown_s.setText(getSeconds(timer.cooldownTime))
                    repeats.setText(timer.repeats.toString())
                    cycles.setText(timer.cycles.toString())
                    select_color.setBackgroundColor(Color.parseColor(timer.color))
                }

            }
        }

        setFilters()

        saveTimerBtn.setOnClickListener {
            saveTimer()
        }

        cancelBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        select_color.setOnClickListener {
            createColorPicker((select_color.background as ColorDrawable).color)
        }
    }

    private fun saveTimer() {
        launch {
            val name = timer_title.text.toString()
            val warmUp =
                warm_up_min.text.toString().toInt() * 60 + warm_up_s.text.toString().toInt()
            val work = work_min.text.toString().toInt() * 60 + warm_up_s.text.toString().toInt()
            val rest = rest_min.text.toString().toInt() * 60 + rest_s.text.toString().toInt()
            val cooldown =
                cooldown_min.text.toString().toInt() * 60 + cooldown_s.text.toString().toInt()
            val repeats = if (repeats.text.toString() == "") 0 else repeats.text.toString().toInt()
            val cycles = if (cycles.text.toString() == "") 0 else cycles.text.toString().toInt()
            val color = java.lang.String.format(
                "#%06X",
                0xFFFFFF and (select_color.background as ColorDrawable).color
            )

            val timer = Timer(name, warmUp, work, rest, repeats, cycles, cooldown, color)

            context?.let {
                if (timerId == -1) {
                    TimersDatabase.getDatabase(it).timerDao().insertTimer(timer)
                } else {
                    timer.id = timerId
                    TimersDatabase.getDatabase(it).timerDao().updateTimer(timer)
                }
            }
            cancelBtn.callOnClick()
        }
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

    private fun createColorPicker(defaultColor: Int){
        context?.let {
            MaterialColorPickerDialog
                .Builder(it)
                .setColorShape(ColorShape.SQAURE)
                .setColorSwatch(ColorSwatch._700)
                .setDefaultColor(defaultColor)
                .setColorListener { color, _ -> select_color.setBackgroundColor(color) }
                .show()
        }
    }

    private fun setFilters(){
        val filters = arrayOf<InputFilter>(TimerInputFilter())
        warm_up_min.filters = filters
        warm_up_s.filters = filters
        work_min.filters = filters
        work_s.filters = filters
        rest_min.filters = filters
        rest_s.filters = filters
        cooldown_min.filters = filters
        cooldown_s.filters = filters
    }
}