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
import hoanglong180903.myproject.socialhub.listener.OnClickItemListener
import hoanglong180903.myproject.socialhub.model.Track

class TracksAdapter(private var context:Context, private val listener: OnClickItemListener) :
    RecyclerView.Adapter<TracksAdapter.TrackViewHolder>() {

    private var tracks: List<Track> = listOf()
    private var isMediaPlayerServiceRunning = false // false = pause
    private var currentTrackId: Int? = null
    private var previousViewHolder: TrackViewHolder? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val currentTrack = tracks[position]
        holder.trackTitle.text = currentTrack.title
        holder.trackArtist.text = currentTrack.artist.name
        holder.preview.text = currentTrack.preview
        Glide.with(holder.itemView.context).load(currentTrack.artist.picture)
            .into(holder.trackImage)
        // Set the correct icon based on the current track
        if (isMediaPlayerServiceRunning && currentTrackId == currentTrack.id) {
            holder.imgStart.setImageResource(R.drawable.ic_pause)
        } else {
            holder.imgStart.setImageResource(R.drawable.ic_play)
        }
        holder.imgStart.setOnClickListener {
            listener.onItemClick(currentTrack)
            if (isMediaPlayerServiceRunning && currentTrackId == currentTrack.id) {
                holder.imgStart.setImageResource(R.drawable.ic_play)
                isMediaPlayerServiceRunning = false
                currentTrackId = null
                previousViewHolder = null
            } else {
                if (isMediaPlayerServiceRunning && previousViewHolder != null) {
                    previousViewHolder?.imgStart?.setImageResource(R.drawable.ic_play)
                }
                holder.imgStart.setImageResource(R.drawable.ic_pause)
                isMediaPlayerServiceRunning = true
                currentTrackId = currentTrack.id
                previousViewHolder = holder
            }
        }
        holder.imgFavorite.setOnClickListener {
            listener.onItemClickFavorite(currentTrack)
        }
        holder.itemView.setOnClickListener {
            listener.onItemClickIntent(currentTrack)
        }
    }

    override fun getItemCount() = tracks.size

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackTitle: TextView = itemView.findViewById(R.id.trackTitle)
        val trackArtist: TextView = itemView.findViewById(R.id.trackArtist)
        val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
        val preview: TextView = itemView.findViewById(R.id.preview)
        val imgStart: ImageView = itemView.findViewById(R.id.item_songList_imgPlay)
        val imgFavorite: ImageView = itemView.findViewById(R.id.item_songList_imgFavorite)
    }

    fun submitList(trackList: List<Track>) {
        tracks = trackList
        notifyDataSetChanged()
    }

    fun setPlayingState(isPlaying: Boolean) {
        this.isMediaPlayerServiceRunning = isPlaying
        notifyDataSetChanged()
    }
}
