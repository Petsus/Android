package br.com.petsus.service

import br.com.petsus.api.service.notification.NotificationRepository
import br.com.petsus.util.global.notification.AppNotification
import br.com.petsus.util.tool.Keys
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MessagingService @Inject constructor() : FirebaseMessagingService() {
    @Inject
    lateinit var repository: NotificationRepository

    private val coroutineScope = CoroutineScope(SupervisorJob())

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        coroutineScope.launch {
            repository.tokenNotification(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        coroutineScope.launch {
            val data = message.data
            AppNotification.Builder(repository)
                .title(data[Keys.NOTIFICATION_TITLE.valueKey] ?: "")
                .subtitle(data[Keys.NOTIFICATION_SUBTITLE.valueKey])
                .image(data[Keys.NOTIFICATION_IMAGE.valueKey])
                .channel(data[Keys.NOTIFICATION_CHANNEL.valueKey])
                .notificationId(data[Keys.NOTIFICATION_ID.valueKey])
                .builder(this@MessagingService)
                .notify()
        }
    }
}