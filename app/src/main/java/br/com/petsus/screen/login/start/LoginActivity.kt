package br.com.petsus.screen.login.start

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.petsus.databinding.ActivityLoginBinding
import br.com.petsus.screen.login.welcome.WelcomeFragment
import br.com.petsus.util.global.router.Navigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityLoginBinding.inflate(layoutInflater).apply {
            setContentView(root)
            Navigator.of(loginContainer)
                .show(fragment = WelcomeFragment(), addToBack = false)
        }
    }
}