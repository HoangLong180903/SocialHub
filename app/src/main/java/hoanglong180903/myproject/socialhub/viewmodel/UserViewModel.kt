package hoanglong180903.myproject.socialhub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hoanglong180903.myproject.socialhub.model.UserModel
import hoanglong180903.myproject.socialhub.repository.MessageRepository
import hoanglong180903.myproject.socialhub.repository.SignUpRepository
import hoanglong180903.myproject.socialhub.repository.UserRepository

class UserViewModel (application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(application)
    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> get() = _user
    fun logOut(){
        repository.logOut()
    }

    fun fetchUser(uid: String) {
        repository.getUserData(uid) { user ->
            _user.value = user
        }
    }
}