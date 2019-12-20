package com.example.dogwalker.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(InfoViewModel::class.java)) return InfoViewModel() as T
        if(modelClass.isAssignableFrom(PostViewModel::class.java)) return PostViewModel() as T
        if(modelClass.isAssignableFrom(OngoingOrderViewModel::class.java)) return OngoingOrderViewModel() as T
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)) return LoginViewModel() as T
        if(modelClass.isAssignableFrom(RegisterViewModel::class.java)) return RegisterViewModel() as T
        if(modelClass.isAssignableFrom(RegisterDogViewModel::class.java)) return RegisterDogViewModel() as T
        if(modelClass.isAssignableFrom(SinglePostViewModel::class.java)) return SinglePostViewModel() as T

        throw IllegalArgumentException("Unknown view model class")
    }
}