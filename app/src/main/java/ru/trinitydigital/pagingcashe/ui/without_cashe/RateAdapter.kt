package ru.trinitydigital.pagingcashe.ui.without_cashe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.trinitydigital.pagingcashe.R
import ru.trinitydigital.pagingcashe.data.payloads.RatingPayloads
import ru.trinitydigital.pagingcashe.databinding.ItemRecipeBinding

class RateAdapter(private val listener: (pos: Int) -> Unit) :
    ListAdapter<RateData, RateViewHolder>(diffUtils) {

    //    private val list = arrayListOf<RateData>()
    private var lastSelectedPos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        return RateViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.bind(getItem(position), listener, position)
    }

    override fun onBindViewHolder(
        holder: RateViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val list = payloads as List<RatingPayloads>
            for (item in list) {
                when (item) {
                    RatingPayloads.LIKES -> {
                        holder.updateLikes(getItem(position))
                    }
                }
            }

        } else {
            super.onBindViewHolder(holder, position, payloads)
        }

    }

    companion object {
        val diffUtils = object : DiffUtil.ItemCallback<RateData>() {

            override fun areItemsTheSame(oldItem: RateData, newItem: RateData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: RateData, newItem: RateData): Boolean {
                return oldItem.isSelected == newItem.isSelected &&
                        oldItem.rate == newItem.rate
            }
        }
    }
}


class RateViewHolder(private val binding: ItemRecipeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(rateData: RateData, listener: (pos: Int) -> Unit, position: Int) {
        binding.likes.text = rateData.rate.toString()
        binding.imageLike.customClickListener {
            listener.invoke(position)
        }
        Picasso.get()
            .load("https://media.sproutsocial.com/uploads/2017/02/10x-featured-social-media-image-size.png")
            .into(binding.image)

//        binding.tvTitle.text = rateData.rate.toString()
//        binding.root.isSelected = rateData.isSelected
    }

    fun updateLikes(rateData: RateData) {
        binding.likes.text = rateData.rate.toString()
    }

    companion object {
        fun create(parent: ViewGroup): RateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recipe, parent, false)
            val binding = ItemRecipeBinding.bind(view)
            return RateViewHolder(binding)
        }
    }

}

data class RateData(
    var rate: Int,
    var isSelected: Boolean = false
)