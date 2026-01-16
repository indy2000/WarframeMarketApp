package com.fukajima.warframerepo.entity

data class UserItemOrderResponse (
    var apiVersion: String,
    var data: List<ItemData>? = null,
    var error: String?
)

data class ItemData(
    var id: String,
    var type: String,
    var platinum: Int,
    var quantity: Int,
    var perTrade: Int,
    var visible: Boolean,
    var createdAt: String,
    var updatedAt: String,
    var itemId: String,
    var item_name: String?,
    var itemImage: String?
)