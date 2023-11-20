package br.com.petsus.normal.api.manager

import br.com.petsus.BuildConfig
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiManager(sessionManager: SessionManager) {
    companion object {
        private lateinit var current: ApiManager

        fun initialize(sessionManager: SessionManager) { current = ApiManager(sessionManager) }

        fun <T>create(clazz: Class<T>): T = current.retrofit.create(clazz)

        private const val TIMEOUT = 60L
    }

    private val retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .apply {
                        if (BuildConfig.DEBUG)
                            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    }
                    .callTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .authenticator(AuthenticatorInterceptor(sessionManager = sessionManager))
                    .addInterceptor(AuthInterceptor(sessionManager = sessionManager))
                    .build()
            )
            .build()
    }

    private class AuthenticatorInterceptor(private val sessionManager: SessionManager) : Authenticator {
        override fun authenticate(route: Route?, response: Response): Request? {
            sessionManager.fetchToken()?.getOrNull()?.let { authToken ->
                return response.request.newBuilder()
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

            try {
                return chain.proceed(builder.build())
            } catch (e: Throwable) {
                throw e
            }
        }

        open fun insertHeaders(request: Request.Builder) {
            request.addHeader("App-Type", "Mobile.Android")
            request.addHeader("App-Version", BuildConfig.VERSION_NAME)
        }
    }

    private class AuthInterceptor(
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