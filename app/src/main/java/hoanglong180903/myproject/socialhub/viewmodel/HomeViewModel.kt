package hoanglong180903.myproject.socialhub.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hoanglong180903.myproject.socialhub.model.UserModel
import hoanglong180903.myproject.socialhub.model.UserStories
import hoanglong180903.myproject.socialhub.repository.HomeRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HomeRepository(application)
    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> get() = _user

    private val _users = MutableLiveData<List<UserStories>>()
    val users: LiveData<List<UserStories>> get() = _users
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchUser(uid: String) {
        repository.getUserData(uid) { user ->
            _user.value = user
        }
    }

    fun addUserStories(
        data: Intent,
        user: UserModel
    ){
        repository.addUserStories(data,user)
    }

    fun getUserStories(){
        repository.getUserStories(
            onSuccess = { userList ->
                _users.value = userList
            },
            onFailure = { databaseError ->
                _error.value = databaseError.message
            }
        )
    }
}