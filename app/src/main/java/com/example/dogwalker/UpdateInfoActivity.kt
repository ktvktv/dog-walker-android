package com.example.dogwalker

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.dogwalker.animation.PageTransformer
import com.example.dogwalker.view.*
import com.google.android.material.tabs.TabLayout

class UpdateInfoActivity : AppCompatActivity() {

    private val NUM_PAGES = 2

    private val TAG = UpdateInfoActivity::class.java.simpleName

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout_dashboard)
        tabLayout.tabMode = TabLayout.MODE_FIXED
        tabLayout.setTabTextColors(Color.GREEN, Color.BLACK)

        viewPager.adapter = ScreenSlidePagerAdapter(supportFragmentManager)
        viewPager.setPageTransformer(true, PageTransformer())

        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))

        //If user type is customer then there're 4 page, including order a walker
        tabLayout.getTabAt(0)?.setIcon(R.drawable.profile_picture)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.dog_profile)
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
        override fun getCount(): Int = NUM_PAGES

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> UserUpdateFragment()
                else -> DogUpdateFragment()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            READ_STORAGE_PERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                Log.d(TAG, "Request permission result for Info Fragment")

                //Read and write file to storage device
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Terimakasih telah memberi permisi. Silahkan pilih kembali foto yang diinginkan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Tidak diberikan ijin", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else -> {}
        }
    }
}
