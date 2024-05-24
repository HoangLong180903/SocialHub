package hoanglong180903.myproject.socialhub.repository

import android.app.Application
import com.google.firebase.auth.FirebaseAuth

class UserRepository (val application: Application) {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun logOut(){
        auth.signOut()
    }


}