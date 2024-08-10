package hoanglong180903.myproject.socialhub.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import hoanglong180903.myproject.socialhub.R
import hoanglong180903.myproject.socialhub.application.MyApplication
import hoanglong180903.myproject.socialhub.broadcast.PlayMusicBroadcast
import hoanglong180903.myproject.socialhub.model.Track


class PlayMusicService : Service() {
    private val myBinder = MyBinder()
    private var isPlaying : Boolean = false
    private var mTracks: Track? = null
    companion object{
        const val ACTION_PAUSE: Int = 1 //biến tạm dùng
        const val ACTION_RESUME: Int = 2 //biến chạy lại
        const val ACTION_CANCEL: Int = 3 //biển hủy
        const val ACTION_START: Int = 4
    }
    private var player: ExoPlayer? = null
    override fun onBind(intent: Intent?): IBinder {
        Log.e("PlayMusicService", "PlayMusicService : MyService onBind")
        return myBinder
    }

    inner class MyBinder : Binder(){
        fun getMyService(): PlayMusicService {
            return this@PlayMusicService
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("PlayMusicService", "PlayMusicService : MyService onCreate")
    }

    @UnstableApi
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //get bundle an object to main_activity
        val bundle = intent!!.extras
        if (bundle != null) {
            val track: Track? = bundle["objectSong"] as Track?
            if (track != null) {
                mTracks = track
                playMedia(track.preview)
                sendNotification(track)
            }
        }
        val actionMusic = intent.getIntExtra("action_music_service", 0)
        handleActionMusic(actionMusic)
        return START_STICKY
    }

    @UnstableApi
    private fun playMedia(mediaUrl:String) {
        // Release any existing player instance
        releasePlayer()
        // Create a new player instance
        player = ExoPlayer.Builder(this)
            .setTrackSelector(DefaultTrackSelector(this))
            .build()
        // Create a media item
        val mediaItem = MediaItem.fromUri(mediaUrl)
        val mediaSource = ProgressiveMediaSource.Factory(
            DefaultDataSource.Factory(this)
        ).createMediaSource(mediaItem)
        player?.setMediaSource(mediaSource)
        player?.prepare()
        // Start playback
        player?.playWhenReady = true
        isPlaying = true
        sendActionActivity(ACTION_START)
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("PlayMusicService", "PlayMusicService onDestroy")
        releasePlayer()
    }

    ///check 15:00
    private fun handleActionMusic(action: Int) {
        when (action) {
            ACTION_PAUSE -> actionPauseMusic()
            ACTION_RESUME -> actionResumeMusic()
            ACTION_CANCEL -> {
                stopSelf() //stop service
                sendActionActivity(ACTION_CANCEL)
            }
        }
    }

    private fun actionPauseMusic() {
        if (player != null && isPlaying) {
            player!!.pause()
            isPlaying = false
            sendNotification(mTracks!!)
            sendActionActivity(ACTION_PAUSE)
        }
    }

    private fun actionResumeMusic() {
        if (player != null && !isPlaying) {
            player!!.play()
            isPlaying = true
            sendNotification(mTracks!!)
            sendActionActivity(ACTION_RESUME)
        }
    }

    private fun sendActionActivity(action: Int) {
        val mIntent = Intent("send_data_to_activity")
        val bundle = Bundle()
        bundle.putSerializable("object_song", mTracks)
        bundle.putBoolean("status_player", isPlaying)
        bundle.putInt("action_music", action)
        mIntent.putExtras(bundle)
        LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
    }

//    xử lý foreground service với media style
    @SuppressLint("ForegroundServiceType")
    private fun sendNotification(mSong: Track) {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.image_13)
        val mediaSessionCompat = MediaSessionCompat(this, "tag")
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            this, MyApplication.CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_music)
            .setSubText("黄龙 - Hoang Long")
            .setContentTitle(mSong.title)
            .setContentText(mSong.artist.name)
            .setLargeIcon(bitmap)
            .setSound(null)
            .setSilent(true)
            .setCategory(NotificationCompat.PRIORITY_HIGH.toString())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
            )

        if (isPlaying) {
            notificationBuilder.addAction(R.drawable.ic_back, "Previous", null) // #1
                .addAction(
                    R.drawable.ic_pause,
                    "Pause",
                    getPendingIntent(this, ACTION_PAUSE)
                ) // #2
                .addAction(R.drawable.ic_next, "Next", null) // #3
//                .addAction(R.drawable.ic_shuffer, "Shuffle", null) // #4
//                .addAction(R.drawable.ic_repeat, "Repeat", null) // #5
        } else {
            notificationBuilder.addAction(R.drawable.ic_back, "Previous", null) // #1
                .addAction(
                    R.drawable.ic_play,
                    "Pause",
                    getPendingIntent(this, ACTION_RESUME)
                ) // #2
                .addAction(R.drawable.ic_next, "Next", null) // #3
//                .addAction(R.drawable.ic_shuffer, "Shuffle", null) // #4
//                .addAction(R.drawable.ic_repeat, "Repeat", null) // #5
        }

        val notification = notificationBuilder.build()
        startForeground(1, notification)
    }

    private fun getPendingIntent(mContext: Context, action: Int): PendingIntent {
        val mIntent = Intent(this, PlayMusicBroadcast::class.java)
        mIntent.putExtra("action_music", action)
        return PendingIntent.getBroadcast(
            mContext.applicationContext,
            action,
            mIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}