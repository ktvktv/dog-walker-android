package com.example.dogwalker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.dogwalker.animation.PageTransformer
import com.example.dogwalker.view.LoginFragment
import com.example.dogwalker.view.RegisterFragment

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val type = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.type_cache), "")

        Log.d(TAG, type)

        if(type != "") {
            var intent = Intent()
            if (type.toLowerCase() == "customer") {
                intent = Intent(this, DashboardActivity::class.java)
            } else if (type.toLowerCase() == "walker") {
                intent = Intent(this, WalkerDashboardActivity::class.java)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}
