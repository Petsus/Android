package br.com.petsus.screen.profile

import android.os.Bundle
import br.com.petsus.api.model.user.User
import br.com.petsus.databinding.ActivityEditProfileBinding
import br.com.petsus.util.base.activity.AppActivity
import br.com.petsus.util.base.viewmodel.appViewModels
import br.com.petsus.util.tool.preventDoubleClick
import br.com.petsus.util.tool.text
import com.redmadrobot.inputmask.MaskedTextChangedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : AppActivity() {

    private val viewModel: EditProfileViewModel by appViewModels()

    private val binding: ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    private val maskedPhone: MaskedTextChangedListener by lazy {
        MaskedTextChangedListener.installOn(binding.inputPhone.editText!!, "{(}[00]{)} [00000]{-}[0000]")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observers()
        loadingData()
    }

    private fun loadingData() {
        showLoading()
        viewModel.get().observe(this) { user ->
            closeLoading()
            insertData(user = user)
        }
    }

    private fun insertData(user: User) {
        binding.inputEmail.editText?.setText(user.email)
        binding.inputName.editText?.setText(user.name)

        maskedPhone.setText(user.phone ?: "")
    }

    private fun observers() {
        binding.backAppBar.setOnBackClickListener { finish() }
        binding.updateProfile.setOnClickListener { button ->
            button.preventDoubleClick()
            updateValuesUser()
        }
    }

    private fun updateValuesUser() {
        viewModel.current?.let { user ->
            user.insertValues()
            viewModel.update(
                user = user,
                loading = { showLoading() }
            ).observe(this) { this.callbackResponse() }
        }
    }

    private fun User.insertValues() {
        this.phone = binding.inputPhone.text
        this.name = binding.inputName.text ?: ""
        this.email = binding.inputEmail.text ?: ""
    }

    private fun callbackResponse() = closeLoading { finish() }
}