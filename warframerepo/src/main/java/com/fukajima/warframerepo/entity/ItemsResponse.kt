package com.fukajima.warframerepo.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

class ItemsResponse {
    var payload: ItemsPayload? = null
}

class ItemsPayload {
    var items: List<Item> = mutableListOf()
}


@Entity(tableName = "Item")
class Item {
    @ColumnInfo
    var thumb: String? = null
    @ColumnInfo
    var item_name: String? = null
    @ColumnInfo
    var url_name: String? = null
    @PrimaryKey
    var id: String? = null

    fun getItemAssetUrl() : String? {
        return if(!this.thumb.isNullOrEmpty()) "https://warframe.market/static/assets/${this.thumb}" else null
    }
}

