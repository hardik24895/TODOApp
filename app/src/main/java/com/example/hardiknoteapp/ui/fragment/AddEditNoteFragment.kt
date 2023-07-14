package com.example.hardiknoteapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hardiknoteapp.R
import com.example.hardiknoteapp.data.modal.NoteModal
import com.example.hardiknoteapp.databinding.FragmentAddEditNoteBinding
import com.example.hardiknoteapp.extension.getValue
import com.example.hardiknoteapp.extension.hideKeyboard
import com.example.hardiknoteapp.extension.showSnackBar
import com.example.hardiknoteapp.ui.viewmodal.NoteViewModal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditNoteFragment : Fragment() {
    private val noteViewModal: NoteViewModal by viewModels()
    lateinit var binding: FragmentAddEditNoteBinding
    private val args: AddEditNoteFragmentArgs by navArgs()
    private var isEdit = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToEvents()
        getSafeArgData()
        viewClickEvent()
    }

    /**
     * Get Flow Event
     */
    private fun subscribeToEvents() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                noteViewModal.noteEvent.collect { event ->
                    when (event) {
                        is NoteViewModal.NoteEvent.OnSuccessMsg -> onSuccess(event.response)
                        is NoteViewModal.NoteEvent.OnError -> onFailed(event.error)
                        else -> {

                        }
                    }

                }
            }
        }

    }

    private fun getSafeArgData() {
        isEdit = args.NoteModalUpdate != null
        if (isEdit) {
            binding.include.tvTitle.text = getString(R.string.edit_note)
            binding.include.imgDelete.visibility = View.VISIBLE
            binding.edtTitle.setText(args.NoteModalUpdate?.title.toString())
            binding.edtDesc.setText(args.NoteModalUpdate?.description.toString())
        } else {
            binding.include.tvTitle.text = getString(R.string.new_note)
        }
    }

    private fun viewClickEvent(){
        binding.include.imgSave.setOnClickListener {
            insertOrUpdateNote(it)
        }
        binding.include.imgDelete.setOnClickListener {
            deleteNote()
        }
        binding.include.imgBack.setOnClickListener {
            it.findNavController().navigate(R.id.action_addEditNoteFragment_to_noteList)
        }
    }

    private fun deleteNote() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.warning)
        builder.setMessage(R.string.are_you_want_to_sure_delete_this_note)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { _, _ ->
            noteViewModal.deleteNote(args.NoteModalUpdate!!)
        }

        builder.setNegativeButton("No") { _, _ ->


        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun insertOrUpdateNote(it: View) {
        hideKeyboard(requireActivity())
        val title = binding.edtTitle.getValue()
        val desc = binding.edtDesc.getValue()
        val note = NoteModal(title = title, description = desc)
        if (title.isBlank()) it.showSnackBar("Enter Title") else if (desc.isBlank()) it.showSnackBar(
            "Enter Description"
        ) else {
            if (!isEdit) {
                noteViewModal.insertNote(note)
            } else {
                note.noteId = args.NoteModalUpdate!!.noteId
                note.date = System.currentTimeMillis()
                noteViewModal.updateNote(note)
            }
        }
    }

    private fun onFailed(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    private fun onSuccess(response: String) {
        Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show()
        binding.edtTitle.findNavController().navigate(R.id.action_addEditNoteFragment_to_noteList)
    }

}