package hoanglong180903.myproject.socialhub.repository

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hoanglong180903.myproject.socialhub.model.Comment
import hoanglong180903.myproject.socialhub.model.Posts
import hoanglong180903.myproject.socialhub.model.UserModel
import java.util.Date

class DetailPostRepository(val application: Application) {
    private val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    private var ref : DatabaseReference  = FirebaseDatabase.getInstance().reference
    var processComment : Boolean = true
    var comments : String = ""
    private var posRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("Posts")
    fun sendComment(postIde : String , comment: String , uId : String , uName : String , uImage : String){
        val date = Date()
        val hashMap = HashMap<String , Any>()
        hashMap["cId"] = date.time
        hashMap["comment"] = comment
        hashMap["timestamp"] = date.time
        hashMap["uid"] = uId
        hashMap["uName"] = uName
        hashMap["uImage"] = uImage

        ref.child("Posts")
            .child(postIde)
            .child("Comments")
            .child(date.time.toString())
            .setValue(hashMap)
            .addOnSuccessListener {
                Log.d("Comment","Comments successfully")
            }
            .addOnFailureListener {
                Log.d("Comment","Comments failed")

            }
    }

    fun updateComment(postIde : String){
        database.reference.child("Posts")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("SuspiciousIndentation")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (processComment) {
                        comments = snapshot.child(postIde).child("pComments").value.toString()
                        posRef.child(postIde).child("pComments").setValue("" + (comments.toInt() + 1))
                        processComment = false
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun getComment(onSuccess: (List<Comment>) -> Unit, onFailure: (DatabaseError) -> Unit , postIde: String) {
        database.reference.child("Posts")
            .child(postIde)
            .child("Comments")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userStoriesList = mutableListOf<Comment>()
                        snapshot.children.forEach { child ->
                            val user = child.getValue(Comment::class.java)
                            user?.let { userStoriesList.add(it) }
                        }
                        onSuccess(userStoriesList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure(error)
                }
            })
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