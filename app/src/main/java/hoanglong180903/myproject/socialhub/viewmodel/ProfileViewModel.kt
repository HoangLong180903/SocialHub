package hoanglong180903.myproject.socialhub.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import hoanglong180903.myproject.socialhub.repository.ProfileRepository

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    val repository = ProfileRepository(application)
    val isSuccessful : LiveData<Boolean>
    init {
        isSuccessful = repository.isSuccessful
    }
    fun updateUser(uid: String, name: String, selectedImage: Uri?){
        repository.updateUser(uid, name,
            selectedImage)
    }
}