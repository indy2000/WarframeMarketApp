package com.fukajima.warframerepo.repository

import android.content.Context
import com.fukajima.warframerepo.Remote
import com.fukajima.warframerepo.entity.ItemOrder
import com.fukajima.warframerepo.entity.Response

class ItemOrderRepository(val context: Context) {
    fun getItemOrders(item_url_name: String): Response<List<ItemOrder>> {
        return Remote(context).getOrdersByItem(item_url_name)
    }
}