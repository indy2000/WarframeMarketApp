package com.fukajima.warframerepo.entity


    data class ItemOrderEditResponse(
        val apiVersion: String,
        val data: OrderData,
        val error: ItemOrderEditError?
    )

    data class ItemOrderEditError(
        var request: List<String>
    )

    data class OrderData(
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
