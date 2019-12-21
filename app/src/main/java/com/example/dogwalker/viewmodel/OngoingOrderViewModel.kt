package com.example.dogwalker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.data.Order

class OngoingOrderViewModel : ViewModel() {
    private val orderList = MutableLiveData<List<Order>>()

    init {
        orderList.value = listOf(
            Order(
                name = "Kevin",
                phone = "081290001998",
                status = "Pending",
                date = "18:00 - 23:00 12/09/12",
                address = "Jl. Barleria VI B1/H5",
                userImageUrl = "https://cbbinus.files.wordpress.com/2017/05/p_20170504_092501_bf1.jpg?w=700"
            ),
            Order(
                name = "Wijoyo",
                phone = "081293312313",
                status = "On Going",
                date = "18:00 09/09/1998 - 23:00 09/09/1998",
                address = "Jl. Barleria VI B1/H5",
                userImageUrl = ""
            ),
            Order(
                name = "Wijoyo",
                phone = "081293312313",
                status = "On Going",
                date = "18:00 09/09/1998 - 23:00 09/09/1998",
                address = "Jl. Barleria VI B1/H5",
                userImageUrl = ""
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