package com.fukajima.warframerepo.repository

import android.content.Context
import com.fukajima.warframerepo.Remote
import com.fukajima.warframerepo.entity.Response
import com.fukajima.warframerepo.entity.ResponseGeneric

class LoginRepository(val context: Context) {

    fun handleLogin(jwt: String, address: String, pass: String): Response<String>{
        return Remote(context).login(jwt, address, pass)
    }

}