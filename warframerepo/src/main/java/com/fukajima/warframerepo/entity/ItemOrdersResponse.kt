package com.fukajima.warframerepo.entity

class ItemOrdersResponse {
    var payload: ItemOrderPayload? = null
}

class ItemOrderPayload {
    var orders: List<ItemOrder> = mutableListOf()
}

class ItemOrder {
    var platform: String? = null
    var creation_date: String? = null
    var quantity: Int? = null
    var platinum: Int? = null
    var user: ItemOrderUser? = null
    var order_type: String? = null
    var last_update: String? = null
    var visible: Boolean = false
    var id: String? = null
    var region: String? = null
}

class ItemOrderUser {
    var reputation: Int? = null
    var locale: String? = null
    var avatar: String? = null
    var ingame_name: String? = null
    var last_seen: String? = null
    var id: String? = null
    var region: String? = null
    var status: String? = null

    fun getAvatarAssetUrl() : String? {
        return if(!this.avatar.isNullOrEmpty()) "https://warframe.market/static/assets/${this.avatar}" else null
    }
}