package br.com.petsus.screen.home.clinics.pager.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import br.com.petsus.api.model.clinic.Clinic

class ClinicInfoFragmentPager(
    private val viewModel: LiveData<Clinic>
) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observe(viewLifecycleOwner) { clinic ->

        }
    }

}