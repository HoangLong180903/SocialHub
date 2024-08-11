package hoanglong180903.myproject.socialhub.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import hoanglong180903.myproject.socialhub.model.Album
import hoanglong180903.myproject.socialhub.model.Track
import hoanglong180903.myproject.socialhub.repository.DeezerRepository
import hoanglong180903.myproject.socialhub.responses.TracksResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeezerViewModel(application: Application) : ViewModel() {
    private val repository: DeezerRepository = DeezerRepository(application)
    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks
    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> get() = _albums
    fun fetchTracks(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getTracks(url)
            _tracks.postValue(response.data)
        }
    }

    fun fetchAlbum(url: String){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getAlbum(url)
            _albums.postValue(response.data)
        }
    }
}
