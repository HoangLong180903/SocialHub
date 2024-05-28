package hoanglong180903.myproject.socialhub.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hoanglong180903.myproject.socialhub.model.MessageModel
import hoanglong180903.myproject.socialhub.model.UserModel
import hoanglong180903.myproject.socialhub.repository.ChatRepository

class ChatViewModel(application: Application): AndroidViewModel(application) {
    private val repository = ChatRepository(application)
    private val _users = MutableLiveData<List<MessageModel>>()
    val users: LiveData<List<MessageModel>> get() = _users
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error
    fun sendMessage(messageTxt : String ,senderUid : String ,date : Long , senderRoom : String , receiverRoom : String){
        repository.sendMessage(messageTxt, senderUid, date, senderRoom, receiverRoom)
    }

    fun fetchMessage(senderRoom: String){
        repository.fetchMessage(
            onSuccess = { userList ->
                _users.value = userList
            },
            onFailure = { databaseError ->
                _error.value = databaseError.message
            },
        senderRoom)
    }

    fun sendPhotoGallery(
        data: Intent, messageTxt: String,
        senderUid: String,
        senderRoom: String,
        receiverRoom: String
    ){
        repository.sendPhotoGallery(data,
            messageTxt,
            senderUid,
            senderRoom,
            receiverRoom)
    }
}