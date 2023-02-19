package br.com.petsus.screen.home.clinics.pager

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import br.com.petsus.api.model.clinic.Clinic
import br.com.petsus.databinding.FragmentClinicInfoBinding

class ClinicInfo(
    private val clinic: Clinic,
) : Fragment() {

    private lateinit var binding: FragmentClinicInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentClinicInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            this.clinic = this@ClinicInfo.clinic
            this.goToSite.setOnClickListener {
                context?.apply {
                    CustomTabsIntent.Builder()
                        .setShowTitle(true)
                        .build()
                        .launchUrl(this, Uri.parse(this@ClinicInfo.clinic.site))
                }
            }
            this.goToCall.setOnClickListener {
                startActivity(
                    Intent(Intent.ACTION_DIAL)
                        .setData(Uri.parse("tel:${this@ClinicInfo.clinic.phone}"))
                )
            }
        }
    }

}