package com.fukajima.warframerepo.entity

data class UserItemOrderResponse (
    val apiVersion: String,
    val data: List<ItemData>? = null,
    val error: String?
)

data class ItemData(
    val id: String,
    val type: String,
    val platinum: Int,
    val quantity: Int,
    val perTrade: Int,
    val visible: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val itemId: String
)