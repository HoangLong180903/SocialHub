package hoanglong180903.myproject.socialhub.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class MyApplication : Application() {

    companion object {
        const val CHANNEL_ID = "CHANNEL_EXAMPLE"
    }
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = "Music Notification Channel"
//            val descriptionText = "Channel for music notifications"
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel("MUSIC_CHANNEL_ID", name, importance).apply {
//                description = descriptionText
//            }
//            val notificationManager: NotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name: CharSequence = "channel id example"
//            val description = "Example Service Channel"
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel(CHANNEL_ID, name, importance)
//            channel.description = description
//
//            val notificationManager = getSystemService(
//                NotificationManager::class.java
//            )
//            notificationManager.createNotificationChannel(channel)
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel Example"
            val descriptionText = "Example Service Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}