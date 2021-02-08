package ru.trinitydigital.pagingcashe.ui.without_cashe

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.trinitydigital.pagingcashe.R
import ru.trinitydigital.pagingcashe.databinding.ItemLoadStatrFooterBinding

class GitHubLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<GitHubLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: GitHubLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): GitHubLoadStateViewHolder {
        Log.d("Asdasdasdasd", "adasdasdasdadasda")
        return GitHubLoadStateViewHolder.create(parent, retry)
    }
}


class GitHubLoadStateViewHolder(
    private val binding: ItemLoadStatrFooterBinding,
    private val retry: () -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        binding.btnRetry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.tvError.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.btnRetry.isVisible = loadState !is LoadState.Loading
        binding.tvError.isVisible = loadState !is LoadState.Loading

    }


    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): GitHubLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_load_statr_footer, parent, false)
            val binding = ItemLoadStatrFooterBinding.bind(view)
            return GitHubLoadStateViewHolder(binding, retry)
        }
    }

}