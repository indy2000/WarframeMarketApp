package com.fukajima.warframemarket

import android.content.Context
import com.google.gson.Gson

/**
 * This class gets the JWT "code" from warframe market website when user log in, so that we can use this code to identify
 * the user further in other functionalities (such as place order).
 */
class CacheJWT {

    val PRE_ID = "JWT_Cache"

    fun setJWT(context: Context, jwtValue:String){
        val editor = context.getSharedPreferences(PRE_ID, 0).edit()
        editor.putString("JWT", jwtValue)
        editor.apply()
    }

    fun getJWT(context: Context): String{
        val editor = context.getSharedPreferences(PRE_ID, 0)
        val getJWTString = editor.getString("JWT", "")

        return if(getJWTString.isNullOrEmpty()) "" else getJWTString
    }

}