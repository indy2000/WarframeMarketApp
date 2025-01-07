package com.fukajima.warframerepo.repository

import android.content.Context
import com.fukajima.warframerepo.Remote
import com.fukajima.warframerepo.entity.Item
import com.fukajima.warframerepo.entity.ItemOrder
import com.fukajima.warframerepo.entity.Response
import io.reactivex.Maybe

class ItemRepository(val context: Context) {

     fun getItems(): Response<List<Item>> {
            return Remote(context).getItems()
     }

    fun getItemOrders(item_url_name: String): Response<List<ItemOrder>> {
        return Remote(context).getOrdersByItem(item_url_name)
    }
}