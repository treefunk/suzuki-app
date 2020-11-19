package com.myoptimind.suzuki_app.features.suzuki_diary.service_history

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.data.ServiceHistoryDetails
import timber.log.Timber


/**
 *  For scheduled service history notifications
 */
class ServiceHistoryBroadcastReceiver(): BroadcastReceiver() {

    companion object{
        const val EXTRA_SERVICE_HISTORY_DETAILS = "extra_service_history_details" // for service history details object
        const val EXTRA_BEAST_NICKNAME = "extra_beast_nickname"
        const val EXTRA_SERVICE_HISTORY_NOTIF_TYPE = "extra_service_history_notif_type" // notif type (defines the contents)

        const val TYPE_DAY_BEFORE = "type_day_before" // check if notif is day before
        const val TYPE_DAY_ITSELF = "type_day_itself"
        private const val CHANNEL_ID = "service_history_channel_100"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if(context != null && intent != null){
            val extras = intent.extras
            Timber.v("extras " + extras.toString())


            val beastNickname = extras?.getString(EXTRA_BEAST_NICKNAME,"")
//            val serviceHistoryDetails = extras?.getParcelable<ServiceHistoryDetails>(EXTRA_SERVICE_HISTORY_DETAILS)
            val type = extras?.getString(EXTRA_SERVICE_HISTORY_NOTIF_TYPE)

            Timber.v("notifying from receiver..")
            Timber.v("type $type")
            Timber.v("beast nickname $beastNickname")
            if(beastNickname != null && beastNickname.isNotEmpty()){
                createNotificationChannel(context)
                val notif: Notification = when(type){
                    TYPE_DAY_BEFORE -> createNotificationDayBefore(context,beastNickname)
                    TYPE_DAY_ITSELF -> createNotificationDayItself(context,beastNickname)
                    else -> createNotificationDayBefore(context,beastNickname)
                }

                with(NotificationManagerCompat.from(context)){
                    this.notify(300,notif)
                }
            }


        }
    }

    private fun createNotificationDayItself(context: Context, beastNickname: String): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.suzuki_logo) //todo: add logo to notif
                .setContentTitle("Reminder!")
                .setContentText("Your service for ${beastNickname} is due today!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
    }

    fun createNotificationDayBefore(context: Context, beastNickname: String): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.suzuki_logo) //todo: add logo to notif
                .setContentTitle("Reminder!")
                .setContentText("Your service for ${beastNickname} is due tomorrow!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ServiceHistoryChannel"
            val descriptionText = "schedules next pms date"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}