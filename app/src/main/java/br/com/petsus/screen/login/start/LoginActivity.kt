package br.com.petsus.screen.login.start

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.petsus.databinding.ActivityLoginBinding
import br.com.petsus.screen.login.start.fragment.LoginFragment

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        goTo(LoginFragment())
    }

    fun goTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.loginContainer.id, fragment, fragment::class.java.name)
            .addToBackStack(fragment::class.java.name)
            .commit()
    }

}