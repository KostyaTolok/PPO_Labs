package com.lab2.tabata.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lab2.tabata.R
import com.lab2.tabata.data.Timer
import kotlinx.android.synthetic.main.rv_item_timer.view.*

class TimersAdapter : RecyclerView.Adapter<TimersAdapter.TimersViewHolder>() {
    private var timers: ArrayList<Timer> = ArrayList()

    fun setTimers(timers: ArrayList<Timer>) {
        this.timers = timers
    }

    private var onPlayClicklistener: OnItemClickListener? = null
    private var onDeleteClickListener: OnItemClickListener? = null
    private var onEditClickListener: OnItemClickListener? = null

    fun setListeners(
        onPlayClicklistener: OnItemClickListener?,
        onDeleteClickListener: OnItemClickListener?,
        onEditClickListener: OnItemClickListener?
    ) {

        this.onPlayClicklistener = onPlayClicklistener
        this.onDeleteClickListener = onDeleteClickListener
        this.onEditClickListener = onEditClickListener
    }


    class TimersViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_item_timer, parent, false)

        return TimersViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimersViewHolder, position: Int) {
        holder.itemView.timerName.text = timers[position].name
        holder.itemView.timerItemView.setCardBackgroundColor(
            Color.parseColor(timers[position].color)
        )
        holder.itemView.timerItemView.setOnClickListener {
            onPlayClicklistener!!.onClicked(timers[position].id)
        }
        holder.itemView.editButton.setOnClickListener {
            onEditClickListener!!.onClicked(timers[position].id)
        }
        holder.itemView.deleteButton.setOnClickListener {
            onDeleteClickListener!!.onClicked(timers[position].id)
        }
    }

    override fun getItemCount(): Int {
        return timers.size
    }

    interface OnItemClickListener {
        fun onClicked(timerId: Int?)
    }
}