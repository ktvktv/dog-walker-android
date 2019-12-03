package com.example.dogwalker.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.R
import com.example.dogwalker.data.User
import com.example.dogwalker.view.InfoFragment

class InfoViewModel() : ViewModel() {

    private val TAG = InfoViewModel::class.java.simpleName
    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user
}