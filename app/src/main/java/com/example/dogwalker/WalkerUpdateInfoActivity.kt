package com.example.dogwalker

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.dogwalker.databinding.ActivityWalkerRegisterBinding
import com.example.dogwalker.viewmodel.ViewModelFactory
import com.example.dogwalker.viewmodel.WalkerUpdateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class WalkerUpdateInfoActivity : AppCompatActivity() {

    private val walkerUpdateInfoViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(WalkerUpdateViewModel::class.java)
    }
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val walkerUpdateBinding = ActivityWalkerRegisterBinding.inflate(LayoutInflater.from(this))

        walkerUpdateBinding.walkerRegisterTitle.text = "Update Walker"
        walkerUpdateBinding.registerButton.text = "Ubah"

        walkerUpdateBinding.registerButton.setOnClickListener {

        }

        setContentView(walkerUpdateBinding.root)
    }
}