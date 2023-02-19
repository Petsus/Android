package br.com.petsus.normal.api.manager

import androidx.annotation.WorkerThread
import br.com.petsus.BuildConfig
import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.model.auth.RefreshToken
import br.com.petsus.application.preferences.AppPreferences
import br.com.petsus.normal.api.exception.ApiException
import br.com.petsus.normal.api.service.auth.AuthRepositoryRetrofit
import br.com.petsus.util.tool.Keys
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SessionManager private constructor(
    private val preferences: AppPreferences,
    interceptor: Interceptor
) {
    companion object {
        lateinit var current: SessionManager
            private set

        fun create(preferences: AppPreferences, interceptor: Interceptor): SessionManager  {
            return SessionManager(preferences = preferences, interceptor = interceptor).apply { current = this }
        }
    }

    private val api: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .apply {
                    if (BuildConfig.DEBUG)
                        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                }
                .addInterceptor(interceptor)
                .build()
        )
        .build()

    @WorkerThread
    fun fetchToken(): Result<AuthToken>? {
        val currentToken = preferences.getObject(Keys.KEY_TOKEN.valueKey, AuthToken::class.java) ?: return null
        if (currentToken.expiration >= System.currentTimeMillis())
            return Result.success(value = currentToken)
        try {
            val request = api.create(AuthRepositoryRetrofit::class.java)
                .refresh(RefreshToken(token = currentToken.token))
                .execute()

            return when {
                request.code() in 200..299 -> {
                    val token = request.body()?.data ?: throw JSONException("Not load json token")
                    this@SessionManager.token = token
                    Result.success(value = token)
                }
                request.code() in 400..499 -> {
                    this@SessionManager.token = null
                    null
                }
                else -> throw ApiException(response = request)
            }

        } catch (e: Throwable) {
            return Result.failure(exception = e)
        }
    }

    @get:Synchronized
    @set:Synchronized
    var token: AuthToken?
        get() = preferences.getObject(Keys.KEY_TOKEN.valueKey, AuthToken::class.java)
        set(value) {
            preferences.putObject(Keys.KEY_TOKEN.valueKey, value)
        }

    val hasToken: Boolean
        get() = token != null

}