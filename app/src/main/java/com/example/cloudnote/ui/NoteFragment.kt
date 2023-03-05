package com.example.cloudnote.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cloudnote.R
import com.example.cloudnote.data.models.noteModels.NoteRequest
import com.example.cloudnote.data.models.noteModels.NoteResponse
import com.example.cloudnote.databinding.FragmentNoteBinding
import com.example.cloudnote.utils.Constants.BUNDLE_KEY_NOTE_ITEM
import com.example.cloudnote.utils.NetworkResult
import com.example.cloudnote.viewModels.NoteViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding : FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private val noteViewModel by viewModels<NoteViewModel>()

    private var note : NoteResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitialData()
        setupViews()
        setupObservers()
    }

    private fun setInitialData() {
        val noteJson = arguments?.getString(BUNDLE_KEY_NOTE_ITEM)
        if(noteJson != null) {
            note = Gson().fromJson(noteJson, NoteResponse::class.java)
            note?.let {
                binding.etTitle.setText(it.title)
                binding.etDescription.setText(it.description)
            }

        } else {
            binding.tvEditNoteLabel.text = "Add Note"
            binding.btnSubmit.text = "Add Note"
        }
    }

    private fun setupViews() {
        binding.btnDelete.setOnClickListener {
            note?.let {
                // This will be called only if 'note' object is not null
                noteViewModel.deleteNote(it._id)
            }
        }

        binding.btnSubmit.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text.toString()
            val noteRequest = NoteRequest(title, description)
            if (note == null) {
                noteViewModel.createNote(noteRequest)
            } else {
                noteViewModel.updateNote(note!!._id, noteRequest)
            }
        }
    }

    private fun setupObservers() {
        noteViewModel.statusLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            when(it) {
                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Success -> { findNavController().navigateUp() }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}