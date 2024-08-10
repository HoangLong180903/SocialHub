package hoanglong180903.myproject.socialhub.view.fragment

import hoanglong180903.myproject.socialhub.adapter.TracksAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hoanglong180903.myproject.socialhub.viewmodelFactory.DeezerViewModelFactory
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.adapter.AlbumAdapter
import hoanglong180903.myproject.socialhub.broadcast.NetworkBroadcast
import hoanglong180903.myproject.socialhub.databinding.FragmentMusicBinding
import hoanglong180903.myproject.socialhub.listener.OnClickItemListener
import hoanglong180903.myproject.socialhub.model.Track
import hoanglong180903.myproject.socialhub.service.PlayMusicService
import hoanglong180903.myproject.socialhub.utils.Contacts
import hoanglong180903.myproject.socialhub.viewmodel.DeezerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//class MusicFragment : Fragment(), OnClickItemListener {
//    private lateinit var tracksAdapter: TracksAdapter
//    private lateinit var albumAdapter: AlbumAdapter
//    private var mSong: Track? = null
//    private var isPlaying = false
//    private var isMediaPlayerServiceRunning = false // false = pause
//    private var currentTrackId: Int? = null
//
//    //register broadcast
//    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent) {
//            val bundle = intent.extras ?: return
//            mSong = bundle.get("object_song") as Track?
//            isPlaying = bundle.getBoolean("status_player")
//            val action = bundle.getInt("action_music")
//            handlePlayingMusic(action)
//        }
//    }
//    private lateinit var connectivityLiveData: NetworkBroadcast
//
//    private val deezerViewModel: DeezerViewModel by lazy {
//        ViewModelProvider(
//            this,
//            DeezerViewModelFactory(requireActivity().application)
//        )[DeezerViewModel::class.java]
//    }
//    private lateinit var binding: FragmentMusicBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        binding = FragmentMusicBinding.inflate(layoutInflater, container, false)
//        val title = SpannableString("Music")
//        title.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, 0)
//        activity?.title = title
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initView()
//        initVariable()
//        checkNetworkConnection()
//    }
//
//
//    private fun initView() {
//        //Đăng ký broadcast
//        LocalBroadcastManager.getInstance(requireContext())
//            .registerReceiver(receiver, IntentFilter("send_data_to_activity"))
//    }
//
//
//    //setup recyclerview
//    private fun initVariable() {
//        tracksAdapter = TracksAdapter(requireContext(), this)
//        binding.recyclerView.adapter = tracksAdapter
//        binding.recyclerView.apply {
//            layoutManager = LinearLayoutManager(context)
//        }
//        albumAdapter = AlbumAdapter(requireContext())
//        binding.rcNewAlbum.adapter = albumAdapter
//        binding.rcNewAlbum.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        }
//        getData()
//    }
//
//    //get Data
//    private fun getData() {
//        binding.musicProgessBarSong.visibility = View.VISIBLE
//        CoroutineScope(Dispatchers.IO).launch {
//            deezerViewModel.fetchTracks(Contacts.URL_TRACK)
//            withContext(Dispatchers.Main) {
//                deezerViewModel.tracks.observe(requireActivity(), Observer { tracks ->
//                    tracks?.let {
//                        tracksAdapter.submitList(it)
//                        binding.musicProgessBarSong.visibility = View.GONE
//                    }
//                })
//            }
//        }
//        //get album
//        binding.musicProgessBarAlbum.visibility = View.VISIBLE
//        CoroutineScope(Dispatchers.IO).launch {
//            deezerViewModel.fetchAlbum(Contacts.URL_ALBUM)
//            withContext(Dispatchers.Main) {
//                deezerViewModel.albums.observe(requireActivity(), Observer { albums ->
//                    albums.let {
//                        albumAdapter.submitList(albums)
//                        binding.musicProgessBarAlbum.visibility = View.GONE
//                    }
//                })
//            }
//        }
//    }
//
//
//    override fun onItemClick(track: Track) {
//        if (isMediaPlayerServiceRunning && isPlaying && currentTrackId == track.id) {
//            stopMusicService()
//            binding.mainIcPlayAndPause.setImageResource(R.drawable.ic_play)
//            isPlaying = false
//            isMediaPlayerServiceRunning = false
//
//        } else {
//            if (isMediaPlayerServiceRunning && isPlaying) {
//                stopMusicService()
//                isPlaying = false
//                isMediaPlayerServiceRunning = false
//                binding.mainIcPlayAndPause.setImageResource(R.drawable.ic_play)
//            }
//            playMusic(track)
//            isMediaPlayerServiceRunning = true
//            isPlaying = true
//            currentTrackId = track.id
//            binding.mainIcPlayAndPause.setImageResource(R.drawable.ic_pause)
//        }
//        Log.e("music","onClick called ${isPlaying} \t ${isMediaPlayerServiceRunning}")
//    }
//
//    override fun onItemClickFavorite(track: Track) {
////        val favorite = Favorite(
////            0,
////            track.title,
////            track.artist.name,
////            track.preview,
////            track.artist.picture
////        )
////        favoriteViewModel.insertFavorite(favorite)
////        Toast.makeText(this@MainActivity, "Successfully added track!", Toast.LENGTH_LONG).show()
//    }
//
//    override fun onItemClickIntent(track: Track) {
////        val intent = Intent(this, NowPlayingActivity::class.java)
////        val bundle = Bundle()
////        bundle.putSerializable("objectIntent", track)
////        intent.putExtras(bundle)
////        startActivity(intent)
//////        val intent = Intent(this,NowPlayingActivity::class.java)
//////        startActivity(intent)
//    }
//
//    private fun playMusic(track: Track) {
//        val song: Track = Track(
//            track.id,
//            track.title,
//            track.link,
//            track.duration,
//            track.preview,
//            track.artist,
//        )
//        val intent = Intent(context, PlayMusicService::class.java)
//        //gửi 1 object
//        val bundle = Bundle()
//        bundle.putSerializable("objectSong", song)
//        intent.putExtras(bundle)
//        requireActivity().startService(intent)
//    }
//
//    private fun stopMusicService() {
//        sendActionService(PlayMusicService.ACTION_PAUSE)
//        isPlaying = false
//        isMediaPlayerServiceRunning = false
//    }
//
//
//    private fun handlePlayingMusic(action: Int) {
//        when (action) {
//            PlayMusicService.ACTION_START -> {
//                binding.lnBottom.visibility = View.VISIBLE
//                showInfoLayoutBottom()
//                setPlayingState(true)
//                setStatusBottomPlayOrPause()
//            }
//
//            PlayMusicService.ACTION_PAUSE -> {
//                setStatusBottomPlayOrPause()
//                setPlayingState(false)
//            }
//
//            PlayMusicService.ACTION_RESUME -> {
//                setStatusBottomPlayOrPause()
//                setPlayingState(true)
//
//            }
//
//            PlayMusicService.ACTION_CANCEL -> {
////                binding.lnBottom.visibility = View.GONE
//                setPlayingState(false)
//            }
//        }
//    }
//
//    private fun showInfoLayoutBottom() {
//        if (mSong == null) {
//            return
//        }
//        Glide.with(this).load(mSong!!.artist.picture).into(binding.mainLnImageView)
//        binding.mainLnTvSong.text = mSong!!.title
//        binding.mainLnTvSinger.text = mSong!!.artist.name
//        binding.mainIcPlayAndPause.setOnClickListener(View.OnClickListener {
//            if (isMediaPlayerServiceRunning && isPlaying) {
//                sendActionService(PlayMusicService.ACTION_PAUSE)
//                setPlayingState(false)
//            } else {
//                sendActionService(PlayMusicService.ACTION_RESUME)
//                setPlayingState(true)
//            }
//        })
//    }
//
//    private fun setStatusBottomPlayOrPause() {
//        if (isPlaying) {
//            binding.mainIcPlayAndPause.setImageResource(R.drawable.ic_pause)
//        } else {
//            binding.mainIcPlayAndPause.setImageResource(R.drawable.ic_play)
//        }
//    }
//
//    private fun setPlayingState(isPlaying: Boolean) {
//        this.isPlaying = isPlaying
//        tracksAdapter.setPlayingState(isPlaying)
//    }
//
//    private fun sendActionService(action: Int) {
//        val mIntent = Intent(context, PlayMusicService::class.java)
//        mIntent.putExtra("action_music_service", action)
//        requireActivity().startService(mIntent)
//    }
//
//    //check 17:00 - 30/7/2024
//    private fun checkNetworkConnection() {
//        connectivityLiveData = NetworkBroadcast(requireActivity().application)
//        connectivityLiveData.observe(requireActivity(), Observer { isAvailable ->
//            when (isAvailable) {
//                false -> {
//                    requireActivity().runOnUiThread(Runnable {
//                        Thread.sleep(2000)
//                        sendActionService(PlayMusicService.ACTION_PAUSE)
//                        Toast.makeText(context, "Không có kết nối mạng", Toast.LENGTH_SHORT).show()
//                    })
//                }
//
//                true -> {
//                    requireActivity().runOnUiThread(Runnable {
//                        Thread.sleep(2000)
//                        sendActionService(PlayMusicService.ACTION_RESUME)
//                    })
//                }
//            }
//        })
//    }
//    override fun onResume() {
//        if (isMediaPlayerServiceRunning == false && isPlaying == false) {
//            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
//            sendActionService(PlayMusicService.ACTION_PAUSE)
//            isMediaPlayerServiceRunning = false
//            isPlaying = false;
//        } else {
//            sendActionService(PlayMusicService.ACTION_RESUME)
//            isMediaPlayerServiceRunning = true
//            isPlaying = true;
//        }
//        Log.e("music","onDestroy called ${isPlaying} \t ${isMediaPlayerServiceRunning}")
//        super.onResume()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
////        if (isMediaPlayerServiceRunning == false && isPlaying == false) {
////            sendActionService(PlayMusicService.ACTION_PAUSE)
//////            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
////            isMediaPlayerServiceRunning = false
////            isPlaying = false;
////        } else {
////            sendActionService(PlayMusicService.ACTION_RESUME)
////            isMediaPlayerServiceRunning = true
////            isPlaying = true;
////        }
//        Log.e("music","onDestroy called ${isPlaying} \t ${isMediaPlayerServiceRunning}")
//    }
//    override fun onPause() {
//        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
//        super.onPause()
//        Log.e("music","onPause called")
//    }
//
//    override fun onStop() {
//        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
//        super.onStop()
//        Log.e("music","onstop called")
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
//
////        if (isMediaPlayerServiceRunning == false && isPlaying == false) {
//////            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
////            sendActionService(PlayMusicService.ACTION_PAUSE)
////            isMediaPlayerServiceRunning = false
////            isPlaying = false;
////        } else {
////            sendActionService(PlayMusicService.ACTION_RESUME)
////            isMediaPlayerServiceRunning = true
////            isPlaying = true;
////        }
//        Log.e("music","onDestroyView called ${isPlaying} \t ${isMediaPlayerServiceRunning}")
//    }
//
//    override fun onStart() {
//        super.onStart()
//        if (isMediaPlayerServiceRunning == false && isPlaying == false) {
//            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
//            sendActionService(PlayMusicService.ACTION_PAUSE)
//            isMediaPlayerServiceRunning = false
//            isPlaying = false;
//        } else {
//            sendActionService(PlayMusicService.ACTION_RESUME)
//            isMediaPlayerServiceRunning = true
//            isPlaying = true;
//        }
//        Log.e("music","onStart called ${isPlaying} \t ${isMediaPlayerServiceRunning}")
//    }
//}

