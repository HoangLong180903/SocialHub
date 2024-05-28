package hoanglong180903.myproject.socialhub.repository

import android.annotation.SuppressLint
import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hoanglong180903.myproject.socialhub.model.UserModel

class MessageRepository(val application: Application) {
    private val database = FirebaseDatabase.getInstance()
    fun getUsers(onSuccess: (List<UserModel>) -> Unit, onFailure: (DatabaseError) -> Unit) {
        database.getReference("users").addValueEventListener(object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<UserModel>()
                snapshot.children.forEach { child ->
                    val user = child.getValue(UserModel::class.java)
                    if (!user?.id.equals(FirebaseAuth.getInstance().uid))
                    user?.let { users.add(it) }
                }
                onSuccess(users)
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error)
            }
        })
    }
    fun fetchPresence(status : String){
        val currentId = FirebaseAuth.getInstance().uid
        if (currentId != null) {
            database.reference.child("Presence").child(currentId).setValue(status)
        }
    }
}