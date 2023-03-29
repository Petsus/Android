package br.com.petsus.screen.address.add.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.petsus.api.model.address.Address
import br.com.petsus.screen.address.add.adapter.holder.SearchAddressHolder
import br.com.petsus.screen.address.add.adapter.holder.UserAddressHolder
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse

class ListAddressAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val userAddresses: MutableList<Address> = mutableListOf()
    private val searchAddresses: MutableList<FindAutocompletePredictionsResponse> = mutableListOf()

    var isSearch: Boolean =  false
        set(value) {
            field = value
        }

    override fun getItemViewType(position: Int): Int {
        return when {
            isSearch -> 1 /**** User Address ****/
            else -> 0 /**** Search Address ****/
        }
    }

    override fun getItemCount(): Int {
        return if (isSearch) searchAddresses.size + 1 else userAddresses.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when {
            isSearch -> SearchAddressHolder(TODO())
            else -> UserAddressHolder(TODO())
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SearchAddressHolder -> holder.load(if (position == 0) null else searchAddresses[position - 1])
            is UserAddressHolder -> holder.load(userAddresses[position])
        }
    }
}