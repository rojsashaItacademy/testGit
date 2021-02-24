package ru.trinitydigital.pagingcashe.ui.without_cashe

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.trinitydigital.pagingcashe.data.model.TestListItem
import ru.trinitydigital.pagingcashe.data.payloads.RatingPayloads
import ru.trinitydigital.pagingcashe.databinding.ActivityWithoutCacheBinding
import ru.trinitydigital.pagingcashe.ui.main.MainAdapter
import ru.trinitydigital.pagingcashe.utils.hideKeyboard

const val TOTAL_TIME = 100_000L
const val STEP_TIME = 1_000L

class WithoutCacheActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWithoutCacheBinding
    private val vm by viewModel<WithoutCacheViewModel>()
    private val adapter by lazy { MainAdapter() }
//    private val rateAdapter by lazy {
//        RateAdapter() { pos ->
//            vm.getLikes(pos)
//        }
//    }

    private val flexAdapter by lazy {
        FlexAdapter() { _ ->
        }
    }

    private var jobSearch: Job? = null

    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWithoutCacheBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecycler()
        setupListener()
        timer.start()
        vm.listData.observe(this, {
            flexAdapter.submitList(it)
        })
        vm.eventUpdateLikes.observe(this, {
            flexAdapter.notifyItemChanged(it, RatingPayloads.LIKES)
        })

        binding.btnRefreshList.setOnClickListener {
            vm.list[0].rate = 101
            flexAdapter.notifyDataSetChanged()
        }
        vm.testLiveData.observe(this, {

            Log.d("adasdsad", "Adasdasdasd")
        })

        vm.loadCounties()

    }


    val timer = object : CountDownTimer(TOTAL_TIME, STEP_TIME) {

        override fun onTick(millisUntilFinished: Long) {

            val percent: Int = 100 - (millisUntilFinished / 1000).toInt()
            Log.d("_______", percent.toString())
            binding.progress.progress = percent
        }

        override fun onFinish() {
            binding.progress.progress = 100
        }

    }

    private fun setupRecycler() {
        binding.recycler.adapter = adapter.withLoadStateHeaderAndFooter(
            header = GitHubLoadStateAdapter { adapter.retry() },
            footer = GitHubLoadStateAdapter { adapter.retry() }
        )

        val layoutManager = FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP)
        layoutManager.justifyContent = JustifyContent.FLEX_START
        binding.rvRate.layoutManager = layoutManager
        binding.rvRate.adapter = flexAdapter
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