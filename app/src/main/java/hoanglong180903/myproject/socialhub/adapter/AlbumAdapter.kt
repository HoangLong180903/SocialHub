package hoanglong180903.myproject.socialhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.model.Album

class AlbumAdapter(private var context:Context) :
    RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    private var albums: List<Album> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_new_album, parent, false)
        return AlbumViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val item = albums[position]
        Glide.with(holder.itemView.context).load(item.picture)
            .into(holder.imageAlbum)
        holder.nameAlbum.text = item.title
        holder.singerAlbum.text = item.creator.name
    }

    override fun getItemCount() = albums.size

    class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageAlbum: ImageView = itemView.findViewById(R.id.item_album_imageAlbum)
        val nameAlbum: TextView = itemView.findViewById(R.id.item_album_tvNameAlbum)
        val singerAlbum : TextView = itemView.findViewById(R.id.item_album_tvSingerAlbum)
    }

    fun submitList(albums: List<Album>) {
        this.albums = albums
        notifyDataSetChanged()
    }
}