class MusicFragment : Fragment(), OnClickItemListener {
    private lateinit var tracksAdapter: TracksAdapter
    private lateinit var albumAdapter: AlbumAdapter
    private var mSong: Track? = null
    private var isPlaying = false
    private var isMediaPlayerServiceRunning = false // false = pause
    private var currentTrackId: Int? = null

    private var isReceiverRegistered = false

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
    private lateinit var connectivityLiveData: NetworkBroadcast

    private val deezerViewModel: DeezerViewModel by lazy {
        ViewModelProvider(
            this,
            DeezerViewModelFactory(requireActivity().application)
        )[DeezerViewModel::class.java]
    }
    private lateinit var binding: FragmentMusicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMusicBinding.inflate(layoutInflater, container, false)
        val title = SpannableString("Music")
        title.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, 0)
        activity?.title = title
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initVariable()
        checkNetworkConnection()
    }

    private fun initView() {
        // Initializing views, but not registering the receiver here anymore
    }

    //setup recyclerview
    private fun initVariable() {
        tracksAdapter = TracksAdapter(requireContext(), this)
        binding.recyclerView.adapter = tracksAdapter
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
        }
        albumAdapter = AlbumAdapter(requireContext())
        binding.rcNewAlbum.adapter = albumAdapter
        binding.rcNewAlbum.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        getData()
    }

    //get Data
    private fun getData() {
        binding.musicProgessBarSong.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            deezerViewModel.fetchTracks(Contacts.URL_TRACK)
            withContext(Dispatchers.Main) {
                deezerViewModel.tracks.observe(requireActivity(), Observer { tracks ->
                    tracks?.let {
                        tracksAdapter.submitList(it)
                        binding.musicProgessBarSong.visibility = View.GONE
                    }
                })
            }
        }
        //get album
        binding.musicProgessBarAlbum.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            deezerViewModel.fetchAlbum(Contacts.URL_ALBUM)
            withContext(Dispatchers.Main) {
                deezerViewModel.albums.observe(requireActivity(), Observer { albums ->
                    albums.let {
                        albumAdapter.submitList(albums)
                        binding.musicProgessBarAlbum.visibility = View.GONE
                    }
                })
            }
        }
    }

    override fun onItemClick(track: Track) {
        if (isMediaPlayerServiceRunning && isPlaying && currentTrackId == track.id) {
            stopMusicService()
            binding.mainIcPlayAndPause.setImageResource(R.drawable.ic_play)
            isPlaying = false
            isMediaPlayerServiceRunning = false

        } else {
            if (isMediaPlayerServiceRunning && isPlaying) {
                stopMusicService()
                isPlaying = false
                isMediaPlayerServiceRunning = false
                binding.mainIcPlayAndPause.setImageResource(R.drawable.ic_play)
            }
            playMusic(track)
            isMediaPlayerServiceRunning = true
            isPlaying = true
            currentTrackId = track.id
            binding.mainIcPlayAndPause.setImageResource(R.drawable.ic_pause)
        }
        Log.e("music", "onClick called ${isPlaying} \t ${isMediaPlayerServiceRunning}")
    }

    override fun onItemClickFavorite(track: Track) {
        // Implementation for favorite click
    }

    override fun onItemClickIntent(track: Track) {
        // Implementation for intent click
    }

    private fun playMusic(track: Track) {
        val song: Track = Track(
            track.id,
            track.title,
            track.link,
            track.duration,
            track.preview,
            track.artist,
        )
        val intent = Intent(context, PlayMusicService::class.java)
        val bundle = Bundle()
        bundle.putSerializable("objectSong", song)
        intent.putExtras(bundle)
        requireActivity().startService(intent)
    }

    private fun stopMusicService() {
        sendActionService(PlayMusicService.ACTION_PAUSE)
        isPlaying = false
        isMediaPlayerServiceRunning = false
    }

    private fun handlePlayingMusic(action: Int) {
        when (action) {
            PlayMusicService.ACTION_START -> {
                binding.lnBottom.visibility = View.VISIBLE
                showInfoLayoutBottom()
                setPlayingState(true)
                setStatusBottomPlayOrPause()
            }
            PlayMusicService.ACTION_PAUSE -> {
                setStatusBottomPlayOrPause()
                setPlayingState(false)
            }
            PlayMusicService.ACTION_RESUME -> {
                setStatusBottomPlayOrPause()
                setPlayingState(true)
            }
            PlayMusicService.ACTION_CANCEL -> {
                setPlayingState(false)
            }
        }
    }

    private fun showInfoLayoutBottom() {
        if (mSong == null) {
            return
        }
        Glide.with(this).load(mSong!!.artist.picture).into(binding.mainLnImageView)
        binding.mainLnTvSong.text = mSong!!.title
        binding.mainLnTvSinger.text = mSong!!.artist.name
        binding.mainIcPlayAndPause.setOnClickListener {
            if (isMediaPlayerServiceRunning && isPlaying) {
                sendActionService(PlayMusicService.ACTION_PAUSE)
                setPlayingState(false)
            } else {
                sendActionService(PlayMusicService.ACTION_RESUME)
                setPlayingState(true)
            }
        }
    }

    private fun setStatusBottomPlayOrPause() {
        if (isPlaying) {
            binding.mainIcPlayAndPause.setImageResource(R.drawable.ic_pause)
        } else {
            binding.mainIcPlayAndPause.setImageResource(R.drawable.ic_play)
        }
    }

    private fun setPlayingState(isPlaying: Boolean) {
        this.isPlaying = isPlaying
        tracksAdapter.setPlayingState(isPlaying)
    }

    private fun sendActionService(action: Int) {
        val mIntent = Intent(context, PlayMusicService::class.java)
        mIntent.putExtra("action_music_service", action)
        requireActivity().startService(mIntent)
    }

    private fun checkNetworkConnection() {
        connectivityLiveData = NetworkBroadcast(requireActivity().application)
        connectivityLiveData.observe(requireActivity(), Observer { isAvailable ->
            when (isAvailable) {
                false -> {
                    requireActivity().runOnUiThread {
                        Thread.sleep(2000)
                        sendActionService(PlayMusicService.ACTION_PAUSE)
                        Toast.makeText(context, "Không có kết nối mạng", Toast.LENGTH_SHORT).show()
                    }
                }
                true -> {
                    requireActivity().runOnUiThread {
                        Thread.sleep(2000)
                        sendActionService(PlayMusicService.ACTION_RESUME)
                    }
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(receiver, IntentFilter("send_data_to_activity"))
            isReceiverRegistered = true
        }
    }

    override fun onStop() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
            isReceiverRegistered = false
        }
        super.onStop()
    }

    override fun onDestroyView() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
            isReceiverRegistered = false
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
            isReceiverRegistered = false
        }
        super.onDestroy()
    }
}
