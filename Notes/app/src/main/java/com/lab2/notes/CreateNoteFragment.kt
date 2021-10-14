package com.lab2.notes

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.lab2.notes.database.NotesDatabase
import com.lab2.notes.entities.Note
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.contracts.contract

class CreateNoteFragment : BaseFragment() {

    private var noteId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            noteId = requireArguments().getInt("noteId", -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateNoteFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (noteId != -1) {
            launch {
                context?.let {
                    val note = NotesDatabase.getDatabase(it).noteDao().getNoteById(noteId)

                    noteTitle.setText(note.title)
                    noteDateTime.text = note.dateTime
                    noteText.setText(note.text)

                }
                deleteNoteBtn.visibility = View.VISIBLE

                deleteNoteBtn.setOnClickListener {
                    deleteNote()
                }
            }
        }

        val formatter = SimpleDateFormat("dd.MM.yyyy hh:mm")
        val currentDate = formatter.format(Date())

        noteDateTime.text = currentDate

        btnEnd.setOnClickListener {
            if (noteId == -1) {
                saveNote()
            }
            else{
                updateNote()
            }
        }


        btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun saveNote() {
        if (noteTitle.text.isNullOrBlank()) {
            noteTitle.setText(noteDateTime.text.toString())
        }

        launch {
            val note = Note(
                noteTitle.text.toString(),
                noteDateTime.text.toString(),
                noteText.text.toString()
            )

            context?.let {
                NotesDatabase.getDatabase(it).noteDao().insertNote(note)
            }
            btnBack.callOnClick()
        }
    }

    private fun updateNote(){
        if (noteTitle.text.isNullOrBlank()) {
            noteTitle.setText(noteDateTime.text.toString())
        }

        launch {
            context?.let {
                val note = NotesDatabase.getDatabase(it).noteDao().getNoteById(noteId)

                note.title = noteTitle.text.toString()
                note.text = noteText.text.toString()
                note.dateTime = noteDateTime.text.toString()

                NotesDatabase.getDatabase(it).noteDao().updateNote(note)
            }
            btnBack.callOnClick()
        }
    }

    private fun deleteNote(){

        launch {
            context?.let {
                val note = NotesDatabase.getDatabase(it).noteDao().getNoteById(noteId)

                NotesDatabase.getDatabase(it).noteDao().deleteNote(note)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

}