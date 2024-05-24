package hoanglong180903.myproject.socialhub.repository

import android.app.Application
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import hoanglong180903.myproject.socialhub.model.UserModel

class ChatFragmentRepository(val application: Application) {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("Users")
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    fun getUsers(onResult: (List<UserModel>) -> Unit) {
        usersCollection.get()
            .addOnSuccessListener { result ->
                val users = result.map { document -> document.toObject(UserModel::class.java) }
                onResult(users)
            }
            .addOnFailureListener { exception ->
                Log.w("UserRepository", "Error getting documents: ", exception)
                onResult(emptyList())
            }
    }

    fun fetchPresence(status : String){
        val currentId = FirebaseAuth.getInstance().uid
        if (currentId != null) {
            database.reference.child("Presence").child(currentId).setValue(status)
        }
    }
}