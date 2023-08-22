package br.com.petsus.screen.animal.history.add.dialog

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.petsus.databinding.CardSearchFieldBinding
import br.com.petsus.util.base.adapter.BaseAdapter
import br.com.petsus.util.tool.inflater

class SimpleAdapter<T> : BaseAdapter<Pair<String, T>, SimpleAdapter<T>.SimpleHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder =
        SimpleHolder(CardSearchFieldBinding.inflate(parent.inflater, parent, false))

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        holder.load(elements[position])
    }

    inner class SimpleHolder(val binding: CardSearchFieldBinding) : RecyclerView.ViewHolder(binding.root) {
        fun load(item: Pair<String, T>) {
            binding.root.text = item.first
            binding.root.setOnClickListener { callOnClick(item) }
        }
    }
}