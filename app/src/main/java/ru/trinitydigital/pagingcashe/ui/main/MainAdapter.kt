package ru.trinitydigital.pagingcashe.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.trinitydigital.pagingcashe.R
import ru.trinitydigital.pagingcashe.data.model.RowsModel

class MainAdapter : PagingDataAdapter<RowsModel, MainViewHolder>(ROWSMODEL_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        private val ROWSMODEL_COMPARATOR = object : DiffUtil.ItemCallback<RowsModel>() {
            override fun areItemsTheSame(oldItem: RowsModel, newItem: RowsModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RowsModel, newItem: RowsModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun create(parent: ViewGroup): MainViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_rc, parent, false)
            return MainViewHolder(view)
        }
    }

    private val tvTitle = view.findViewById<TextView>(R.id.tvTitle)

    fun bind(item: RowsModel?) {
        tvTitle.text = item?.full_name
    }


}