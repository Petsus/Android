package br.com.petsus.screen.address.adapter

import android.view.ViewGroup
import br.com.petsus.databinding.CardAddressBinding
import br.com.petsus.screen.address.adapter.holder.SearchAddressHolder
import br.com.petsus.util.base.adapter.BaseAdapter
import br.com.petsus.util.global.Action
import br.com.petsus.util.tool.inflater
import com.google.android.libraries.places.api.model.AutocompletePrediction

class ListSearchAdapter : BaseAdapter<AutocompletePrediction, SearchAddressHolder>() {
    private val addressAction: Action<AutocompletePrediction> = Action { appAddress -> callOnClick(appAddress) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAddressHolder =
        SearchAddressHolder(CardAddressBinding.inflate(parent.inflater, parent, false), callOnClick = addressAction)

    override fun onBindViewHolder(holder: SearchAddressHolder, position: Int) =
        holder.load(elements[position])
}