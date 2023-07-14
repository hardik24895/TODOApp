package com.example.hardiknoteapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.hardiknoteapp.R
import com.example.hardiknoteapp.data.modal.NoteModal
import com.example.hardiknoteapp.databinding.FragmentNoteListBinding
import com.example.hardiknoteapp.extension.SnackbarActionListener
import com.example.hardiknoteapp.extension.hide
import com.example.hardiknoteapp.extension.showSnackBar
import com.example.hardiknoteapp.extension.visible
import com.example.hardiknoteapp.ui.adapter.NoteAdapter
import com.example.hardiknoteapp.ui.viewmodal.NoteViewModal
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteListFragment : Fragment(), NoteAdapter.OnItemSelected {
    private lateinit var binding: FragmentNoteListBinding
    private lateinit var noteAdapter: NoteAdapter
    private val noteViewModal: NoteViewModal by viewModels()
    private var list: MutableList<NoteModal> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        noteViewModal.getAllNotes()
        subscribeToEvents()
        binding.animAddNote.setOnClickListener {
            it.findNavController().navigate(R.id.action_noteList_to_addEditNoteFragment)
        }
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter(requireContext(), list, this)
        binding.rv.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = noteAdapter
            setHasFixedSize(true)
            val swipe = ItemTouchHelper(callbackSwipeToDelete())
            swipe.attachToRecyclerView(this)
        }
    }

    /**
     * Get Flow Event
     */
    private fun subscribeToEvents() {
        lifecycleScope.launch {
            noteViewModal.noteListEvent.observe(viewLifecycleOwner) {
                when (it) {
                    is NoteViewModal.NoteEvent.OnSuccessList -> onSuccess(it.response)
                    is NoteViewModal.NoteEvent.OnError -> onFailed(it.error)
                    else -> {

                    }
                }
            }

            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                noteViewModal.noteEvent.collect { event ->
                    when (event) {
                        is NoteViewModal.NoteEvent.OnSuccessMsg -> onSuccessMsg(event.response)
                        is NoteViewModal.NoteEvent.OnError -> onFailed(event.error)
                        else -> {

                        }
                    }

                }
            }

        }

    }

    private fun onSuccessMsg(response: String) {
        showHidePlaceHolder()
        Toast.makeText(requireContext(),response, Toast.LENGTH_SHORT).show()

    }

    private fun onFailed(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    private fun onSuccess(response: MutableList<NoteModal>) {
        list.clear()
        list.addAll(response)
        noteAdapter.notifyDataSetChanged()
        showHidePlaceHolder()
    }

    override fun onItemSelect(position: Int, data: NoteModal, action: String) {
        this.findNavController()
            .navigate(NoteListFragmentDirections.actionNoteListToAddEditNoteFragment(data))
    }

    private fun callbackSwipeToDelete(): ItemTouchHelper.SimpleCallback {
        //Swipe recycler view items on RIGHT
        return object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val deletedNote: NoteModal =
                    list.get(position)
                list.removeAt(position)
                noteAdapter.notifyItemRemoved(position)
                noteViewModal.deleteNote(deletedNote)
                Snackbar.make(binding.rv, "Deleted ${deletedNote.title}", Snackbar.LENGTH_LONG)
                    .setAction(
                        "Undo"
                    ) {
                        noteViewModal.insertNote(deletedNote)
                        list.add(position, deletedNote)
                        noteAdapter.notifyItemInserted(position)
                    }.show()
            }


        }
    }

    private fun showHidePlaceHolder(){
        if (list.isEmpty()) {
            binding.rv.hide()
            binding.animNoData.visible()
            binding.animNoData.playAnimation()
        }else{
            binding.rv.visible()
            binding.animNoData.hide()
            binding.animNoData.cancelAnimation()
        }
    }
}