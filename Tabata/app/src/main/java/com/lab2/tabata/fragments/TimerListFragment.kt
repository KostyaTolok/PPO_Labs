package com.lab2.tabata.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.lab2.tabata.R
import com.lab2.tabata.TimerActivity
import com.lab2.tabata.adapter.TimersAdapter
import com.lab2.tabata.data.Timer
import com.lab2.tabata.data.TimersDatabase
import kotlinx.android.synthetic.main.fragment_timer_list.*
import kotlinx.coroutines.launch

class TimerListFragment : BaseFragment() {

    private var adapter: TimersAdapter = TimersAdapter()
    private var timers: List<Timer>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timer_list, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TimerListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(context)
        launch {
            context?.let {
                timers = TimersDatabase.getDatabase(it).timerDao().getTimers()
                adapter.setTimers(timers as ArrayList<Timer>)
                recycler_view.adapter = adapter
            }
        }

        adapter.setListeners(onPlayClicklistener, onDeleteClicklistener, onEditClicklistener)

        addTimerButton.setOnClickListener {
            replaceAndAddToBackStackFragment(TimerEditFragment.newInstance())
        }

        clearButton.setOnClickListener{
            launch {
                context?.let {
                    TimersDatabase.getDatabase(it).timerDao().clearTimers()
                }
                replaceFragment(newInstance())
            }
        }
    }

    private val onEditClicklistener = object : TimersAdapter.OnItemClickListener {

        override fun onClicked(timerId: Int?) {

            if (timerId != null) {
                val bundle = Bundle()
                bundle.putInt("timerId", timerId)

                val fragment = TimerEditFragment.newInstance()
                fragment.arguments = bundle

                replaceAndAddToBackStackFragment(fragment)
            }
        }
    }

    private val onDeleteClicklistener = object : TimersAdapter.OnItemClickListener {

        override fun onClicked(timerId: Int?) {

            if (timerId != null) {

                launch {
                    context?.let {
                        val timer = TimersDatabase.getDatabase(it).timerDao().getTimer(timerId)

                        TimersDatabase.getDatabase(it).timerDao().deleteTimer(timer)

                    }
                    replaceFragment(newInstance())
                }

            }
        }
    }

    private val onPlayClicklistener = object : TimersAdapter.OnItemClickListener {

        override fun onClicked(timerId: Int?) {

            if (timerId != null) {
                launch {
                    context?.let {
                        val timer = TimersDatabase.getDatabase(it).timerDao().getTimer(timerId)

                        val intent = Intent(activity, TimerActivity::class.java)
                        intent.putExtra("timer", timer)
                        startActivity(intent)
                    }
                }

            }
        }
    }

}