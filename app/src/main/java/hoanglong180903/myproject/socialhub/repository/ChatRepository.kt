package hoanglong180903.myproject.socialhub.repository

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import hoanglong180903.myproject.socialhub.model.MessageModel
import java.io.ByteArrayOutputStream
import java.util.Calendar
import java.util.Date

class ChatRepository(val application: Application) {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    fun sendMessage(
        messageTxt: String,
        senderUid: String,
        date: Long,
        senderRoom: String,
        receiverRoom: String
    ) {
        val message = MessageModel(
            messageText = messageTxt,
            senderId = senderUid,
            timestamp = date
        )
        val randomKey: String = database.reference.push().key!!
        val lastMgsObj = HashMap<String, Any>()
        lastMgsObj["lastMsg"] = message.messageText
        lastMgsObj["lastMsgTime"] = date
        database.reference.child("Chats").child(senderRoom).updateChildren(lastMgsObj)
        database.reference.child("Chats").child(receiverRoom).updateChildren(lastMgsObj)
        database.reference.child("Chats")
            .child(senderRoom)
            .child("Messages")
            .child(randomKey)
            .setValue(message)
            .addOnSuccessListener {
                database.reference.child("Chats")
                    .child(receiverRoom)
                    .child("Messages")
                    .child(randomKey)
                    .setValue(message)
                    .addOnSuccessListener {
                        Log.d("sendMessage", "gui tin nhan thanh cong")
                    }
            }
            .addOnFailureListener {
                Log.d("sendMessage", "gui tin nhan khong thanh cong")
            }
    }

    fun fetchMessage(
        onSuccess: (List<MessageModel>) -> Unit,
        onFailure: (DatabaseError) -> Unit,
        senderRoom: String
    ) {
        database.getReference("Chats")
            .child(senderRoom)
            .child("Messages").addValueEventListener(object : ValueEventListener {
                @SuppressLint("SuspiciousIndentation")
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messageList = mutableListOf<MessageModel>()
                    snapshot.children.forEach { child ->
                        val user = child.getValue(MessageModel::class.java)
                        user?.let { messageList.add(it) }
                    }
                    onSuccess(messageList)
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure(error)
                }
            })
    }

    fun sendPhotoGallery(
        data: Intent,
        messageTxt: String,
        senderUid: String,
        senderRoom: String,
        receiverRoom: String
    ) {
        val selectedImage: Uri = data.data!!
        val calendar = Calendar.getInstance()
        val reference: StorageReference = storage.reference.child("Chats").child(calendar.timeInMillis.toString() + "")
        reference.putFile(selectedImage).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                reference.downloadUrl.addOnSuccessListener { uri ->
                    val filePath = uri.toString()
                    val messageTxt: String = messageTxt
                    val date = Date()
                    val message = MessageModel(
                        messageText = messageTxt,
                        senderId = senderUid,
                        messageImageUrl = filePath,
                        timestamp = date.time
                    )
                    val randomKey = database.reference.push().key
                    val lastMgsObj = HashMap<String, Any>()
                    lastMgsObj["lastMsg"] = "You just send an image"
                    lastMgsObj["lastMsgTime"] = date.time
                    database.reference.child("Chats").child(senderRoom).updateChildren(lastMgsObj)
                    database.reference.child("Chats").child(receiverRoom).updateChildren(lastMgsObj)
                    database.reference.child("Chats")
                        .child(senderRoom)
                        .child("Messages")
                        .child(randomKey!!)
                        .setValue(message)
                        .addOnSuccessListener {
                            database.reference.child("Chats")
                                .child(receiverRoom)
                                .child("Messages")
                                .child(randomKey)
                                .setValue(message)
                                .addOnSuccessListener {
                                    Log.d("send photo", "send photo successful")
                                }
                                .addOnFailureListener {
                                    Log.d("send photo", it.localizedMessage)
                                }
                        }
                }
            }
        }
    }

//    fun sendCameraGallery(){
//        checkSelfPermission()
//        val extras: Bundle = data.getExtras()
//        imageBitmap = extras["data"] as Bitmap?
//        uploadImageToFirebaseStorage()
//    }
//
//    private fun checkSelfPermission() {
//        if (ContextCompat.checkSelfPermission(this@ChatActivity, Manifest.permission.CAMERA)
//            != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this@ChatActivity, arrayOf<String>(
//                    Manifest.permission.CAMERA
//                ), 100
//            )
//        }
//    }
//
//    private fun uploadImageToFirebaseStorage() {
//        val calendar = Calendar.getInstance()
//        val reference =
//            storage.reference.child("Chats").child(calendar.timeInMillis.toString() + "")
//        val outputStream = ByteArrayOutputStream()
//        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//        val data = outputStream.toByteArray()
//        reference.putBytes(data).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                reference.downloadUrl.addOnSuccessListener { uri ->
//                    val filePath = uri.toString()
//                    val messageTxt: String = binding.edChating.getText().toString()
//                    val date = Date()
//                    val message = Message(messageTxt, senderUid, date.time)
//                    message.setMessageText("camera")
//                    message.setMessageImageUrl(filePath)
//                    binding.edChating.setText("")
//
//                    //lấy mã gửi tin nhắn
//                    val randomKey = database.reference.push().key
//
//                    //set thời gian gửi , tin nhắn gửi mới nhất
//                    val lastMgsObj = java.util.HashMap<String, Any>()
//                    lastMgsObj["lastMsg"] = message.getMessageText()
//                    lastMgsObj["lastMsgTime"] = date.time
//                    database.reference.child("Chats").child(senderRoom).updateChildren(lastMgsObj)
//                    database.reference.child("Chats").child(receiverRoom).updateChildren(lastMgsObj)
//                    database.reference.child("Chats")
//                        .child(senderRoom)
//                        .child("Messages")
//                        .child(randomKey!!)
//                        .setValue(message)
//                        .addOnSuccessListener {
//                            database.reference.child("Chats")
//                                .child(receiverRoom)
//                                .child("Messages")
//                                .child(randomKey)
//                                .setValue(message)
//                                .addOnSuccessListener { }
//                        }
//                }
//            }
//        }
//    }
}