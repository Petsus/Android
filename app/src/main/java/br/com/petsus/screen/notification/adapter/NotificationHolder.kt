package br.com.petsus.screen.notification.adapter

import androidx.recyclerview.widget.RecyclerView
import br.com.petsus.api.model.notifications.Notifications
import br.com.petsus.databinding.CardNotificationBinding

class NotificationHolder(val binding: CardNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
    fun load(notifications: Notifications) {
        binding.notificationTitle.text = notifications.title
        binding.notificationSubtitle.text = notifications.subtitle
    }
}