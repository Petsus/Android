package br.com.petsus.screen.about

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.text.HtmlCompat
import br.com.petsus.databinding.ActivityAboutAppBinding
import br.com.petsus.util.base.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutAppActivity : BaseActivity() {

    private val viewModel: AboutAppViewModel by viewModels()

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