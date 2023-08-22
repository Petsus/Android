package br.com.petsus.screen.notification

import android.os.Bundle
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.petsus.databinding.ActivityNotificationsBinding
import br.com.petsus.screen.notification.adapter.NotificationAdapter
import br.com.petsus.screen.notification.dialog.DialogInfoFoundAnimal
import br.com.petsus.util.base.activity.AppActivity
import br.com.petsus.util.base.dialog.ErrorInterface
import br.com.petsus.util.base.viewmodel.StringFormatter
import br.com.petsus.util.base.viewmodel.appViewModels
import br.com.petsus.util.global.notification.AppNotification
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : AppActivity() {

    private val binding: ActivityNotificationsBinding by lazy {
        ActivityNotificationsBinding.inflate(layoutInflater)
    }

    private val adapter: NotificationAdapter = NotificationAdapter().apply {
        addClickListener { notification ->
            if (notification.notificationId != null)
                DialogInfoFoundAnimal(notification.notificationId)
                    .show(supportFragmentManager, null)
        }
    }

    private val viewModel: NotificationViewModel by appViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureView()

        verifyAndShowDialog()
    }

    private fun configureView() {
        binding.loadingNotification.show()
        binding.listNotification.isInvisible = true

        binding.listNotification.adapter = adapter
        binding.listNotification.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        binding.appBarNotification.setOnBackClickListener { finish() }

        viewModel.list().observe(this) { notifications ->
            binding.loadingNotification.hide()
            binding.listNotification.isInvisible = false

            adapter.update(notifications.toSet())
        }
    }

    private fun verifyAndShowDialog() {
        val notificationID = intent.getStringExtra(AppNotification.NOTIFICATION_EXTRA).takeIf { !it.isNullOrBlank() } ?: return
        DialogInfoFoundAnimal(notificationID)
            .show(supportFragmentManager, null)
    }

    override fun error(message: StringFormatter): ErrorInterface {
        binding.loadingNotification.hide()
        return super.error(message)
    }

}