package com.encoders.notificationinkotlin

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        sendNotification(remoteMessage.data)
    }

    private fun sendNotification(data: Map<String, String>) {
        var intent: Intent? = null
        val num = ++NOTIFICATION_ID
        val msg = Bundle()
        for (element in data) {
            msg.putString(element.key, element.value)
            msg.putString(element.key, element.value)
        }
        Log.e("sadfadsfadsfadsf", msg.toString())


        intent = Intent(this, MainActivity::class.java)
        intent.putExtra("ORDER_ID", msg.getString("ref_id"))



        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, num, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val notifyID = 1
        val CHANNEL_ID = "my_channel_01"// The id of the channel.
        val name = "name"// The user-visible name of the channel.
        val importance = NotificationManager.IMPORTANCE_HIGH
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notification = Notification.Builder(this@MyFirebaseMessagingService)
                .setContentTitle(msg.getString("title"))
                .setContentText(msg.getString("message"))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .build()
            val mNotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.createNotificationChannel(mChannel)
            mNotificationManager.notify(notifyID, notification)
        } else {

            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(msg.getString("title"))
                .setContentText(msg.getString("message"))
                .setAutoCancel(true)
                .setChannelId(CHANNEL_ID)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(num, notificationBuilder.build())
        }
    }

    companion object {
        var NOTIFICATION_ID = 1
    }
}