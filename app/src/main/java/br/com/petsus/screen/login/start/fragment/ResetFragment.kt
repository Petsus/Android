package br.com.petsus.screen.login.start.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.updatePadding
import br.com.petsus.R
import br.com.petsus.databinding.FragmentResetPasswordBinding
import br.com.petsus.screen.login.start.LoginViewModel
import br.com.petsus.util.base.fragment.BaseFragment
import br.com.petsus.util.base.viewmodel.StringFormatter
import br.com.petsus.util.base.viewmodel.appViewModels
import br.com.petsus.util.tool.preventDoubleClick

class ResetFragment : BaseFragment<FragmentResetPasswordBinding>() {

    private val rootView: View by lazy {
        requireActivity().findViewById(android.R.id.content)
    }

    private val listenerKeyboard = ViewTreeObserver.OnGlobalLayoutListener {
        val visibleBounds = Rect()
        rootView.getWindowVisibleDisplayFrame(visibleBounds)
        val heightDiff = rootView.height - visibleBounds.height()

        binding?.root?.updatePadding(bottom = heightDiff)
    }

    private val viewModel: LoginViewModel by appViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.resetPassword?.setOnClickListener {
            it.preventDoubleClick()
            loading()

            viewModel.resetPassword(
                email = binding?.inputEmail?.editText?.text?.toString()
            ).observe(viewLifecycleOwner) {
                closeLoading()
                message(StringFormatter(messageId = R.string.send_reset_successfull))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        rootView.viewTreeObserver.addOnGlobalLayoutListener(listenerKeyboard)
    }

    override fun onPause() {
        super.onPause()
        rootView.viewTreeObserver.removeOnGlobalLayoutListener(listenerKeyboard)
    }

}