package com.lab2.notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.lab2.notes.R
import com.lab2.notes.entities.Note
import kotlinx.android.synthetic.main.item_rv_note.view.*

class NotesAdapter() :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private var notes :ArrayList<Note> = ArrayList()

    fun setNotes(notes: ArrayList<Note>){
        this.notes = notes
    }

    private var listener: OnItemClickListener? = null

    fun setListener(listener: OnItemClickListener?){
        this.listener = listener
    }

    class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_note, parent, false)

        return NotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.itemView.cardTitle.text = notes[position].title
        holder.itemView.cardText.text = notes[position].text
        holder.itemView.cardView.setOnClickListener {
            listener!!.onClicked(notes[position].id)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    interface OnItemClickListener{
        fun onClicked(noteId: Int?)
    }
}