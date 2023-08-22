package br.com.petsus.screen.about

import android.os.Bundle
import androidx.core.text.HtmlCompat
import br.com.petsus.databinding.ActivityAboutAppBinding
import br.com.petsus.util.base.activity.AppActivity
import br.com.petsus.util.base.viewmodel.appViewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutAppActivity : AppActivity() {

    private val viewModel: AboutAppViewModel by appViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityAboutAppBinding.inflate(layoutInflater).apply {
            setContentView(root)

            back.setOnClickListener { finish() }
            viewModel.about().observe(this@AboutAppActivity) { htmlString ->
                aboutApp.text = HtmlCompat.fromHtml(htmlString, HtmlCompat.FROM_HTML_MODE_COMPACT)
            }
        }
    }

}