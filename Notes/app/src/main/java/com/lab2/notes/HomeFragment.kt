package com.lab2.notes

import android.icu.lang.UCharacter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lab2.notes.adapter.NotesAdapter
import com.lab2.notes.database.NotesDatabase
import com.lab2.notes.entities.Note
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment() {

    var adapter: NotesAdapter = NotesAdapter()
    var notes: List<Note>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.setHasFixedSize(true)

        recycler_view.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        launch {
            context?.let {
                notes = NotesDatabase.getDatabase(it).noteDao().getAllNotes()

                adapter.setNotes(notes as ArrayList<Note>)

                recycler_view.adapter = adapter
            }
        }

        adapter.setListener(listener)

        createNoteBtn.setOnClickListener {
            replaceFragment(CreateNoteFragment.newInstance())
        }

        search_view.setOnQueryTextListener(object:
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                var foundNotes = ArrayList<Note>()
                if (notes != null && p0 !=null) {

                    for (note in notes!!) {
                        if (note.title.lowercase(Locale.getDefault()).contains(p0.toString())){
                            foundNotes.add(note)
                        }
                    }

                }

                adapter.setNotes(foundNotes)
                adapter.notifyDataSetChanged()
                return true
            }
        })
    }

    private val listener = object: NotesAdapter.OnItemClickListener{

        override fun onClicked(noteId: Int?) {

            if (noteId != null) {
                val bundle = Bundle()
                bundle.putInt("noteId", noteId)

                val fragment = CreateNoteFragment.newInstance()
                fragment.arguments = bundle

                replaceFragment(fragment)
            }
        }
    }

}