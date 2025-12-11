package com.fukajima.warframerepo.entity


class ItemsResponseV2 {
    var apiVersion: String? = null
    var data: List<ItemDataV2>? = null
}

class ItemDataV2 {
    var id: String? = null
    var slug: String? = null
    var gameRef: String? = null
    var tags: List<String>? = null
    var i18n: ItemI18nV2? = null
}

class ItemI18nV2 {
    var pt: ItemLanguageDataV2? = null
    var en: ItemLanguageDataV2? = null
}

class ItemLanguageDataV2 {
    var name: String? = null
    var icon: String? = null
    var thumb: String? = null
}