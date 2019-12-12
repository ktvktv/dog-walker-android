package com.example.dogwalker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.data.Order

class OngoingOrderViewModel : ViewModel() {
    private val orderList = MutableLiveData<List<Order>>()

    init {
        orderList.value = listOf(
            Order(
                name = "Wijoyo",
                phone = "081293312313",
                status = "On Going",
                date = "18:00 09/09/1998 - 23:00 09/09/1998",
                address = "Jl. Barleria VI B1/H5"
            ),
            Order(
                name = "Wijoyo",
                phone = "081293312313",
                status = "On Going",
                date = "18:00 09/09/1998 - 23:00 09/09/1998",
                address = "Jl. Barleria VI B1/H5"
            )
        )
    }

    fun getOrderList() : List<Order> {
        if(orderList.value == null) {
            return listOf()
        }

        return orderList.value!!
    }
}