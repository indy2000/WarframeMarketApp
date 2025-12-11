package com.fukajima.warframerepo.entity

class PlaceOrderRequest {
    var platinum: Int? = null
    var quantity: Int? = null
    var visible: Boolean = true
    var item_id: String? = null
    var order_type: String? = null

}

class PlaceOrderRequestV2 {
    var platinum: Int? = null
    var quantity: Int? = null
    var visible: Boolean = true
    var itemId: String? = null
    var type: String? = null

}

class PlaceOrderResponse{
    val payload: Payload? = null
}

class PlaceOrderResponseV2{
    val error: String? = null
}

data class Payload(
    val order: Order
)

data class Order(
    val order_type: String,
    val visible: Boolean,
    val platinum: Int,
    val id: String,
    val item: Item,
    val last_update: String,
    val region: String,
    val quantity: Int,
    val creation_date: String,
    val platform: String
)

data class PlaceOrderItem(
    val tags: List<String>,
    val url_name: String,
    val thumb: String,
    val id: String,
    val ducats: Int,
    val sub_icon: String,
    val icon: String,
    val quantity_for_set: Int,

    // Múltiplas traduções, cada idioma possui apenas "item_name"
    val en: LangItem,
    val ru: LangItem,
    val ko: LangItem,
    val fr: LangItem,
    val sv: LangItem,
    val de: LangItem,
    val zh_hant: LangItem,
    val zh_hans: LangItem,
    val pt: LangItem,
    val es: LangItem,
    val pl: LangItem,
    val cs: LangItem,
    val uk: LangItem,
    val it: LangItem,
    val tr: LangItem,
    val ja: LangItem
)

data class LangItem(
    val item_name: String
)
