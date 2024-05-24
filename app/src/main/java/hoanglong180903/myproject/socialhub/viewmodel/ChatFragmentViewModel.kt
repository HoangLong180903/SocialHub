package hoanglong180903.myproject.socialhub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hoanglong180903.myproject.socialhub.model.UserModel
import hoanglong180903.myproject.socialhub.repository.ChatFragmentRepository

class ChatFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ChatFragmentRepository(application)
    private val _users = MutableLiveData<List<UserModel>>()
    val users: LiveData<List<UserModel>> get() = _users

    fun fetchUsers() {
        repository.getUsers { userList ->
            _users.value = userList
        }
    }

    fun fetchPresence(status : String){
        repository.fetchPresence(status)
    }
}