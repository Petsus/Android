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

class ApiManager(sessionManager: SessionManager) {
    companion object {
        lateinit var current: ApiManager
            private set

        fun initialize(sessionManager: SessionManager) { current = ApiManager(sessionManager) }

        fun <T>create(clazz: Class<T>): T = current.retrofit.create(clazz)
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
            return chain.proceed(builder.build())
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