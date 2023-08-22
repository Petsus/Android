package br.com.petsus.normal.api.service.notification

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import br.com.petsus.api.exception.RepositoryException
import br.com.petsus.api.model.notifications.Notifications
import br.com.petsus.api.model.notifications.NotificationsAnimal
import br.com.petsus.api.service.notification.NotificationRepository
import br.com.petsus.application.preferences.AppPreferences
import br.com.petsus.normal.api.exception.send
import br.com.petsus.normal.api.manager.ApiManager
import br.com.petsus.normal.api.model.PushTokenRequest
import br.com.petsus.util.base.viewmodel.StringFormatter
import br.com.petsus.util.tool.Keys
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NotificationRepositoryImpl(
    private val context: Context,
    private val appPreferences: AppPreferences
) : NotificationRepository {

    override fun notification(): Flow<List<Notifications>> {
        return flow {
            val response = ApiManager
                .create(NotificationRepositoryRetrofit::class.java)
                .list()
            send(response = response)
        }
    }

    override fun tokenNotification(token: String) {
        runCatching {
            appPreferences.putObject(Keys.KEY_PUSH_TOKEN.valueKey, token)
            ApiManager
                .create(NotificationRepositoryRetrofit::class.java)
                .registerPushToken(PushTokenRequest(token = token))
        }
    }

    override fun clearTokenNotification(appToken: String) {
        runCatching {
            val token = appPreferences.getObject(Keys.KEY_PUSH_TOKEN.valueKey, String::class.java) ?: return@runCatching
            ApiManager
                .create(NotificationRepositoryRetrofit::class.java)
                .unregisterPushToken(token = appToken, PushTokenRequest(token = token))
        }
    }

    override fun downloadImage(url: String): Flow<Bitmap> {
        return flow {
            val result = suspendCoroutine { continuation ->
                Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .timeout(5000)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onLoadCleared(placeholder: Drawable?) = Unit
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) = continuation.resume(resource)
                        override fun onLoadFailed(errorDrawable: Drawable?) = continuation.resumeWithException(object : RepositoryException("Invalid image") {
                            override fun formatter(): StringFormatter = StringFormatter()
                        })
                    })
            }
            emit(result)
        }
    }

    override fun getDetailsNotificationAnimal(notificationId: String): Flow<NotificationsAnimal> {
        return flow {
            val response = ApiManager
                .create(NotificationRepositoryRetrofit::class.java)
                .details(id = notificationId)
            send(response = response)
        }
    }
}

@Module
@InstallIn(ActivityComponent::class, ViewModelComponent::class, ServiceComponent::class)
class NotificationModule {
    @Provides
    fun notification(@ApplicationContext context: Context, appPreferences: AppPreferences): NotificationRepository = NotificationRepositoryImpl(context, appPreferences)
}