package com.fukajima.warframerepo.entity

class ItemOrdersResponseV2 {
    var apiVersion: String? = null
    var data: List<ItemOrderV2>? = null
    var error: String? = null
}

class ItemOrderV2 {
    var id: String? = null
    var type: String? = null
    var platinum: Int? = null
    var quantity: Int? = null
    var perTrade: Int? = null
    var visible: Boolean? = null
    var createdAt: String? = null
    var updatedAt: String? = null
    var itemId: String? = null
    var user: ItemOrderUserV2? = null
}

class ItemOrderUserV2 {
    var id: String? = null
    var ingameName: String? = null
    var slug: String? = null
    var avatar: String? = null
    var reputation: Int? = null
    var platform: String? = null
    var crossplay: Boolean? = null
    var locale: String? = null
    var status: String? = null
    var activity: ItemOrderActivityV2? = null
    var lastSeen: String? = null
}

class ItemOrderActivityV2 {
    var type: String? = null
    var details: String? = null
}