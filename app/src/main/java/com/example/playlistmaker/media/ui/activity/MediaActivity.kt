package com.example.playlistmaker.media.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediatekaBinding
import com.example.playlistmaker.media.ui.FragmentsAdapter
import com.example.playlistmaker.media.ui.PageSelector
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MediaActivity : AppCompatActivity(), PageSelector {
    private lateinit var tabMediator: TabLayoutMediator
    private lateinit var binding: ActivityMediatekaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediatekaBinding.inflate(layoutInflater)
        binding.backButtonMediaActivity.setOnClickListener {
            finish()
        }
        setContentView(binding.root)
        val adapter = FragmentsAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter


        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favourite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
        binding.tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    val currentPosition = tab.position
                    binding.viewPager.currentItem = currentPosition
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            }
        )

    }

    override fun NavigateTo(page: Int) {
        binding.viewPager.currentItem = page
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}