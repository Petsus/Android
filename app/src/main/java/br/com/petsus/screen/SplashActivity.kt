package br.com.petsus.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import br.com.petsus.AppApplication
import br.com.petsus.screen.login.start.LoginActivity
import br.com.petsus.util.base.activity.BaseActivity
import br.com.petsus.util.base.activity.HomeActivity
import br.com.petsus.util.base.viewmodel.StringFormatter
import br.com.petsus.util.tool.cast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }

        lifecycleScope.launchWhenResumed {
            val sessionManager = application.cast<AppApplication>().sessionManager
            runCatching {
                val token = withContext(Dispatchers.IO) { sessionManager.fetchToken() }
                token?.let {
                    withContext(Dispatchers.Main) {
                        startActivity(
                            Intent(this@SplashActivity, HomeActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                    }
                } ?: run {
                    if (!sessionManager.hasToken)
                        startActivity(
                            Intent(this@SplashActivity, LoginActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                    else
                        error(StringFormatter())
                }
            }.messageError()
        }
    }

}