package hoanglong180903.myproject.socialhub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import hoanglong180903.myproject.socialhub.database.DBHelper
import hoanglong180903.myproject.socialhub.model.Favorite
import hoanglong180903.myproject.socialhub.repository.FavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application){
    val getAllFavorite: LiveData<List<Favorite>>
    private val repository: FavoriteRepository
    init {
        val userDao = DBHelper.getDatabase(
            application
        ).dao()
        repository = FavoriteRepository(userDao)
        getAllFavorite = repository.getAllFavorite
    }

    fun insertFavorite(favorite: Favorite){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertFavorite(favorite)
        }
    }

    fun deleteDisFavorite(favorite: Favorite){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteDisFavorite(favorite)
        }
    }
}