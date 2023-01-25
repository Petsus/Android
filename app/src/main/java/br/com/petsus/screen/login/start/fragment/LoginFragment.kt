package br.com.petsus.screen.login.start.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.databinding.FragmentLoginBinding
import br.com.petsus.screen.login.start.LoginActivity
import br.com.petsus.screen.login.start.LoginViewModel
import br.com.petsus.util.base.activity.HomeActivity
import br.com.petsus.util.base.fragment.BaseFragment
import br.com.petsus.util.tool.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    private val rootView: View by lazy {
        requireActivity().findViewById(android.R.id.content)
    }

    private val listenerKeyboard = ViewTreeObserver.OnGlobalLayoutListener {
        val visibleBounds = Rect()
        rootView.getWindowVisibleDisplayFrame(visibleBounds)
        val heightDiff = rootView.height - visibleBounds.height()

        binding?.root?.updatePadding(bottom = heightDiff)
    }

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

    override fun onResume() {
        super.onResume()
        rootView.viewTreeObserver.addOnGlobalLayoutListener(listenerKeyboard)
    }

    override fun onPause() {
        super.onPause()
        rootView.viewTreeObserver.removeOnGlobalLayoutListener(listenerKeyboard)
    }

    private fun configureView() {
        binding?.login?.setOnClickListener {
            it.preventDoubleClick()
            loading()

            viewModel.login(
                email = binding?.inputEmail?.editText?.text?.toString(),
                password = binding?.inputPassword?.editText?.text?.toString(),
                googleAuthCode = null
            ).observer()
        }
        binding?.createAccount?.setOnClickListener {
            activity?.cast<LoginActivity>()?.goToFragment(CreateAccountFragment())
        }
        binding?.resetPassword?.setOnClickListener {
            activity?.cast<LoginActivity>()?.goToFragment(ResetFragment())
        }
        binding?.loginGoogle?.setOnClickListener {
//            loading()
//            val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestServerAuthCode(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build()
//            requestLoginGoogle.launch(GoogleSignIn.getClient(requireActivity(), signInOptions).signInIntent)
        }
    }

    private fun LiveData<AuthToken>.observer() {
        observe(viewLifecycleOwner) { response ->
            closeLoading()
            context?.apply {
                sharedPreferences.putObject(Keys.KEY_TOKEN.valueKey, response)
                startActivity(
                    Intent(this, HomeActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }

}