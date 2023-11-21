package br.com.petsus.api.service.notification

import android.graphics.Bitmap
import br.com.petsus.api.model.notifications.Notifications
import br.com.petsus.api.model.notifications.NotificationsAnimal
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun notification(): Flow<List<Notifications>>
    suspend fun tokenNotification(token: String)
    fun clearTokenNotification(appToken: String)
    fun downloadImage(url: String): Flow<Bitmap>
    fun getDetailsNotificationAnimal(notificationId: String): Flow<NotificationsAnimal>
    suspend fun sendNotificationPushToken()
}