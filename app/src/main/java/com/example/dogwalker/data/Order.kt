package com.example.dogwalker.data

data class Order(
    val id: Int,
    val clientId: Int,
    val status: String,
    val walkDate: String,
    val price: Int,
    val duration: Int,
    val name: String,
    val address: String,
    val dogId: Int,
    val phoneNumber: String,
    val photo: String?
)

/*
package com.example.dogwalker.data

data class Order(
    val id: Int,
    val userId: Int,
    val walkerId: Int,
    val status: String,
    val walkDate: String,
    val price: Int,
    val duration: Int,
    val beforePhoto: String?,
    val afterPhoto: String?,
    val poopPhoto: String?
)
 */