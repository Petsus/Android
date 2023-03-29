package br.com.petsus.screen.address.list.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.petsus.R
import br.com.petsus.api.model.address.Address
import br.com.petsus.databinding.CardAddressBinding
import br.com.petsus.util.base.adapter.BaseAdapter
import br.com.petsus.util.tool.context
import br.com.petsus.util.tool.inflater

class ListAddressAdapter : BaseAdapter<Address, ListAddressAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(CardAddressBinding.inflate(parent.inflater, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.configure(elements[position])
    }

    inner class Holder(val binding: CardAddressBinding) : RecyclerView.ViewHolder(binding.root) {

        fun configure(address: Address) {
            binding.apply {
                root.setOnClickListener { callOnClick(item = address) }
                addressTitle.text = address.nickname ?: context.getString(R.string.address)
                addressSubtitle.text = address.completeAddress
            }
        }

    }

}