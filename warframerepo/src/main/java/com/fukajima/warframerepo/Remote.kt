package com.fukajima.warframerepo

import android.content.Context
import com.fukajima.warframerepo.entity.Item
import com.fukajima.warframerepo.entity.ItemOrder
import com.fukajima.warframerepo.entity.ItemOrdersResponse
import com.fukajima.warframerepo.entity.ItemsResponse
import com.fukajima.warframerepo.entity.Response
import com.google.gson.reflect.TypeToken
import io.reactivex.Maybe

import kotlin.reflect.javaType
import kotlin.reflect.typeOf

/**
 * Responsável por manter as funções de cada request para a API do Warframe Market.
 */
class Remote(var context: Context) {

    val apiBaseUrl = "https://api.warframe.market/v1"
    private val jwtToken = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzaWQiOiJJSzJVZjlyb0JCcVpJeXhmYmxxUUFiekhMTzNxRGdFUyIsImNzcmZfdG9rZW4iOiI1Mzk5OWU5ODA0YzBjODI3N2Q0ZjEyYTBlNmJiZTE0YzljMjlmZDExIiwiZXhwIjoxNzQxMDM0NzUwLCJpYXQiOjE3MzU4NTA3NTAsImlzcyI6Imp3dCIsImF1ZCI6Imp3dCIsImF1dGhfdHlwZSI6ImNvb2tpZSIsInNlY3VyZSI6ZmFsc2UsImp3dF9pZGVudGl0eSI6IjhEQkp3OGpraDJDUU9BY0x6d3lZdEhYWENHOFN4OHFuIiwibG9naW5fdWEiOiJiJ01vemlsbGEvNS4wIChXaW5kb3dzIE5UIDEwLjA7IFdpbjY0OyB4NjQpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS8xMjguMC4wLjAgU2FmYXJpLzUzNy4zNiBPUFIvMTE0LjAuMC4wJyIsImxvZ2luX2lwIjoiYicyODA0OjE0ZDo1YzE4OmNjZTc6ZWQ3ZDoyYzk0OjUyNzE6ODc0OCcifQ.FwR_rHXoBG7WF6A_cfoyzS6ANmbsusdDv6gxx7r5rrU"

    fun getItems() : Response<List<Item>> {
        var response: Response<List<Item>> = Response<List<Item>>()
        val type = TypeToken.get(ItemsResponse::class.java).type
        var url = "$apiBaseUrl/items"

        try {
            val apiResponse = HttpHelper<ItemsResponse>().HttpGet(type, url, null, 10)

            if(!apiResponse.payload?.items.isNullOrEmpty()) {
               response.success = true
               response.obj = apiResponse.payload!!.items
            }
            else {
                throw Exception(context.getString(R.string.not_possible_return_items))
            }
        }
        catch (t: Throwable) {
            response.success = false
            response.exception = t
            response.message = context.getString(R.string.not_possible_return_items)
        }

        return response
    }

    fun getOrdersByItem(item_url_name: String) : Response<List<ItemOrder>>{
        var response: Response<List<ItemOrder>> = Response<List<ItemOrder>>()
        val type = TypeToken.get(ItemOrdersResponse::class.java).type
        var url = "$apiBaseUrl/items/$item_url_name/orders"

        try {
            val apiResponse = HttpHelper<ItemOrdersResponse>().HttpGet(type, url, null, 10)

            if(!apiResponse.payload?.orders.isNullOrEmpty()) {
                response.success = true
                response.obj = apiResponse.payload!!.orders
            }
            else {
                throw Exception(context.getString(R.string.not_possible_return_orders_item))
            }
        }
        catch (t: Throwable) {
            response.success = false
            response.exception = t
            response.message = context.getString(R.string.not_possible_return_orders_item)
        }

        return response
    }

    fun login() {
        TODO("Not Implemented")
    }
}