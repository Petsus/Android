package br.com.petsus.screen.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import br.com.petsus.screen.home.HomeActivity
import br.com.petsus.screen.login.start.LoginActivity
import br.com.petsus.util.base.activity.AppActivity
import br.com.petsus.util.base.viewmodel.StringFormatter
import br.com.petsus.util.base.viewmodel.appViewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppActivity() {

    private val viewModel: SplashViewModel by appViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }

        viewModel.isLogged().observe(this) { result ->
            when (result?.isSuccess) {
                true -> openActivity(HomeActivity::class.java)
                else -> openActivity(LoginActivity::class.java)
            }
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