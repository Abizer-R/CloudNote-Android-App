package com.example.cloudnote.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.cloudnote.R
import com.example.cloudnote.databinding.FragmentHomeBinding
import com.example.cloudnote.utils.NetworkResult
import com.example.cloudnote.viewModels.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val noteViewModel by viewModels<NoteViewModel>()

    // TODO: (remove) FOR TESTING [1/3]
//    @Inject
//    lateinit var noteAPI: NoteAPI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteViewModel.getNotes()
        setupObservers()
        // TODO: (remove) FOR TESTING [2/3]
    }

    private fun setupObservers() {
        noteViewModel.notesLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            when(it) {
                is NetworkResult.Error -> {
                    binding.tvToken.text = it.message
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    var text = ""
                    for(note in it.data!!) {
                        binding.tvToken.append("\ntitle: ${note.title}\ndesc: ${note.description}\n")
                    }
                }
            }
        }
    }

    // TODO: (remove) FOR TESTING [3/3]
    private fun getNotesTesting() {
//        CoroutineScope(Dispatchers.IO).launch {
//            binding.tvToken.text = "FETCHING"
//            try {
//                val response = noteAPI.getNotes()
//                withContext(Dispatchers.Main) {
//                    binding.tvToken.text = response.body().toString()
//                }
//            } catch (e : Exception) {
//                e.printStackTrace()
//                withContext(Dispatchers.Main) {
//                    binding.tvToken.text = "Something went wrong"
//
//                }
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}