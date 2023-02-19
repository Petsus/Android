package br.com.petsus.util.base.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import br.com.petsus.R
import br.com.petsus.databinding.ActivityHomeBinding
import br.com.petsus.screen.home.clinics.ClinicFragment
import br.com.petsus.screen.home.fragment.animal.AnimalFragment
import br.com.petsus.screen.home.fragment.home.HomeFragment
import br.com.petsus.screen.home.fragment.ProfileFragment
import br.com.petsus.util.global.router.Navigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private val content: Map<Int, Class<out Fragment>> = mapOf(
        Pair(R.id.home_fragment_menu, HomeFragment::class.java),
        Pair(R.id.clinic_fragment_menu, ClinicFragment::class.java),
        Pair(R.id.animal_fragment_menu, AnimalFragment::class.java),
        Pair(R.id.profile_fragment_menu, ProfileFragment::class.java)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityHomeBinding.inflate(layoutInflater).apply {
            setContentView(root)
            val navigator = Navigator.of(homeContainer)

            content.keys.firstOrNull()?.let { id ->
                toggleHomeContainer.selectedItemId = id
                navigator.show(fragment = content.getValue(id).newInstance(), addToBack = false)
            }

            toggleHomeContainer.setOnItemReselectedListener { }
            toggleHomeContainer.setOnItemSelectedListener { menuItem ->
                val classFragment = content[menuItem.itemId] ?: return@setOnItemSelectedListener false
                navigator.show(fragment = classFragment.newInstance(), addToBack = false)
                return@setOnItemSelectedListener true
            }
        }
    }
}