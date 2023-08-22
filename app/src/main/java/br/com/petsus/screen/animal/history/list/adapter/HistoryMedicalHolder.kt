package br.com.petsus.screen.animal.history.list.adapter

import androidx.recyclerview.widget.RecyclerView
import br.com.petsus.api.model.animal.HistoryMedical
import br.com.petsus.databinding.CardHistoryMedicalBinding
import br.com.petsus.util.tool.format
import br.com.petsus.util.tool.toDate

class HistoryMedicalHolder(val binding: CardHistoryMedicalBinding) : RecyclerView.ViewHolder(binding.root) {
    fun load(historyMedical: HistoryMedical) {
        binding.nameHistoryMedical.text = historyMedical.name
        binding.infoHistoryMedical.text = historyMedical.veterinary
        binding.dataHistoryMedical.text = historyMedical.createdAt.toDate()?.format()
    }
}