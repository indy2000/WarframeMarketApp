package com.fukajima.warframerepo.repository

import android.content.Context
import com.fukajima.warframerepo.Remote
import com.fukajima.warframerepo.entity.Response
import com.fukajima.warframerepo.entity.UserData

class ProfileRepository(val context: Context) {
    fun getUserProfile(jwt: String): Response<UserData> {
        return Remote(context).getUserProfile(jwt)
    }
}