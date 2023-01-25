package br.com.petsus.screen.home.clinics.pager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.petsus.api.model.clinic.Clinic
import br.com.petsus.screen.home.clinics.ClinicViewModel
import br.com.petsus.screen.home.clinics.pager.view.ClinicExamFragmentPager
import br.com.petsus.screen.home.clinics.pager.view.ClinicInfoFragmentPager
import br.com.petsus.screen.home.clinics.pager.view.ClinicSpecieFragmentPager

class ClinicPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val viewModel: LiveData<Clinic>,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments = listOf(
        ClinicInfoFragmentPager::class.java,
        ClinicSpecieFragmentPager::class.java,
        ClinicExamFragmentPager::class.java
    )

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when {
            position <= fragments.lastIndex -> fragments[position].getConstructor(LiveData::class.java).newInstance(viewModel)
            else -> throw NotImplementedError()
        }
    }

}