package br.com.petsus.screen.address.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import br.com.petsus.R
import br.com.petsus.databinding.CardAddressBinding
import br.com.petsus.util.global.Action
import br.com.petsus.util.tool.context
import com.google.android.libraries.places.api.model.AutocompletePrediction

class SearchAddressHolder(
    private val binding: CardAddressBinding,
    private val callOnClick: Action<AutocompletePrediction>,
) : RecyclerView.ViewHolder(binding.root) {
    fun load(places: AutocompletePrediction) {
        binding.apply {
            root.setOnClickListener { callOnClick.action(data = places) }
            addressTitle.text = places.getPrimaryText(null) ?: context.getString(R.string.address)
            addressSubtitle.text =places.getSecondaryText(null)
        }
    }
}