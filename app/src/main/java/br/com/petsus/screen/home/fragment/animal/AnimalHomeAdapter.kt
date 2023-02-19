package br.com.petsus.screen.home.fragment.animal

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.petsus.api.model.animal.Animal
import br.com.petsus.databinding.CardHomeAnimalBinding
import br.com.petsus.util.base.adapter.BaseAdapter
import br.com.petsus.util.tool.inflater
import br.com.petsus.util.tool.pixel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class AnimalHomeAdapter : BaseAdapter<Animal, AnimalHomeAdapter.AnimalHomeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalHomeHolder {
        return AnimalHomeHolder(
            binding = CardHomeAnimalBinding.inflate(parent.inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AnimalHomeHolder, position: Int) {
        holder.load(elements[position])
    }

    inner class AnimalHomeHolder(
        val binding: CardHomeAnimalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun load(animal: Animal) {
            binding.nameHomeAnimal.text = animal.name
            binding.raceAndSpecieAnimal.text = String.format("${animal.race.name}/${animal.race.specie.name}")

            Glide.with(binding.imageHomeAnimal)
                .asDrawable()
                .load(animal.photo)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(RoundedCorners(16f.pixel), CenterCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageHomeAnimal)
                .waitForLayout()
                .clearOnDetach()

            binding.root.setOnClickListener { callOnClick(item = animal) }
        }
    }

}