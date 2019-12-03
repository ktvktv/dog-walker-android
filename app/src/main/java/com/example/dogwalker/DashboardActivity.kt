package com.example.dogwalker

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager.widget.ViewPager
import com.example.dogwalker.animation.PageTransformer
import com.example.dogwalker.view.DashboardFragment
import com.example.dogwalker.view.InfoFragment
import com.example.dogwalker.view.MapsFragment
import com.example.dogwalker.view.PostFragment
import com.example.dogwalker.viewmodel.InfoViewModel
import com.google.android.material.tabs.TabLayout

class DashboardActivity: AppCompatActivity() {

    private val NUM_PAGES = 3
    private val TAG = DashboardActivity::class.java.simpleName

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
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = NUM_PAGES

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> InfoFragment()
                1 -> DashboardFragment()
//                2 -> MapsFragment()
                else -> PostFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when(position) {
                0 -> "Information"
                1 -> "Dashboard"
//                2 -> "Tracker"
                else -> "Post"
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            InfoFragment.READ_WRITE_STORAGE_PERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                Log.d(TAG, "Request permission result for Info Fragment")

                //Read and write file to storage device
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    InfoFragment.uploadImageFirstTimePermission.value = true
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else -> {}
        }
    }
}