package br.com.petsus.screen.address.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import br.com.petsus.R
import br.com.petsus.api.model.address.AppAddress
import br.com.petsus.databinding.CardAddressBinding
import br.com.petsus.util.global.Action
import br.com.petsus.util.tool.context

class UserAddressHolder(
    private val binding: CardAddressBinding,
    private val callOnClick: Action<AppAddress>,
) : RecyclerView.ViewHolder(binding.root) {
    fun configure(appAddress: AppAddress) {
        binding.apply {
            root.setOnClickListener { callOnClick.action(data = appAddress) }
            addressTitle.text = appAddress.nickname ?: context.getString(R.string.address)
            addressSubtitle.text = appAddress.completeAddress
        }
    }
}