package com.frogobox.kickstart.mvvm.main

import android.os.Bundle
import com.frogobox.kickstart.R
import com.frogobox.kickstart.core.BaseActivity
import com.frogobox.kickstart.databinding.ActivityMainBinding
import com.frogobox.kickstart.mvvm.consumable.ConsumableFragment
import com.frogobox.kickstart.mvvm.favorite.VideoFragment

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun setupViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun setupViewModel() {}

    override fun onCreateExt(savedInstanceState: Bundle?) {
        super.onCreateExt(savedInstanceState)
        setupToolbar()
        setupBottomNav(binding.framelayoutMainContainer.id)
        setupFragment(savedInstanceState)
    }

    private fun setupToolbar() {
        supportActionBar?.elevation = 0f
    }

    private fun setupFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            binding.bottomNavMainMenu.selectedItemId = R.id.bottom_menu_main
        }
    }

    private fun setupBottomNav(frameLayout: Int) {
        binding.bottomNavMainMenu.apply {
            clearAnimation()
            itemIconTintList = null

            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.bottom_menu_consumable -> {
                        supportActionBar?.title = "Dashboard"
                        setupChildFragment(
                            frameLayout,
                            ConsumableFragment()
                        )
                    }

                    R.id.bottom_menu_favorite -> {
                        supportActionBar?.title = "News"
                        setupChildFragment(
                            frameLayout,
                            MainFragment()
                        )
                    }

                    R.id.bottom_menu_main -> {
                        supportActionBar?.title = getString(R.string.title_main)
                        setupChildFragment(
                            frameLayout,
                            VideoFragment()
                        )
                    }
                }

                true
            }
        }

    }

}
