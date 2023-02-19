package br.com.petsus.screen.home.fragment.home

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isInvisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.petsus.databinding.FragmentHomeBinding
import br.com.petsus.screen.home.fragment.home.adpter.NewsAdapter
import br.com.petsus.util.base.fragment.BaseFragment
import br.com.petsus.util.global.ResultState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by activityViewModels()

    private val adapter: NewsAdapter = NewsAdapter().apply {
        addClickListener { news ->
            context?.apply {
                CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .build()
                    .launchUrl(this, Uri.parse(news.url))
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.news().observe(this) { items ->
            when (items) {
                is ResultState.Init -> {
                    binding?.loadingNews?.show()
                    binding?.listNews?.isInvisible = true
                }
                is ResultState.Success -> {
                    binding?.loadingNews?.hide()
                    binding?.listNews?.isInvisible = false
                    adapter.update(items.data)
                }
                is ResultState.Fail -> {
                    binding?.loadingNews?.hide()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            listNews.adapter = adapter
            listNews.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

}