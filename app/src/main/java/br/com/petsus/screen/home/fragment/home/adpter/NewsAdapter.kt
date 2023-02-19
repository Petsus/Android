package br.com.petsus.screen.home.fragment.home.adpter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.petsus.api.model.others.News
import br.com.petsus.databinding.CardNewsBinding
import br.com.petsus.util.base.adapter.BaseAdapter
import br.com.petsus.util.tool.dp
import br.com.petsus.util.tool.inflater
import br.com.petsus.util.tool.pixel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class NewsAdapter : BaseAdapter<News, NewsAdapter.NewsHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder =
        NewsHolder(CardNewsBinding.inflate(parent.inflater, parent, false))

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        holder.load(elements[position])
    }

    inner class NewsHolder(val binding: CardNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun load(news: News) {
            binding.titleNews.text = news.name
            Glide.with(binding.imageNews)
                .asDrawable()
                .load(news.img)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(RoundedCorners(16f.pixel), CenterCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageNews)
                .waitForLayout()
                .clearOnDetach()
            binding.root.setOnClickListener { callOnClick(item = news) }
        }
    }
}