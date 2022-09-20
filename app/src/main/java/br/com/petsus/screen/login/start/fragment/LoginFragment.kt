package br.com.petsus.screen.login.start.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import br.com.petsus.R
import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.model.base.BaseResponse
import br.com.petsus.databinding.FragmentLoginBinding
import br.com.petsus.screen.login.start.LoginActivity
import br.com.petsus.screen.login.start.LoginViewModel
import br.com.petsus.util.base.BaseFragment
import br.com.petsus.util.base.activity.HomeActivity
import br.com.petsus.util.base.viewmodel.appViewModels
import br.com.petsus.util.tool.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by appViewModels()

    private val requestLoginGoogle = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result == null || result.resultCode != Activity.RESULT_OK)
            return@registerForActivityResult
        runCatching {
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data).getResult(ApiException::class.java)
            viewModel.login(email = null, password = null, googleAuthCode = account.serverAuthCode).observer()
        }.onFailure {
            it.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureView()
    }

    private fun configureView() {
        binding?.login?.setOnClickListener {
            it.preventDoubleClick()
            showLoading()

            viewModel.login(
                email = binding?.inputEmail?.editText?.text?.toString(),
                password = binding?.inputPassword?.editText?.text?.toString(),
                googleAuthCode = null
            ).observer()
        }
        binding?.createAccount?.setOnClickListener {
            activity?.cast<LoginActivity>()?.goTo(CreateAccountFragment())
        }
        binding?.resetPassword?.setOnClickListener {
            activity?.cast<LoginActivity>()?.goTo(ResetFragment())
        }
        binding?.loginGoogle?.setOnClickListener {
            showLoading()
            val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            requestLoginGoogle.launch(GoogleSignIn.getClient(requireActivity(), signInOptions).signInIntent)
        }
    }

    private fun LiveData<BaseResponse<AuthToken>>.observer() {
        observe(viewLifecycleOwner) { response ->
            dismissLoading()
            context?.apply {
                sharedPreferences.putObject(Keys.KEY_TOKEN.valueKey, response.data)
                startActivity(
                    Intent(this, HomeActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }

}