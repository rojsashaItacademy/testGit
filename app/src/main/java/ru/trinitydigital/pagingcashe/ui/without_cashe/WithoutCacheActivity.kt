package ru.trinitydigital.pagingcashe.ui.without_cashe

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.trinitydigital.pagingcashe.databinding.ActivityWithoutCacheBinding
import ru.trinitydigital.pagingcashe.ui.main.MainAdapter
import ru.trinitydigital.pagingcashe.utils.hideKeyboard

class WithoutCacheActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWithoutCacheBinding
    private val vm by viewModel<WithoutCacheViewModel>()
    private val adapter by lazy { MainAdapter() }
    private var jobSearch: Job? = null

    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWithoutCacheBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("asdasdasd", "asdasdasdsad")
        Log.d("asdasdasd", "asdasdasdsad")
        Log.d("asdasdasd", "asdasdasdsad")
        Log.d("asdasdasd", "asdasdasdsad")
        Log.d("asdasdasd", "asdasdasdsad")
        Log.d("asdasdasd", "asdasdasdsad")
        Log.d("asdasdasd", "asdasdasdsad")
        Log.d("Asdasdasdasd", "adasdasdasdadasda")
        setupRecycler()
        setupListener()
    }

    private fun setupRecycler() {
        binding.recycler.adapter = adapter.withLoadStateHeaderAndFooter(
            header = GitHubLoadStateAdapter { adapter.retry() },
            footer = GitHubLoadStateAdapter { adapter.retry() }
        )
    }

    @ExperimentalPagingApi
    private fun setupListener() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                updateSearchRepo()
                hideKeyboard()
                true
            } else
                false
        }

        adapter.addLoadStateListener { loadState ->
            binding.recycler.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.btnRetry.isVisible = loadState.source.refresh is LoadState.Error
        }

        binding.btnRetry.setOnClickListener { adapter.retry() }
    }

    @ExperimentalPagingApi
    fun updateSearchRepo() {
        binding.etSearch.text.trim().let {
            if (it.isNotEmpty()) search(it.toString())
        }
    }

    @ExperimentalPagingApi
    private fun search(query: String) {
        jobSearch?.cancel()
        jobSearch = lifecycleScope.launch {
            vm.getPagingData(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }
}