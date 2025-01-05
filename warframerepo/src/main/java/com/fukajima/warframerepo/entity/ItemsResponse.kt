package com.fukajima.warframerepo.entity

class ItemsResponse {
    var payload: ItemsPayload? = null
}

class ItemsPayload {
    var items: List<Item> = mutableListOf()
}

class Item {
    var thumb: String? = null
    var item_name: String? = null
    var url_name: String? = null
    var id: String? = null
}

