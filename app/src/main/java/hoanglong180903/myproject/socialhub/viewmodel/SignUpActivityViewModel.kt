package hoanglong180903.myproject.socialhub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import hoanglong180903.myproject.socialhub.repository.LoginActivityRepository
import hoanglong180903.myproject.socialhub.repository.SignUpActivityRepository

class SignUpActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SignUpActivityRepository(application)
    val isSuccessful : LiveData<Boolean>
    init {
        isSuccessful = repository.isSuccessful
    }
    fun requestSignUp(username: String , email : String , password: String) {
        repository.requestSignUp(username, email,password)
    }

}