package com.example.cloudnote.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.cloudnote.R
import com.example.cloudnote.data.models.noteModels.NoteResponse
import com.example.cloudnote.databinding.FragmentHomeBinding
import com.example.cloudnote.ui.adapters.NoteAdapter
import com.example.cloudnote.utils.Constants.BUNDLE_KEY_NOTE_ITEM
import com.example.cloudnote.utils.NetworkResult
import com.example.cloudnote.utils.TokenManager
import com.example.cloudnote.viewModels.NoteViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val noteViewModel by viewModels<NoteViewModel>()

    private lateinit var noteAdapter: NoteAdapter

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        // Initializing adapter
        noteAdapter = NoteAdapter(::onNoteClicked)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteViewModel.getNotes()
        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        binding.rvNote.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvNote.adapter = noteAdapter

        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_noteFragment)
        }

        binding.btnLogout.setOnClickListener {
            tokenManager.clearToken()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }

    private fun setupObservers() {
        noteViewModel.notesLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            when(it) {
                is NetworkResult.Error -> {
//                    binding.tvToken.text = it.message
                    // TODO: Implement swipe to refresh
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    noteAdapter.submitList(it.data!!)
                }
            }
        }
    }

    private fun onNoteClicked(noteResponse: NoteResponse) {
        val bundle = Bundle()
        bundle.putString(BUNDLE_KEY_NOTE_ITEM, Gson().toJson(noteResponse))
        findNavController().navigate(R.id.action_homeFragment_to_noteFragment, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}