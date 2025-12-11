package com.fukajima.warframerepo.repository

import android.content.Context
import com.fukajima.warframerepo.Remote
import com.fukajima.warframerepo.entity.Item
import com.fukajima.warframerepo.entity.ItemDataV2
import com.fukajima.warframerepo.entity.ItemOrder
import com.fukajima.warframerepo.entity.Response
import io.reactivex.Maybe

class ItemRepository(val context: Context) {

    @Deprecated("API está retornando HTTP 404. Utilizar a v2.")
     fun getItems(): Response<List<Item>> {
            return Remote(context).getItems()
     }
    fun getItemsV2(): Response<List<ItemDataV2>> {
            return Remote(context).getItemsV2()
     }
}