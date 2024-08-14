package hoanglong180903.myproject.socialhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.interfaces.ItemMessageOnClick
import hoanglong180903.myproject.socialhub.model.UserModel
import java.text.SimpleDateFormat
import java.util.Date

class MessageAdapter (private val users: List<UserModel>,
                      private val listener: ItemMessageOnClick
                      ) : RecyclerView.Adapter<MessageAdapter.UserViewHolder>() {
    private var senderId : String = "";
    var senderRoom : String = "";
    var database: FirebaseDatabase? = null
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageProfile : ImageView = itemView.findViewById(R.id.item_history_message_image)
        val tvName : TextView = itemView.findViewById(R.id.item_history_message_tvName)
        val tvTime : TextView = itemView.findViewById(R.id.item_history_message_tvTime)
        val tvMessage : TextView = itemView.findViewById(R.id.item_history_message_tvMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_message, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = users[position]
        senderId = FirebaseAuth.getInstance().uid!!
        senderRoom = senderId + item.id
        database = FirebaseDatabase.getInstance()
        FirebaseDatabase.getInstance().reference
            .child("Chats")
            .child(senderRoom)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val lastMsg = snapshot.child("lastMsg").getValue(String::class.java)
                        val time = snapshot.child("lastMsgTime").getValue(Long::class.java)!!
                        val dateFormat = SimpleDateFormat("hh:mm a")
                        holder.tvTime.text = dateFormat.format(Date(time))
                        holder.tvMessage.text = lastMsg
                    } else {
                        holder.tvMessage.text = "Tap to chat"
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        holder.tvName.text = item.name
        Glide.with(holder.itemView.context).load(item.image)
            .placeholder(R.drawable._144760)
            .into(holder.imageProfile)
        holder.itemView.setOnClickListener {
            listener.onClickItemToChat(item)
        }
    }

    override fun getItemCount() = users.size

}