package hoanglong180903.myproject.socialhub.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.databinding.ItemReceiverBinding
import hoanglong180903.myproject.socialhub.databinding.ItemSenderBinding
import hoanglong180903.myproject.socialhub.model.MessageModel


class ChatAdapter(private val messageList: List<MessageModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ITEM_SENT = 1
    private val ITEM_RECEIVE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_SENT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sender, parent, false)
            SentViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_receiver, parent, false)
            ReceiverViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        return if (FirebaseAuth.getInstance().uid == message.senderId) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        if (holder is SentViewHolder) {
            holder.binding.itemSentTextview.text = message.messageText
            if (message.messageText == "Photo" || message.messageText
                == "camera"
            ) {
                holder.binding.itemSendPhotoImage.visibility = View.VISIBLE
                holder.binding.itemSentTextview.visibility = View.GONE
                holder.binding.linearLayout.setBackgroundResource(R.drawable.bg_edittext)
                Glide.with(holder.itemView.context).load(message.messageImageUrl)
                    .placeholder(R.drawable._144760)
                    .into(holder.binding.itemSendPhotoImage)
            }
        } else if (holder is ReceiverViewHolder) {
            holder.binding.itemReceiveTextview.text = message.messageText
            if (message.messageText == "Photo" || message.messageText
                == "camera"
            ) {
                holder.binding.itemReceivePhotoImage.visibility = View.VISIBLE
                holder.binding.itemReceiveTextview.visibility = View.GONE
                holder.binding.linearLayout2.setBackgroundResource(R.drawable.bg_edittext)
                Glide.with(holder.itemView.context).load(message.messageImageUrl)
                    .placeholder(R.drawable._144760)
                    .into(holder.binding.itemReceivePhotoImage)
            }
        }
    }

    override fun getItemCount(): Int = messageList.size

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemSenderBinding = ItemSenderBinding.bind(itemView)
    }

    class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemReceiverBinding = ItemReceiverBinding.bind(itemView)
    }
}