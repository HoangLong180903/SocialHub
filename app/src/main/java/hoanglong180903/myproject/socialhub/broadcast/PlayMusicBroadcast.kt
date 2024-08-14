package hoanglong180903.myproject.socialhub.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hoanglong180903.myproject.socialhub.service.PlayMusicService

class PlayMusicBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val actionMusic = intent!!.getIntExtra("action_music", 0)
        val mIntent = Intent(context, PlayMusicService::class.java)
        mIntent.putExtra("action_music_service", actionMusic)
        context!!.startService(mIntent)
    }
}