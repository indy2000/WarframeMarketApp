package com.fukajima.warframerepo.entity

data class LoginResponse(
    val payload: Payload?,
    val error: ErrorDetail?,
    var jwt: String?
)

data class LoginPayload(
    val user: LoginUser
)

data class LoginUser(
    val platform: String,
    val writtenReviews: Int,
    val role: String,
    val background: String?,
    val unreadMessages: Int,
    val avatar: String?,
    val reputation: Int,
    val locale: String,
    val checkCode: String,
    val ingameName: String,
    val banned: Boolean,
    val hasMail: Boolean,
    val slug: String,
    val id: String,
    val anonymous: Boolean,
    val verification: Boolean,
    val region: String,
    val crossplay: Boolean,
    val linkedAccounts: LinkedAccounts
)

data class LinkedAccounts(
    val steamProfile: Boolean,
    val patreonProfile: Boolean,
    val xboxProfile: Boolean,
    val discordProfile: Boolean,
    val githubProfile: Boolean
)

data class ErrorDetail(
    val password: List<String>
)