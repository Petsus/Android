package br.com.petsus

import android.app.Application
import br.com.petsus.api.manager.RequestAPI
import br.com.petsus.api.manager.SessionManager
import br.com.petsus.api.service.APIRepository
import br.com.petsus.api.service.APIRepositoryImpl

class AppApplication : Application() {

    val repository: APIRepository = APIRepositoryImpl()

    lateinit var sessionManager: SessionManager
        private set

    override fun onCreate() {
        super.onCreate()

        sessionManager = SessionManager(interceptor = RequestAPI.BaseInterceptor(), context = applicationContext)
        RequestAPI.initialize(this)
    }
}