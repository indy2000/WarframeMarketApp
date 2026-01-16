package com.fukajima.warframerepo.entity

data class UserProfileResponse(

    val apiVersion: String,
    val data: UserData?,
    val error: Any?
)


class UserData{
    val id: String? = null
    val role: String? = null
    val tier: String? = null
    val subscription: Boolean? = null
    val ingameName: String? = null
    val slug: String? = null
    val avatar: String? = null
    val about: String? = null
    val aboutRaw: String? = null
    val reputation: Int? = null
    val masteryRank: Int? = null
    val credits: Int? = null
    val lastSeen: String? = null
    val platform: String? = null
    val crossplay: Boolean? = null
    val locale: String? = null
    val theme: String? = null
    val syncLocale: Boolean? = null
    val syncTheme: Boolean? = null
    val verification: Boolean? = null
    val checkCode: String? = null
    val createdAt: String? = null
    val reviewsLeft: Int? = null
    val unreadNotifications: Int? = null
    val linkedAccounts: LinkedAccounts? = null
    val hasEmail: Boolean? = null

    fun getUserAvatarAssetUrl() : String? {
        return if(!this.avatar.isNullOrEmpty()) "https://warframe.market/static/assets/${this.avatar}" else null
    }
}


data class ProfileLinkedAccounts(
    val steam: Boolean,
    val discord: Boolean,
    val xbox: Boolean,
    val playstation: Boolean,
    val github: Boolean,
    val patreon: Boolean
)
