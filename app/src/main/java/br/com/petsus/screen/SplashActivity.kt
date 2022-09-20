package br.com.petsus.screen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.petsus.AppApplication
import br.com.petsus.screen.login.start.LoginActivity
import br.com.petsus.util.base.activity.HomeActivity
import br.com.petsus.util.tool.cast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenResumed {
            withContext(Dispatchers.IO) {
                runCatching {
                    val sessionManager = application.cast<AppApplication>().sessionManager
                    sessionManager.fetchToken()?.let {
                        withContext(Dispatchers.Main) {
                            startActivity(
                                Intent(this@SplashActivity, HomeActivity::class.java)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                        }
                    } ?: run {
                        if (!sessionManager.hasToken)
                            withContext(Dispatchers.Main) {
                                startActivity(
                                    Intent(this@SplashActivity, LoginActivity::class.java)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                )
                            }
                        else
                            TODO("Warning error API")
                    }
                }.onFailure {
                    it.printStackTrace()
                }
            }
        }
    }

}