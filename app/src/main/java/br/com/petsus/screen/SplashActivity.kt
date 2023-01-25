package br.com.petsus.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import br.com.petsus.api.service.auth.SessionRepository
import br.com.petsus.screen.login.start.LoginActivity
import br.com.petsus.util.base.activity.BaseActivity
import br.com.petsus.util.base.activity.HomeActivity
import br.com.petsus.util.base.viewmodel.StringFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    @Inject lateinit var sessionRepository: SessionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }

        lifecycleScope.launchWhenResumed {
            runCatching {
                val token = withContext(Dispatchers.IO) { sessionRepository.fetchToken() }
                when {
                    token != null -> openActivity(HomeActivity::class.java)
                    !sessionRepository.hasToken -> openActivity(LoginActivity::class.java)
                    else -> error(StringFormatter())
                }
            }.messageError()
        }
    }

    private fun openActivity(clazz: Class<*>) {
        startActivity(
            Intent(this@SplashActivity, clazz)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}