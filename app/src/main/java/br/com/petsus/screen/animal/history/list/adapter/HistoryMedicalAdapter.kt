package br.com.petsus.screen.animal.history.list.adapter

import android.view.ViewGroup
import br.com.petsus.api.model.animal.HistoryMedical
import br.com.petsus.databinding.CardHistoryMedicalBinding
import br.com.petsus.util.base.adapter.BaseAdapter
import br.com.petsus.util.tool.inflater

class HistoryMedicalAdapter : BaseAdapter<HistoryMedical, HistoryMedicalHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryMedicalHolder =
        HistoryMedicalHolder(CardHistoryMedicalBinding.inflate(parent.inflater, parent, false))

    override fun onBindViewHolder(holder: HistoryMedicalHolder, position: Int) =
        holder.load(elements[position])
}