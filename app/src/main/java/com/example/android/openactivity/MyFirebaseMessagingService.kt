package com.example.android.openactivity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject


/**
 * Created by Arif Ikhsanudin on Wednesday, 12 December 2018.
 */

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        var TAG = MyFirebaseMessagingService::class.java.simpleName
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        remoteMessage?.let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            val data = JSONObject(remoteMessage.data)
            val jsonMessage = data.getString("extra_information")
            Log.d(TAG, "onMessageReceived: Extra Information: $jsonMessage")
        }

        if (null != remoteMessage?.notification) {
            val title = remoteMessage.notification?.title
            val message = remoteMessage.notification?.body
            val clickAction = remoteMessage.notification?.clickAction

            Log.d(TAG, "Message Notification Title: $title")
            Log.d(TAG, "Message Notification Body: $message")
            Log.d(TAG, "Message Notification click_action: $clickAction")

            sendNotification(title, message, clickAction)
        }
    }

    override fun onDeletedMessages() {

    }

    private fun sendNotification(title: String?, messageBody: String?, click_action: String?) {
        val intent: Intent
        when (click_action) {
            "SOMEACTIVITY" -> {
                intent = Intent(this, SomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            "MAINACTIVITY" -> {
                intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            else -> {
                intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}
