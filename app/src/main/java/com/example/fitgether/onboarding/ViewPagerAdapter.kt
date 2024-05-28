package com.example.fitgether.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentList : Array<Fragment>, fm : FragmentManager, lifecycle : Lifecycle) : FragmentStateAdapter(fm , lifecycle) {

    private val fragments = fragmentList

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}