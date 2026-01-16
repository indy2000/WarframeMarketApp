package com.fukajima.warframerepo.repository

import android.content.Context
import com.fukajima.warframerepo.Remote
import com.fukajima.warframerepo.entity.Item
import com.fukajima.warframerepo.entity.ItemDataV2
import com.fukajima.warframerepo.entity.ItemOrder
import com.fukajima.warframerepo.entity.Response
import io.reactivex.Maybe

class ItemRepository(val context: Context) {

    var itemDao = ItemDataBase.getDataBase(context).itemDao()

    @Deprecated("API está retornando HTTP 404. Utilizar a v2.")
     fun getItems(): Response<List<Item>> {
            return Remote(context).getItems()
     }
    fun getItemsV2(): Response<List<ItemDataV2>> {
            return Remote(context).getItemsV2()
     }

    fun insertItemOnDataBase(item:List<Item>){
        itemDao.insertAllItems(item)
    }

    fun deleteAll(){
        itemDao.deleteAll()
    }

    fun getItemById(id: String): Item{
        return itemDao.getItemById(id)
    }

}