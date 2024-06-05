package hoanglong180903.myproject.socialhub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hoanglong180903.myproject.socialhub.model.Comment
import hoanglong180903.myproject.socialhub.model.Posts
import hoanglong180903.myproject.socialhub.model.UserModel
import hoanglong180903.myproject.socialhub.repository.DetailPostRepository

class DetailPostViewModel(application: Application)  : AndroidViewModel(application) {
    private var repository = DetailPostRepository(application)
    private val _users = MutableLiveData<List<Comment>>()
    val users: LiveData<List<Comment>> get() = _users
    private val _error = MutableLiveData<String>()


    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> get() = _user
    fun sendComment(postIde : String , comment: String , uId : String , uName : String , uImage : String) {
        repository.sendComment(postIde, comment, uId, uName , uImage)
    }

    fun updateComment(postIde : String) {
        repository.updateComment(postIde)
    }

    fun getComment(postIde : String) {
        repository.getComment(
            onSuccess = { userList ->
                _users.value = userList
            },
            onFailure = { databaseError ->
                _error.value = databaseError.message
            },postIde
        )
    }

    fun fetchUser(uid: String) {
        repository.getUserData(uid) { user ->
            _user.value = user
        }
    }
}