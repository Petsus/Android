package br.com.petsus.screen.notification.adapter

import android.view.ViewGroup
import br.com.petsus.api.model.notifications.Notifications
import br.com.petsus.databinding.CardNotificationBinding
import br.com.petsus.util.base.adapter.BaseAdapter
import br.com.petsus.util.tool.inflater

class NotificationAdapter : BaseAdapter<Notifications, NotificationHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder =
        NotificationHolder(CardNotificationBinding.inflate(parent.inflater, parent, false))

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        holder.load(elements[position])
        holder.binding.root.setOnClickListener { callOnClick(elements[position]) }
    }
}