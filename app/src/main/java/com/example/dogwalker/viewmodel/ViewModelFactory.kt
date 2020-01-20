package com.example.dogwalker.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(InfoViewModel::class.java)) return InfoViewModel() as T
        if(modelClass.isAssignableFrom(PostViewModel::class.java)) return PostViewModel() as T
        if(modelClass.isAssignableFrom(OngoingOrderViewModel::class.java)) return OngoingOrderViewModel() as T
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)) return LoginViewModel() as T
        if(modelClass.isAssignableFrom(RegisterViewModel::class.java)) return RegisterViewModel() as T
        if(modelClass.isAssignableFrom(RegisterDogViewModel::class.java)) return RegisterDogViewModel() as T
        if(modelClass.isAssignableFrom(SinglePostViewModel::class.java)) return SinglePostViewModel() as T
        if(modelClass.isAssignableFrom(RegisterWalkerViewModel::class.java)) return RegisterWalkerViewModel() as T
        if(modelClass.isAssignableFrom(WalkerInfoViewModel::class.java)) return WalkerInfoViewModel() as T
        if(modelClass.isAssignableFrom(WalkerOrderViewModel::class.java)) return WalkerOrderViewModel() as T
        if(modelClass.isAssignableFrom(OrderDetailViewModel::class.java)) return OrderDetailViewModel() as T
        if(modelClass.isAssignableFrom(ListOrderViewModel::class.java)) return ListOrderViewModel() as T
        if(modelClass.isAssignableFrom(OrderDecisionViewModel::class.java)) return OrderDecisionViewModel() as T
        if(modelClass.isAssignableFrom(DashboardViewModel::class.java)) return DashboardViewModel() as T
        if(modelClass.isAssignableFrom(UserUpdateViewModel::class.java)) return UserUpdateViewModel() as T
        if(modelClass.isAssignableFrom(DogUpdateViewModel::class.java)) return DogUpdateViewModel() as T
        if(modelClass.isAssignableFrom(NewPostViewModel::class.java)) return NewPostViewModel() as T
        if(modelClass.isAssignableFrom(WalkerUpdateViewModel::class.java)) return WalkerUpdateViewModel() as T

        throw IllegalArgumentException("Unknown view model class")
    }
}