package hoanglong180903.myproject.socialhub.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import hoanglong180903.myproject.socialhub.adapter.FavoriteAdapter
import hoanglong180903.myproject.socialhub.databinding.ActivityFavoriteBinding
import hoanglong180903.myproject.socialhub.listener.FavoriteClickItemListener
import hoanglong180903.myproject.socialhub.model.Favorite
import hoanglong180903.myproject.socialhub.model.Track
import hoanglong180903.myproject.socialhub.service.PlayMusicService
import hoanglong180903.myproject.socialhub.viewmodel.FavoriteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteActivity : AppCompatActivity(), FavoriteClickItemListener {
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var binding : ActivityFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private var favoriteList= ArrayList<Favorite>()
    private var isMediaPlayerServiceRunning = false // false = pause
    private var currentTrackId: Int? = null
    private var mSong: Track? = null
    private var isPlaying = false
    //register broadcast
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val bundle = intent.extras ?: return
            mSong = bundle.get("object_song") as Track?
            isPlaying = bundle.getBoolean("status_player")
            val action = bundle.getInt("action_music")
            handlePlayingMusic(action)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initVariable()
        getData()
    }

    private fun init(){
        favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class]
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(receiver, IntentFilter("send_data_to_activity"))
    }

    private fun initVariable(){
        favoriteAdapter = FavoriteAdapter(this,this)
        binding.favoriteRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            adapter = favoriteAdapter
        }
    }

    private fun getData(){
        CoroutineScope(Dispatchers.IO).launch {
            val getAllFavorite = favoriteViewModel.getAllFavorite
            withContext(Dispatchers.Main){
                getAllFavorite.observe(this@FavoriteActivity, Observer { it ->
//                    favoriteList.addAll(it)
//                    favoriteAdapter.notifyDataSetChanged()
                    favoriteAdapter.submitList(it)
                })
            }
        }
    }

    override fun onItemClickDelete(favorite: Favorite,position : Int) {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteViewModel.deleteDisFavorite(favorite)
            withContext(Dispatchers.Main) {
                favoriteList.remove(favorite)
//                favoriteAdapter.notifyItemRangeChanged(position, favoriteAdapter.itemCount)
//                favoriteAdapter.notifyItemRemoved(position)
                favoriteAdapter.notifyDataSetChanged()
                Toast.makeText(this@FavoriteActivity, "Successfully delete!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onItemClickPlayAndPause(favorite: Favorite) {
        if (isMediaPlayerServiceRunning && currentTrackId == favorite.id) {
            stopMusicService()
        } else {
            if (isMediaPlayerServiceRunning) {
                stopMusicService()
            }
            playMusic(favorite)
            isMediaPlayerServiceRunning = true
            currentTrackId = favorite.id
        }
    }

    private fun playMusic(favorite: Favorite) {
        val song: Track = Track(
            favorite.id,
            favorite.song,
            favorite.preview,
            favorite.singer,
            favorite.image
        )
        val intent = Intent(this, PlayMusicService::class.java)
        //gửi 1 object
        val bundle = Bundle()
        bundle.putSerializable("objectSong", song)
        intent.putExtras(bundle)
        startService(intent)
    }

    private fun stopMusicService() {
        isMediaPlayerServiceRunning = false
        val intent = Intent(this, PlayMusicService::class.java)
        stopService(intent)
    }

    private fun handlePlayingMusic(action: Int) {
        when (action) {
            PlayMusicService.ACTION_START -> {
                setPlayingState(true)
            }

            PlayMusicService.ACTION_PAUSE -> {
                setPlayingState(false)
            }

            PlayMusicService.ACTION_RESUME -> {
                setPlayingState(true)

            }

            PlayMusicService.ACTION_CANCEL -> {
                setPlayingState(false)
            }
        }
    }


    private fun setPlayingState(isPlaying: Boolean) {
        this.isPlaying = isPlaying
        favoriteAdapter.setPlayingState(isPlaying)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }
}