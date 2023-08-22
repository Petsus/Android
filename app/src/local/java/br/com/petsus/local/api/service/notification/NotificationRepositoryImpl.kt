package br.com.petsus.local.api.service.notification

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import br.com.petsus.R
import br.com.petsus.api.model.notifications.Notifications
import br.com.petsus.api.model.notifications.NotificationsAnimal
import br.com.petsus.api.service.notification.NotificationRepository
import br.com.petsus.local.util.delayDefault
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NotificationRepositoryImpl(
    private val context: Context
) : NotificationRepository {
    private fun generateNotification() = Notifications(
        title = "Title",
        subtitle = "Subtitle",
        notificationId = "asdadasd"
    )
    override fun notification(): Flow<List<Notifications>> {
        return flow {
            delayDefault()
            emit(listOf(generateNotification(), generateNotification(), generateNotification()))
        }
    }

    override fun tokenNotification(token: String) = Unit

    override fun clearTokenNotification(appToken: String) = Unit

    override fun downloadImage(url: String): Flow<Bitmap> {
        return flow {
            delayDefault()
            emit(
                ResourcesCompat.getDrawable(context.resources, R.drawable.sample5, context.theme)!!.toBitmap()
            )
        }
    }

    override fun getDetailsNotificationAnimal(notificationId: String): Flow<NotificationsAnimal> {
        return flow {
            delayDefault()
            emit(
                NotificationsAnimal(
                    lat = 54.0,
                    lng = 0.0,
                    name = "Mato",
                    image = "https://steamuserimages-a.akamaihd.net/ugc/2029481402824246918/420C7ED8187DB71DAB443C734FD4BD9E984DA14D/?imw=637&imh=358&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=true",
                    address = "Rua Qualquer, 123 Vila Random Random/RM",
                    notificationID = "asdadasd"
                )
            )
        }
    }
}

@Module
@InstallIn(ActivityComponent::class, ViewModelComponent::class, ServiceComponent::class)
class NotificationModule {
    @Provides
    fun notifications(@ApplicationContext context: Context): NotificationRepository = NotificationRepositoryImpl(context)
}