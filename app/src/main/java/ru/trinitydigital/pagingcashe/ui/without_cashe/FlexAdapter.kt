package ru.trinitydigital.pagingcashe.ui.without_cashe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.trinitydigital.pagingcashe.R
import ru.trinitydigital.pagingcashe.data.payloads.RatingPayloads
import ru.trinitydigital.pagingcashe.databinding.ItemFlexBinding
import ru.trinitydigital.pagingcashe.databinding.ItemRecipeBinding

class FlexAdapter(private val listener: (pos: Int) -> Unit) :
    ListAdapter<RateData, FlexViewHolder>(diffUtils) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlexViewHolder {
        return FlexViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FlexViewHolder, position: Int) {
        holder.bind(getItem(position), listener, position)
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


class FlexViewHolder(private val binding: ItemFlexBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(rateData: RateData, listener: (pos: Int) -> Unit, position: Int) {
        binding.tvTitle.text = rateData.rate.toString()
    }

    companion object {
        fun create(parent: ViewGroup): FlexViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_flex, parent, false)
            val binding = ItemFlexBinding.bind(view)
            return FlexViewHolder(binding)
        }
    }

}