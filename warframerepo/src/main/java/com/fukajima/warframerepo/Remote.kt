package com.fukajima.warframerepo

import android.content.Context
import android.webkit.CookieManager
import com.fukajima.warframerepo.entity.Item
import com.fukajima.warframerepo.entity.ItemData
import com.fukajima.warframerepo.entity.ItemDataV2
import com.fukajima.warframerepo.entity.ItemOrder
import com.fukajima.warframerepo.entity.ItemOrderV2
import com.fukajima.warframerepo.entity.ItemOrdersResponse
import com.fukajima.warframerepo.entity.ItemOrdersResponseV2
import com.fukajima.warframerepo.entity.ItemsResponse
import com.fukajima.warframerepo.entity.ItemsResponseV2
import com.fukajima.warframerepo.entity.LoginResponse
import com.fukajima.warframerepo.entity.PlaceOrderRequest
import com.fukajima.warframerepo.entity.PlaceOrderRequestV2
import com.fukajima.warframerepo.entity.PlaceOrderResponse
import com.fukajima.warframerepo.entity.PlaceOrderResponseV2
import com.fukajima.warframerepo.entity.Response
import com.fukajima.warframerepo.entity.ResponseGeneric
import com.fukajima.warframerepo.entity.UserData
import com.fukajima.warframerepo.entity.UserItemOrderResponse
import com.fukajima.warframerepo.entity.UserProfileResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Maybe
import okhttp3.Cookie

import kotlin.reflect.javaType
import kotlin.reflect.typeOf

/**
 * Responsável por manter as funções de cada request para a API do Warframe Market.
 */
class Remote(var context: Context) {

    val apiBaseUrl = "https://api.warframe.market/v1"
    private val jwtToken = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzaWQiOiJJSzJVZjlyb0JCcVpJeXhmYmxxUUFiekhMTzNxRGdFUyIsImNzcmZfdG9rZW4iOiI1Mzk5OWU5ODA0YzBjODI3N2Q0ZjEyYTBlNmJiZTE0YzljMjlmZDExIiwiZXhwIjoxNzQxMDM0NzUwLCJpYXQiOjE3MzU4NTA3NTAsImlzcyI6Imp3dCIsImF1ZCI6Imp3dCIsImF1dGhfdHlwZSI6ImNvb2tpZSIsInNlY3VyZSI6ZmFsc2UsImp3dF9pZGVudGl0eSI6IjhEQkp3OGpraDJDUU9BY0x6d3lZdEhYWENHOFN4OHFuIiwibG9naW5fdWEiOiJiJ01vemlsbGEvNS4wIChXaW5kb3dzIE5UIDEwLjA7IFdpbjY0OyB4NjQpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS8xMjguMC4wLjAgU2FmYXJpLzUzNy4zNiBPUFIvMTE0LjAuMC4wJyIsImxvZ2luX2lwIjoiYicyODA0OjE0ZDo1YzE4OmNjZTc6ZWQ3ZDoyYzk0OjUyNzE6ODc0OCcifQ.FwR_rHXoBG7WF6A_cfoyzS6ANmbsusdDv6gxx7r5rrU"

    @Deprecated("API está retornando HTTP 404. Utilizar a v2.")
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

    @Deprecated("API está retornando HTTP 404. Utilizar a v2.")
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

    fun setItemOrder(order: PlaceOrderRequest, jwt:String): ResponseGeneric{
        var response: ResponseGeneric = ResponseGeneric()
        var url = "$apiBaseUrl/profile/orders"
        val type = TypeToken.get(PlaceOrderResponse::class.java).type

        try{
            var header = mutableMapOf<String, String>()
            header.put("Cookie", jwt)
            header.put("Authorization", jwt.split(";").firstOrNull(){ it.contains("JWT")} ?.replace("JWT=","")?:"")
            val apiResponse = HttpHelper<PlaceOrderResponse>().HttpPost(type, url, header,Gson().toJson(order), 10)

            if(apiResponse.payload?.order != null){
                response.success = true
            }
            else {
                throw Exception(context.getString(R.string.not_possible_place_order))
            }
        }
        catch (t: Throwable){
            response.success = false
            response.exception = t
            response.message = context.getString(R.string.not_possible_place_order)
        }

        return response


    }

