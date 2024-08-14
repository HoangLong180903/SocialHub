package hoanglong180903.myproject.socialhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.listener.FavoriteClickItemListener
import hoanglong180903.myproject.socialhub.model.Favorite

class FavoriteAdapter(private var context: Context, private val listener: FavoriteClickItemListener) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private var favorites: List<Favorite> = listOf()
    private var isMediaPlayerServiceRunning = false // false = pause
    private var currentTrackId: Int? = null
    private var previousViewHolder: FavoriteViewHolder? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = favorites[position]
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_in_left)
        holder.itemView.startAnimation(animation)
        holder.favoriteTitle.text = item.song
        holder.favoriteArtist.text = item.singer
//        holder.favoritePreview.text = "${item.id}"

        Glide.with(holder.itemView.context).load(item.image)
            .into(holder.favoriteImage)
        holder.favoriteDeleteImage.setOnClickListener {
            listener.onItemClickDelete(item,position)
        }
        if (isMediaPlayerServiceRunning && currentTrackId == item.id) {
            holder.favoritePlayPauseImage.setImageResource(R.drawable.ic_pause)
        } else {
            holder.favoritePlayPauseImage.setImageResource(R.drawable.ic_play)
        }
//        holder.favoritePlayPauseImage.setOnClickListener {
//            listener.onItemClickPlayAndPause(item)
//        }

        holder.favoritePlayPauseImage.setOnClickListener {
            listener.onItemClickPlayAndPause(item)
            if (isMediaPlayerServiceRunning && currentTrackId == item.id) {
                holder.favoritePlayPauseImage.setImageResource(R.drawable.ic_play)
                isMediaPlayerServiceRunning = false
                currentTrackId = null
                previousViewHolder = null
            } else {
                if (isMediaPlayerServiceRunning && previousViewHolder != null) {
                    previousViewHolder?.favoritePlayPauseImage?.setImageResource(R.drawable.ic_play)
                }
                holder.favoritePlayPauseImage.setImageResource(R.drawable.ic_pause)
                isMediaPlayerServiceRunning = true
                currentTrackId = item.id
                previousViewHolder = holder
            }
        }

    }

    override fun getItemCount() : Int { return favorites.size }

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val favoriteTitle: TextView = itemView.findViewById(R.id.favoriteTitle)
        val favoriteArtist: TextView = itemView.findViewById(R.id.favoriteArtist)
        val favoriteImage: ImageView = itemView.findViewById(R.id.favoriteImage)
        val favoriteDeleteImage: ImageView = itemView.findViewById(R.id.item_favorite_imgFavorite)
        val favoritePlayPauseImage : ImageView = itemView.findViewById(R.id.item_favorite_imgPlay)
        val favoritePreview : TextView = itemView.findViewById(R.id.preview)
    }

    fun submitList(favoriteList: List<Favorite>) {
        favorites = favoriteList
        notifyDataSetChanged()
    }

    fun setPlayingState(isPlaying: Boolean) {
        this.isMediaPlayerServiceRunning = isPlaying
        notifyDataSetChanged()
    }

}
