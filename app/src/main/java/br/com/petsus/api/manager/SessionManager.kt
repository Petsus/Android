package br.com.petsus.api.manager

import android.content.Context
import androidx.annotation.WorkerThread
import br.com.petsus.BuildConfig
import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.model.auth.RefreshToken
import br.com.petsus.api.service.AuthRepository
import br.com.petsus.util.tool.Keys
import br.com.petsus.util.tool.getObject
import br.com.petsus.util.tool.putObject
import br.com.petsus.util.tool.sharedPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.ref.WeakReference

class SessionManager(
    context: Context,
    interceptor: Interceptor
) {

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

    private val weakContext: WeakReference<Context> = WeakReference(context)

    @WorkerThread
    fun fetchToken(): AuthToken? {
        val currentToken = weakContext.get()?.sharedPreferences?.getObject<AuthToken>(Keys.KEY_TOKEN.valueKey) ?: return null
        if (currentToken.expiration < System.currentTimeMillis())
            return currentToken
        return api.create(AuthRepository::class.java).refreshToken(RefreshToken(token = currentToken.token))?.execute()?.run {
            when {
                this.code() in 200..299 -> {
                    val token = this.body().data
                    this@SessionManager.token = token
                    return@run token
                }
                this.code() in 400..499 -> {
                    this@SessionManager.token = null
                    return@run body().data
                }
                else -> return@run null
            }
        }
    }

    @get:Synchronized
    @set:Synchronized
    var token: AuthToken?
        get() = weakContext.get()?.sharedPreferences?.getObject(Keys.KEY_TOKEN.valueKey)
        set(value) {
            weakContext.get()?.sharedPreferences?.putObject(Keys.KEY_TOKEN.valueKey, value)
        }

    val hasToken: Boolean
        get() = token != null

}