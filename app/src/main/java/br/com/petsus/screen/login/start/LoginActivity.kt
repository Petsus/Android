package br.com.petsus.screen.login.start

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import br.com.petsus.R
import br.com.petsus.databinding.ActivityLoginBinding
import br.com.petsus.screen.login.start.fragment.LoginFragment

class LoginActivity : AppCompatActivity() {

    var topInset: Int = 0
        private set

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(window.decorView.rootView) { view, inset ->
            topInset = inset.getInsets(WindowInsetsCompat.Type.navigationBars()).top
            return@setOnApplyWindowInsetsListener WindowInsetsCompat.Builder(inset)
                .setInsets(WindowInsetsCompat.Type.statusBars(), Insets.NONE)
                .build()
        }

        goTo(LoginFragment())
    }

    fun goTo(fragment: Fragment) {
        supportFragmentManager.commit {
            setCustomAnimations(R.anim.full_enter_fragment, R.anim.full_exit_fragment, R.anim.full_pop_enter_fragment, R.anim.full_pop_exit_fragment)
            replace(binding.loginContainer.id, fragment, fragment::class.java.name)
            if (fragment !is LoginFragment)
                addToBackStack(fragment::class.java.name)
        }
    }

}