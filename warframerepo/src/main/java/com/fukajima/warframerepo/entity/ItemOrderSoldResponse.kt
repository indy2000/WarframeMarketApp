package com.fukajima.warframerepo.entity


    data class ItemOrderSoldResponse(
        val apiVersion: String,
        val data: ItemOrderSoldData,
        val error: ItemOrderSoldError?
    )

    data class ItemOrderSoldError(
        var request: List<String>
    )

    data class ItemOrderSoldData(
        val id: String,
        val type: String,
        val originId: String,
        val platinum: Int,
        val quantity: Int,
        val createdAt: String,
        val updatedAt: String,
        val item: ItemOrderSold
    )

    data class ItemOrderSold(
        val id: String
    )

