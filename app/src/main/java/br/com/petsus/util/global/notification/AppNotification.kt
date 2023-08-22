package br.com.petsus.util.global.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import br.com.petsus.R
import br.com.petsus.api.service.notification.NotificationRepository
import br.com.petsus.screen.notification.NotificationActivity

class AppNotification private constructor(
    private val context: Context,
    private val notification: Notification
) {

    class Builder(private val notificationRepository: NotificationRepository) {
        private var title: String? = null
        private var subtitle: String? = null
        private var image: String? = null
        private var channel: String? = null
        private var notificationId: String? = null

        fun title(value: String): Builder {
            title = value
            return this
        }
        fun subtitle(value: String?): Builder {
            subtitle = value
            return this
        }
        fun image(value: String?): Builder {
            image = value
            return this
        }
        fun channel(value: String?): Builder {
            channel = value
            return this
        }
        fun notificationId(value: String?): Builder {
            notificationId = value
            return this
        }
        suspend fun builder(context: Context): AppNotification {
            assert(!title.isNullOrBlank())
            val style = style()

            val notificationIntent = Intent(context, NotificationActivity::class.java)
                .putExtra(NOTIFICATION_EXTRA, notificationId)
            val notificationScreen = PendingIntent.getActivity(context, NOTIFICATION_REQUEST_CODE, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

            return AppNotification(
                context = context,
                notification = NotificationCompat.Builder(context, getChannel(channel, context).id)
                    .setSmallIcon(R.drawable.icon_animal)
                    .setContentTitle(title)
                    .setContentText(subtitle)
                    .setStyle(style)
                    .setContentIntent(notificationScreen)
                    .build()
            )
        }

        private suspend fun style(): NotificationCompat.Style {
            val urlImage = image ?: return NotificationCompat.BigTextStyle().bigText(subtitle)
            var imgBitmap: Bitmap? = null
            notificationRepository.downloadImage(urlImage).collect { newImage ->
                imgBitmap = newImage
            }
            return NotificationCompat.BigPictureStyle()
                .bigPicture(imgBitmap)
        }

        private fun getChannel(channelId: String?, context: Context): NotificationChannelCompat {
            val manager = NotificationManagerCompat.from(context)
            val channelsCompat = manager.notificationChannelsCompat

            val appChannels = channels(context)
            if (channelsCompat.isEmpty())
                manager.createNotificationChannelsCompat(appChannels)
            return appChannels.find { channel -> channel.id == channelId } ?: appChannels.first()
        }
    }

    @JvmName("createNotification")
    fun notify() {
        when  {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU -> showNotification()
            ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED -> showNotification()
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification() {
        NotificationManagerCompat.from(context)
            .notify(System.currentTimeMillis().toInt(), notification)
    }

    companion object {
        const val NOTIFICATION_REQUEST_CODE = 12
        const val NOTIFICATION_EXTRA = "notification_extra"

        private val appLevels: IntArray =
            intArrayOf(NotificationManagerCompat.IMPORTANCE_LOW, NotificationManagerCompat.IMPORTANCE_DEFAULT, NotificationManagerCompat.IMPORTANCE_HIGH)

        private fun channels(context: Context): List<NotificationChannelCompat> {
            return with(context.resources) {
                val names = getStringArray(R.array.channels_name).filterNotNull()
                val descriptions = getStringArray(R.array.channels_description).filterNotNull()
                val levels = getIntArray(R.array.channels_level)

                return@with getStringArray(R.array.channels_id)
                    .filterNotNull()
                    .mapIndexed { index, id ->
                        return@mapIndexed NotificationChannelCompat.Builder(id, appLevels[levels.getOrNull(index) ?: 1])
                            .setName(names.getOrNull(index))
                            .setDescription(descriptions.getOrNull(index))
                            .build()
                    }
            }
        }
    }
}