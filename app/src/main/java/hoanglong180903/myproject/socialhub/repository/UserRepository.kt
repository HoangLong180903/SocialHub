package hoanglong180903.myproject.socialhub.repository

import android.annotation.SuppressLint
import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hoanglong180903.myproject.socialhub.model.UserModel

class UserRepository (val application: Application) {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    fun logOut(){
        auth.signOut()
    }

    fun getUserData(uid: String, callback: (UserModel?) -> Unit) {
        database.getReference("users").child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                callback(user)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }
}