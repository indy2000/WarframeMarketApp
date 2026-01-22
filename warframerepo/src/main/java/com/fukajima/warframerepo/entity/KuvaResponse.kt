package com.fukajima.warframerepo.entity


data class KuvaWeapon(
    val id: String,
    val slug: String,
    val gameRef: String,
    val reqMasteryRank: Int,
    val i18n: KuvaI18n,
    val weaponName: String = i18n.en.name
)

data class KuvaI18n(
    val en: KuvaLanguageContent
)

data class KuvaLanguageContent(
    val name: String,
    val icon: String,
    val thumb: String
)