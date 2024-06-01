package com.example.fitgether.presentation.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.viewpager2.widget.ViewPager2
import com.example.fitgether.R
import com.example.fitgether.presentation.onboarding.screens.FirstScreen
import com.example.fitgether.presentation.onboarding.screens.SecondScreen
import com.example.fitgether.presentation.onboarding.screens.ThirdScreen
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //onboarding fragmentinin full screen olması için setFlag methodu kullanıldı
        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        val view =  inflater.inflate(R.layout.fragment_onboarding, container, false)

        val fragments : Array<Fragment> = arrayOf(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )

        val dotsIndicator = view.findViewById<DotsIndicator>(R.id.dots_indicator)
        val adapter = ViewPagerAdapter(fragments, requireActivity().supportFragmentManager, lifecycle)

        val pager = view.findViewById<ViewPager2>(R.id.viewPager)
        pager.adapter = adapter
        dotsIndicator.attachTo(pager)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }


}