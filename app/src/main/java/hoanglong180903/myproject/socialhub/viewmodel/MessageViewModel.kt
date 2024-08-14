package hoanglong180903.myproject.socialhub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hoanglong180903.myproject.socialhub.model.UserModel
import hoanglong180903.myproject.socialhub.repository.MessageRepository


class MessageViewModel(application: Application): AndroidViewModel(application) {
    private val repository = MessageRepository(application)
    private val _users = MutableLiveData<List<UserModel>>()
    val users: LiveData<List<UserModel>> get() = _users
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchUsers() {
        repository.getUsers(
            onSuccess = { userList ->
                _users.value = userList
            },
            onFailure = { databaseError ->
                _error.value = databaseError.message
            }
        )
    }

    fun fetchPresence(status : String){
        repository.fetchPresence(status)
    }
}