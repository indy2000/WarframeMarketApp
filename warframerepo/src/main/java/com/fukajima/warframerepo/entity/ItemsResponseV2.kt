package com.fukajima.warframerepo.entity


data class ItemsResponseV2(
    val apiVersion: String,
    val data: List<ItemData>
)

data class ItemDataV2(
    val id: String,
    val slug: String,
    val gameRef: String,
    val tags: List<String>,
    val i18n: I18n
)

data class I18n(
    val pt: LanguageData,
    val en: LanguageData
)

data class LanguageData(
    val name: String,
    val icon: String,
    val thumb: String
)