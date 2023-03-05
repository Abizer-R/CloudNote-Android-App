package com.example.cloudnote.data.respositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cloudnote.data.api.NoteAPI
import com.example.cloudnote.data.models.noteModels.NoteRequest
import com.example.cloudnote.data.models.noteModels.NoteResponse
import com.example.cloudnote.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository
@Inject constructor(private val noteAPI: NoteAPI) {

    private val TAG = NoteRepository::class.java.simpleName + "_TESTING"


    private val _notesLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val notesLiveData: LiveData<NetworkResult<List<NoteResponse>>>
        get() = _notesLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusLiveData

    suspend fun getNotes() {
        _notesLiveData.postValue(NetworkResult.Loading())

        try {
            val response = noteAPI.getNotes()
            handleGetNoteResponse(response)

        } catch (e : Exception) {
            e.printStackTrace()
            _notesLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    private fun handleGetNoteResponse(response: Response<List<NoteResponse>>) {
        if (response.isSuccessful && response.body() != null) {
            _notesLiveData.postValue(NetworkResult.Success(response.body()!!))

        } else if (response.errorBody() != null) {
            // read whole json object
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            val errorMsg = errorObj.getString("message")
            _notesLiveData.postValue((NetworkResult.Error(errorMsg)))

        } else {
            _notesLiveData.postValue((NetworkResult.Error("Something went wrong")))
        }
    }

    suspend fun createNote(noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        try {
            val response = noteAPI.createNote(noteRequest)
            handleStatusNoteResponse(response, "Note Created")

        } catch (e : Exception) {
            e.printStackTrace()
            _notesLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun updateNote(noteId: String, noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        try {
            val response = noteAPI.updateNote(noteId, noteRequest)
            handleStatusNoteResponse(response, "Note Updated")

        } catch (e : Exception) {
            e.printStackTrace()
            _notesLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun deleteNote(noteId: String) {
        _statusLiveData.postValue(NetworkResult.Loading())
        try {
            val response = noteAPI.deleteNote(noteId)
            handleStatusNoteResponse(response, "Note Deleted")

        } catch (e : Exception) {
            e.printStackTrace()
            _notesLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    private fun handleStatusNoteResponse(response: Response<NoteResponse>, message: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(message))

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            val errorMsg = errorObj.getString("message")
            _statusLiveData.postValue((NetworkResult.Error(errorMsg)))

        } else {
            _statusLiveData.postValue((NetworkResult.Error("Something went wrong")))
        }
    }
}