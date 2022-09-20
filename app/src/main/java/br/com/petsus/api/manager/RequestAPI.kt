package br.com.petsus.api.manager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.petsus.AppApplication
import br.com.petsus.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RequestAPI {

    private val logout: MutableLiveData<Unit> = MutableLiveData()

    val observerLogout: LiveData<Unit>
        get() = logout

    lateinit var api: Retrofit
        private set

    fun initialize(application: AppApplication) {
        api = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .apply {
                        if (BuildConfig.DEBUG)
                            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    }
                    .authenticator(AuthenticatorInterceptor(sessionManager = application.sessionManager))
                    .addInterceptor(AuthInterceptor(sessionManager = application.sessionManager))
                    .build()
            )
            .build()
    }

    class AuthenticatorInterceptor(private val sessionManager: SessionManager) : Authenticator {
        override fun authenticate(route: Route, response: Response): Request? {
            sessionManager.fetchToken()?.let { authToken ->
                return response.request().newBuilder()
                    .header("Authorization", authToken.token)
                    .build()
            }
            return null
        }
    }

    open class BaseInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
            insertHeaders(request = builder)
            return chain.proceed(builder.build())
        }

        open fun insertHeaders(request: Request.Builder) {
            request.addHeader("App-Type", "Mobile.Android")
            request.addHeader("App-Version", BuildConfig.VERSION_NAME)
        }
    }

    class AuthInterceptor(
        private val sessionManager: SessionManager
    ) : BaseInterceptor() {
        override fun insertHeaders(request: Request.Builder) {
            super.insertHeaders(request)

            sessionManager.token?.apply {
                request.addHeader("Authorization", completeToken)
            }
        }
    }

}