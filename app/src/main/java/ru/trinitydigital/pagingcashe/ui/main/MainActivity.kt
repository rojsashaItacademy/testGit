package ru.trinitydigital.pagingcashe.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.paging.ExperimentalPagingApi
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.trinitydigital.pagingcashe.R
import ru.trinitydigital.pagingcashe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val vm by viewModel<MainViewModel>()
    private val adapter by lazy { MainAdapter() }

    private lateinit var binding: ActivityMainBinding

    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recycler.adapter = adapter
        setupViewModel()
    }

    @ExperimentalPagingApi
    private fun setupViewModel() {
        vm.getPagingData().observe(this, {
            adapter.submitData(lifecycle, it)
        })
//        adapter.submitData()
    }
}