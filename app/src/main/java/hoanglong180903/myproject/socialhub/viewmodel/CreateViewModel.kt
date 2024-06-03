package hoanglong180903.myproject.socialhub.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hoanglong180903.myproject.socialhub.model.Posts
import hoanglong180903.myproject.socialhub.model.UserModel
import hoanglong180903.myproject.socialhub.repository.CreateRepository

class CreateViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CreateRepository(application)
    private val _users = MutableLiveData<List<Posts>>()
    val users: LiveData<List<Posts>> get() = _users
    private val _error = MutableLiveData<String>()

    private val _user = MutableLiveData<UserModel?>()
    val user: LiveData<UserModel?> get() = _user

    fun createPost(selectImage : Uri,
                   user: UserModel,
                   title : String){
        repository.createPosts(selectImage,user,title)
    }

    fun fetchUser(uid: String) {
        repository.getUserData(uid) { user ->
            _user.value = user
        }
    }
}