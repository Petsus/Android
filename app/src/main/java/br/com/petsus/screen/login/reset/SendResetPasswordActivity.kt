package br.com.petsus.screen.login.reset

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import br.com.petsus.databinding.ActivitySendResetPasswordBinding
import br.com.petsus.screen.login.start.LoginActivity
import br.com.petsus.screen.login.start.LoginViewModel
import br.com.petsus.util.base.activity.BaseActivity
import br.com.petsus.util.tool.Keys
import br.com.petsus.util.tool.preventDoubleClick

class SendResetPasswordActivity : BaseActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent.data ?: run {
            finish()
            return
        }

        val token = uri.getQueryParameter(Keys.QUERY_TOKEN.valueKey)
        val email = uri.getQueryParameter(Keys.QUERY_EMAIL.valueKey)

        ActivitySendResetPasswordBinding.inflate(layoutInflater).apply {
            setContentView(root)

            inputEmail.editText?.setText(email)
            resetPassword.setOnClickListener { reset ->
                reset.preventDoubleClick()
                loading()

                viewModel.updatePassword(
                    email = email,
                    password = inputPassword.editText?.text?.toString(),
                    token = token
                ).observe(this@SendResetPasswordActivity) {
                    startActivity(
                        Intent(this@SendResetPasswordActivity, LoginActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
            }
        }
    }
}