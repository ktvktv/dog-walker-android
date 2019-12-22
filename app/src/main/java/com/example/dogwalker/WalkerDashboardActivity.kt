package com.example.dogwalker

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.dogwalker.animation.PageTransformer
import com.example.dogwalker.view.InfoFragment
import com.example.dogwalker.view.OngoingOrderFragment
import com.example.dogwalker.view.PostFragment
import com.example.dogwalker.view.WalkerInfoFragment
import com.google.android.material.tabs.TabLayout

class WalkerDashboardActivity : AppCompatActivity() {

    private val NUM_PAGES = 3
    private val TAG = WalkerDashboardActivity::class.java.simpleName
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout_dashboard)
        tabLayout.tabMode = TabLayout.MODE_FIXED
        tabLayout.setTabTextColors(Color.GREEN, Color.BLACK)

        viewPager.adapter = ScreenSlidePagerAdapter(supportFragmentManager)
        viewPager.setPageTransformer(true, PageTransformer())
        viewPager.setCurrentItem(1)

        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))

        tabLayout.getTabAt(0)?.setIcon(R.drawable.man_user)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.order)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.post_it)

        with(
            getSharedPreferences(getString(R.string.session_cache), Context.MODE_PRIVATE)
                .edit()
        ) {
            putString(getString(R.string.session_cache), "Walker")
            apply()
        }

        //TODO:Call API to update session
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
        override fun getCount(): Int = NUM_PAGES

        override fun getItem(position: Int): Fragment {
            val walkerInfo = WalkerInfoFragment()
            walkerInfo.arguments = Bundle()
            walkerInfo.arguments!!.putInt("walkerId", -1)

            return when(position) {
                0 -> walkerInfo
                1 -> OngoingOrderFragment()
                else -> PostFragment()
            }
        }
    }
}