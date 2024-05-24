package hoanglong180903.myproject.socialhub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import hoanglong180903.myproject.socialhub.repository.SignUpRepository
import hoanglong180903.myproject.socialhub.repository.UserRepository

class UserViewModel (application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(application)
    fun logOut(){
        repository.logOut()
    }
}