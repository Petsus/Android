package br.com.petsus.screen.address.adapter

import android.view.ViewGroup
import br.com.petsus.api.model.address.AppAddress
import br.com.petsus.databinding.CardAddressBinding
import br.com.petsus.screen.address.adapter.holder.UserAddressHolder
import br.com.petsus.util.base.adapter.BaseAdapter
import br.com.petsus.util.global.Action
import br.com.petsus.util.tool.inflater

class ListAddressAdapter : BaseAdapter<AppAddress, UserAddressHolder>() {
    private val addressAction: Action<AppAddress> = Action { appAddress -> callOnClick(appAddress) }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAddressHolder =
        UserAddressHolder(binding = CardAddressBinding.inflate(parent.inflater, parent, false), callOnClick = addressAction)

    override fun onBindViewHolder(holder: UserAddressHolder, position: Int) =
        holder.configure(elements[position])
}