    fun setItemOrderV2 (order: PlaceOrderRequest, jwt:String): ResponseGeneric{
        var orderV2 = PlaceOrderRequestV2()
        orderV2.itemId = order.item_id
        orderV2.type = order.order_type
        orderV2.platinum = order.platinum
        orderV2.visible = order.visible
        orderV2.quantity = order.quantity
        var response: ResponseGeneric = ResponseGeneric()
        var url = "${apiBaseUrl.replace("v1","v2")}/order"
        val type = TypeToken.get(PlaceOrderResponseV2::class.java).type

        try{
            //var responseLogin = login(jwt)
            var header = mutableMapOf<String, String>()
            //header.put("Cookie", responseLogin.obj ?: jwt)
            header.put("Cookie", jwt)
            //header.put("Authorization", jwt.split(";").firstOrNull(){ it.contains("JWT")} ?.replace("JWT=","")?:"")

            val apiResponse = HttpHelper<PlaceOrderResponseV2>().HttpPost(type, url, header,Gson().toJson(orderV2), 10)

            if(apiResponse.error == null){
                response.success = true
            }
            else {
                throw Exception(context.getString(R.string.not_possible_place_order))
            }
        }
        catch (t: Throwable){
            response.success = false
            response.exception = t
            response.message = context.getString(R.string.not_possible_place_order)
        }

        return response

    }

    fun login(jwt:String, address:String, pass:String): Response<String> {
        var response: Response<String> = Response<String>()
        var url = "$apiBaseUrl/auth/signin"
        val type = TypeToken.get(LoginResponse::class.java).type

        try{
            var header = mutableMapOf<String, String>()
            header.put("Cookie", jwt)
            header.put("authorization", jwt.split(";").firstOrNull(){ it.contains("JWT")} ?.replace("JWT=","")?:"")
            val loginRequest = object{
                var email = address
                val password = pass
                val device_id = "None"
            }
            val apiResponse = HttpHelper<LoginResponse>().HttpPostLogin(type, url, header,Gson().toJson(loginRequest), 10)

            if(apiResponse?.payload != null){
                response.success = true
                response.obj = apiResponse.jwt
            }
            else {
                throw Exception(context.getString(R.string.not_possible_to_login))
            }
        }
        catch (t: Throwable){
            response.success = false
            response.exception = t
            response.message = context.getString(R.string.not_possible_to_login)
        }

        return response

    }

    fun getUserProfile(jwt: String): Response<UserData>{
        var response: Response<UserData> = Response<UserData>()
        val type = TypeToken.get(UserProfileResponse::class.java).type
        var url = "${apiBaseUrl.replace("v1", "v2")}/me"

        try {
            var header = mutableMapOf<String, String>()
            header.put("Cookie", jwt)
            header.put("authorization", jwt.split(";").firstOrNull(){ it.contains("JWT")} ?.replace("JWT=","")?:"")

            val apiResponse = HttpHelper<UserProfileResponse>().HttpGet(type, url, header, 10)

            if(apiResponse.data != null) {
                response.success = true
                response.obj = apiResponse.data
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

    fun getOrderBySignInUser(jwt: String): Response<List<ItemData>>{
        var response: Response<List<ItemData>> = Response<List<ItemData>>()
        val type = TypeToken.get(UserItemOrderResponse::class.java).type
        var url = "${apiBaseUrl.replace("v1","v2")}/orders/my"

        try {
            var header = mutableMapOf<String, String>()
            header.put("Cookie", jwt)
            header.put("authorization", jwt.split(";").firstOrNull(){ it.contains("JWT")} ?.replace("JWT=","")?:"")

            val apiResponse = HttpHelper<UserItemOrderResponse>().HttpGet(type, url, header, 10)

            if(apiResponse.data?.isNullOrEmpty() == false) {
                response.success = true
                response.obj = apiResponse.data!!
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

    fun getItemsV2() : Response<List<ItemDataV2>> {
        var response: Response<List<ItemDataV2>> = Response<List<ItemDataV2>>()
        val type = TypeToken.get(ItemsResponseV2::class.java).type
        var url = "${apiBaseUrl.replace("v1","v2")}/items"

        try {
            val apiResponse = HttpHelper<ItemsResponseV2>().HttpGet(type, url, null, 10)

            if(!apiResponse.data.isNullOrEmpty()) {
                response.success = true
                response.obj = apiResponse.data
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

    fun getOrdersByItemV2(item_url_name: String) : Response<List<ItemOrderV2>>{
        var response: Response<List<ItemOrderV2>> = Response<List<ItemOrderV2>>()
        val type = TypeToken.get(ItemOrdersResponseV2::class.java).type
        var url = "${apiBaseUrl.replace("v1","v2")}/orders/item/$item_url_name"

        try {
            val apiResponse = HttpHelper<ItemOrdersResponseV2>().HttpGet(type, url, null, 10)

            if(!apiResponse.data.isNullOrEmpty()) {
                response.success = true
                response.obj = apiResponse.data
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
}