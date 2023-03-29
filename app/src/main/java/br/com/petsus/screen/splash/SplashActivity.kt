package br.com.petsus.screen.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import br.com.petsus.screen.login.start.LoginActivity
import br.com.petsus.util.base.activity.BaseActivity
import br.com.petsus.screen.home.HomeActivity
import br.com.petsus.util.base.viewmodel.StringFormatter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }

        viewModel.isLogged().observe(this) { result ->
            when {
                result == null -> openActivity(LoginActivity::class.java)
                result.isFailure -> error(StringFormatter(throwable = result.exceptionOrNull()))
                result.isSuccess -> openActivity(HomeActivity::class.java)
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