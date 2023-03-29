package br.com.petsus.screen.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import br.com.petsus.R
import br.com.petsus.databinding.ActivityHomeBinding
import br.com.petsus.screen.home.fragment.clinics.ClinicFragment
import br.com.petsus.screen.home.fragment.animal.AnimalFragment
import br.com.petsus.screen.home.fragment.home.HomeFragment
import br.com.petsus.screen.home.fragment.profile.ProfileFragment
import br.com.petsus.util.base.activity.BaseActivity
import br.com.petsus.util.global.router.Navigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private var currentId: Int = 0

    private val content: Map<Int, Fragment> = mapOf(
        Pair(R.id.home_fragment_menu, HomeFragment()),
        Pair(R.id.clinic_fragment_menu, ClinicFragment()),
        Pair(R.id.animal_fragment_menu, AnimalFragment()),
        Pair(R.id.profile_fragment_menu, ProfileFragment())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityHomeBinding.inflate(layoutInflater).apply {
            setContentView(root)
            val navigator = Navigator.of(homeContainer)
            navigator.add(content.values.toList())

            content.keys.firstOrNull()?.let { id ->
                currentId = id
                toggleHomeContainer.selectedItemId = id
                navigator.show(tagShow = content.getValue(id).javaClass.name)
            }

            toggleHomeContainer.setOnItemReselectedListener { }
            toggleHomeContainer.setOnItemSelectedListener { menuItem ->
                val classFragment = content[menuItem.itemId] ?: return@setOnItemSelectedListener false
                val lastFragment = content[currentId] ?: return@setOnItemSelectedListener false

                navigator.show(
                    tagShow = classFragment::class.java.name,
                    tagHide = lastFragment::class.java.name
                )

                currentId = menuItem.itemId
                return@setOnItemSelectedListener true
            }
        }
    }
}