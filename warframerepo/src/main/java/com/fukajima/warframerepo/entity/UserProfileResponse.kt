package com.fukajima.warframerepo.entity

data class UserProfileResponse(

    val apiVersion: String,
    val data: UserData?,
    val error: Any?
)


data class UserData(
    val id: String,
    val role: String,
    val tier: String,
    val subscription: Boolean,
    val ingameName: String,
    val slug: String,
    val about: String,
    val aboutRaw: String,
    val reputation: Int,
    val masteryRank: Int,
    val credits: Int,
    val lastSeen: String,
    val platform: String,
    val crossplay: Boolean,
    val locale: String,
    val theme: String,
    val syncLocale: Boolean,
    val syncTheme: Boolean,
    val verification: Boolean,
    val checkCode: String,
    val createdAt: String,
    val reviewsLeft: Int,
    val unreadNotifications: Int,
    val linkedAccounts: LinkedAccounts,
    val hasEmail: Boolean
    )

data class ProfileLinkedAccounts(
    val steam: Boolean,
    val discord: Boolean,
    val xbox: Boolean,
    val playstation: Boolean,
    val github: Boolean,
    val patreon: Boolean
)
