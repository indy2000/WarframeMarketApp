package com.fukajima.warframerepo

import android.util.Log
import com.fukajima.warframerepo.entity.LoginResponse
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

/**
 * Helper genérico que realiza os requests GET e POST para a API do Warframe Market e retorna o Json desserializado.
 */
class HttpHelper<T> {
    fun HttpGet(type: Type, url: String, headers: Map<String, String>?, timeOut: Long?) : T {

        val client = OkHttpClient.Builder()
        client.connectTimeout(timeOut ?: 10L, TimeUnit.SECONDS)
        client.readTimeout(timeOut ?: 10L , TimeUnit.SECONDS)
        client.writeTimeout(timeOut ?: 10L, TimeUnit.SECONDS)

        val requestBuilder = Request.Builder()

        headers?.let {
            headers.forEach { header ->
                requestBuilder.addHeader(header.key, header.value)
            }
        }

        val request =
            requestBuilder.url(url)
                .build()

        val response = client.build().newCall(request).execute()

        val retorno = response.body()!!.string()

        Log.w("REQUEST-GET", String.format("GET-RETORNO: %s", retorno))
        return Gson().fromJson<T>(retorno, type)
    }

    fun HttpPost(type: Type, url: String, headers: Map<String, String>?, jsonBody: String, timeOut: Long?) : T {

        val client = OkHttpClient.Builder()
        client.connectTimeout(timeOut ?: 10L, TimeUnit.SECONDS)
        client.readTimeout(timeOut ?: 10L, TimeUnit.SECONDS)
        client.writeTimeout(timeOut ?: 10L, TimeUnit.SECONDS)

        val requestBuilder = Request.Builder()

        headers?.let {
            headers.forEach { header ->
                requestBuilder.addHeader(header.key, header.value)
            }
            requestBuilder.addHeader("Content-Type", "application/json")
            requestBuilder.addHeader("Accept", "application/json")
        }

        val body: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody)

        val request =
            requestBuilder
                .url(url)
                .post(body)
                .build()

        val response = client.build().newCall(request).execute()

        val retorno = response.body()!!.string()

        Log.w("REQUEST-GET", String.format("GET-RETORNO: %s", retorno))
        return Gson().fromJson(retorno, type)
    }

    fun HttpPostLogin(type: Type, url: String, headers: Map<String, String>?, jsonBody: String, timeOut: Long?) : LoginResponse? {

        val client = OkHttpClient.Builder()
        client.connectTimeout(timeOut ?: 10L, TimeUnit.SECONDS)
        client.readTimeout(timeOut ?: 10L, TimeUnit.SECONDS)
        client.writeTimeout(timeOut ?: 10L, TimeUnit.SECONDS)

        val requestBuilder = Request.Builder()

        headers?.let {
            headers.forEach { header ->
                requestBuilder.addHeader(header.key, header.value)
            }
            requestBuilder.addHeader("Content-Type", "application/json")
            requestBuilder.addHeader("Accept", "application/json")
        }

        val body: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody)

        val request =
            requestBuilder
                .url(url)
                .post(body)
                .build()

        val response = client.build().newCall(request).execute()

        val retorno = response.body()!!.string()

        Log.w("REQUEST-POST", String.format("POST-RETORNO: %s", retorno))
        var retornoObjeto = Gson().fromJson<LoginResponse>(retorno, type)

        retornoObjeto.jwt = response.header("Set-Cookie")
        return retornoObjeto
    }

    fun HttpDelete(type: Type, url: String, headers: Map<String, String>?, timeOut: Long?) : T {

        val client = OkHttpClient.Builder()
        client.connectTimeout(timeOut ?: 10L, TimeUnit.SECONDS)
        client.readTimeout(timeOut ?: 10L , TimeUnit.SECONDS)
        client.writeTimeout(timeOut ?: 10L, TimeUnit.SECONDS)

        val requestBuilder = Request.Builder()

        headers?.let {
            headers.forEach { header ->
                requestBuilder.addHeader(header.key, header.value)
            }
        }

        val request =
            requestBuilder
                .url(url)
                .delete()
                .build()

        val response = client.build().newCall(request).execute()

        val retorno = response.body()!!.string()

        Log.w("REQUEST-DELETE", String.format("DELETE-RETORNO: %s", retorno))
        return Gson().fromJson<T>(retorno, type)
    }

    fun HttpPatch(type: Type, url: String, headers: Map<String, String>?, jsonBody: String, timeOut: Long?) : T {

        val client = OkHttpClient.Builder()
        client.connectTimeout(timeOut ?: 10L, TimeUnit.SECONDS)
        client.readTimeout(timeOut ?: 10L, TimeUnit.SECONDS)
        client.writeTimeout(timeOut ?: 10L, TimeUnit.SECONDS)

        val requestBuilder = Request.Builder()

        headers?.let {
            headers.forEach { header ->
                requestBuilder.addHeader(header.key, header.value)
            }
            requestBuilder.addHeader("Content-Type", "application/json")
            requestBuilder.addHeader("Accept", "application/json")
        }

        val body: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody)

        val request =
            requestBuilder
                .url(url)
                .patch(body)
                .build()

        val response = client.build().newCall(request).execute()

        val retorno = response.body()!!.string()

        Log.w("REQUEST-PATCH", String.format("PATCH-RETORNO: %s", retorno))
        return Gson().fromJson(retorno, type)
    }


}