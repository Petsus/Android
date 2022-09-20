package br.com.petsus.util.base.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import br.com.petsus.R
import br.com.petsus.databinding.ActivityHomeBinding
import br.com.petsus.screen.home.ClinicFragment
import br.com.petsus.screen.home.fragment.HomeFragment
import br.com.petsus.screen.home.fragment.ProfileFragment

class HomeActivity : BaseActivity() {

    private val content: Map<Int, Class<out Fragment>> = mapOf(
        Pair(R.id.home_fragment_menu, HomeFragment::class.java),
        Pair(R.id.clinic_fragment_menu, ClinicFragment::class.java),
        Pair(R.id.profile_fragment_menu, ProfileFragment::class.java)
    )

    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        content.keys.firstOrNull()?.let { id ->
            binding.toggleHomeContainer.selectedItemId = id
            replace(content.getValue(id))
        }

        binding.toggleHomeContainer.setOnItemReselectedListener { }
        binding.toggleHomeContainer.setOnItemSelectedListener { menuItem ->
            val classFragment = content[menuItem.itemId] ?: return@setOnItemSelectedListener false

            replace(classFragment)
            return@setOnItemSelectedListener true
        }
    }

    private fun replace(fragment: Class<out Fragment>) {
        supportFragmentManager.beginTransaction()
            .replace(binding.homeContainer.id, fragment.newInstance())
            .commit()
    }

}