package br.com.petsus.screen.animal.update.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.petsus.api.model.animal.HistoryMedical
import br.com.petsus.databinding.CardSimpleHistoryMedicalBinding
import br.com.petsus.util.base.adapter.BaseAdapter
import br.com.petsus.util.tool.format
import br.com.petsus.util.tool.inflater
import br.com.petsus.util.tool.toDate

class SimpleHistoryMedical : BaseAdapter<HistoryMedical, SimpleHistoryMedical.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(binding = CardSimpleHistoryMedicalBinding.inflate(parent.inflater, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) =
        holder.load(elements[position])

    class Holder(val binding: CardSimpleHistoryMedicalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun load(historyMedical: HistoryMedical) {
            binding.apply {
                titleSimpleHistory.text = historyMedical.name
                subtitleSimpleHistory.text = historyMedical.createdAt.toDate()?.format(pattern = "dd/MM/yyyy hh:mm")
            }
        }
    }

}