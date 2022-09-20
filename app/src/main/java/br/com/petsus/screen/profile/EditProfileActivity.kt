package br.com.petsus.screen.profile

import android.os.Bundle
import br.com.petsus.databinding.ActivityEditProfileBinding
import br.com.petsus.util.base.activity.BaseActivity
import br.com.petsus.util.base.viewmodel.appViewModels

class EditProfileActivity : BaseActivity() {

    private val viewModel: EditProfileViewModel by appViewModels()

    private val binding: ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        showLoading()

        viewModel.get().observe(this) { user ->
            dismissLoading()
            binding.inputEmail.editText?.setText(user.data.email)
            binding.inputPhone.editText?.setText(user.data.phone)
            binding.inputName.editText?.setText(user.data.name)
        }
    }

}