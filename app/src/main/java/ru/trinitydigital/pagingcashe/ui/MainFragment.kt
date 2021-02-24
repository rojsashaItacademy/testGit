package ru.trinitydigital.pagingcashe.ui

import android.os.Bundle
import ru.trinitydigital.pagingcashe.R
import ru.trinitydigital.pagingcashe.databinding.ItemRecipeBinding

class MainFragment : BaseFragment(R.layout.item_recipe) {
    var binding: ItemRecipeBinding? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding = ItemRecipeBinding.inflate(layoutInflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}