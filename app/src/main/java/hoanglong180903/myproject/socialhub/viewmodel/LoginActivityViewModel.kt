package hoanglong180903.myproject.socialhub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import hoanglong180903.myproject.socialhub.repository.LoginActivityRepository

class LoginActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LoginActivityRepository(application)
    val isSuccessful : LiveData<Boolean>
    init {
        isSuccessful = repository.isSuccessful
    }
    //request login in firebase
    fun requestLogin(email : String , password: String) {
        repository.requestLogin(email,password)
    }
    fun intentRegister(){
        repository.intentRegister()
    }
}