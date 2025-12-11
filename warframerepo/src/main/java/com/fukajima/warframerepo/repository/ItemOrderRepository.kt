package com.fukajima.warframerepo.repository

import android.content.Context
import com.fukajima.warframerepo.Remote
import com.fukajima.warframerepo.entity.ItemData
import com.fukajima.warframerepo.entity.ItemOrder
import com.fukajima.warframerepo.entity.PlaceOrderRequest
import com.fukajima.warframerepo.entity.Response
import com.fukajima.warframerepo.entity.ResponseGeneric

class ItemOrderRepository(val context: Context) {
    fun getItemOrders(item_url_name: String): Response<List<ItemOrder>> {
        return Remote(context).getOrdersByItem(item_url_name)
    }

    fun setItemOrder(order: PlaceOrderRequest, jwt: String): ResponseGeneric {
        return Remote(context).setItemOrderV2(order, jwt)
    }

    fun getItemOrderSignInUser(jwt: String): Response<List<ItemData>>{
        return Remote(context).getOrderBySignInUser(jwt)
    }
}