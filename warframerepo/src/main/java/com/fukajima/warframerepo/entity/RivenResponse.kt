package com.fukajima.warframerepo.entity


data class RivenWeapon(
    val id: String,
    val slug: String,
    val gameRef: String,
    val group: String,
    val rivenType: String,
    val disposition: Double,
    val reqMasteryRank: Int,
    val i18n: RivenI18n,
    val weaponName: String = i18n.en.name
)

data class RivenI18n(
    val en: RivenLanguageContent
)

data class RivenLanguageContent(
    val name: String,
    val icon: String,
    val thumb: String
)