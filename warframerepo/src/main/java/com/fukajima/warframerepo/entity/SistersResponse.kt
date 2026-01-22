package com.fukajima.warframerepo.entity


data class SistersWeapon(
    val id: String,
    val slug: String,
    val gameRef: String,
    val reqMasteryRank: Int,
    val i18n: SistersI18n,
    val weaponName: String = i18n.en.name
)

data class SistersI18n(
    val en: SistersLanguageContent
)

data class SistersLanguageContent(
    val name: String,
    val wikiLink: String,
    val icon: String,
    val thumb: String
)