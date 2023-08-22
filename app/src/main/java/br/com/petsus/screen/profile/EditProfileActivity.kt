package br.com.petsus.screen.profile

import android.os.Bundle
import br.com.petsus.databinding.ActivityEditProfileBinding
import br.com.petsus.util.base.activity.AppActivity
import br.com.petsus.util.base.viewmodel.appViewModels
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

        configureView()
    }

    private fun configureView() {
        showLoading()

        binding.backAppBar.setOnBackClickListener { finish() }

        viewModel.get().observe(this) { user ->
            closeLoading()
            binding.inputEmail.editText?.setText(user.email)
            binding.inputName.editText?.setText(user.name)

            maskedPhone.setText(user.phone ?: "")

            binding.updateProfile.setOnClickListener {
                showLoading()
                viewModel.update(user).observe(this) {
                    closeLoading()
                }
            }
        }
    }

}