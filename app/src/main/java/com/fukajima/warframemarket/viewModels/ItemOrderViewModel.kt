package com.fukajima.warframemarket.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.fukajima.warframerepo.entity.Item
import com.fukajima.warframerepo.entity.ItemData
import com.fukajima.warframerepo.entity.ItemOrder
import com.fukajima.warframerepo.entity.ItemOrderUser
import com.fukajima.warframerepo.entity.ItemOrderV2
import com.fukajima.warframerepo.entity.PlaceOrderRequest
import com.fukajima.warframerepo.entity.Response
import com.fukajima.warframerepo.entity.ResponseGeneric
import com.fukajima.warframerepo.repository.ItemOrderRepository
import com.fukajima.warframerepo.repository.ItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ItemOrderViewModel(application: Application) : AndroidViewModel(application) {
    var itemOrderLiveData = MutableLiveData<Response<List<ItemOrder>>>()
    var itemOrderV2LiveData = MutableLiveData<Response<List<ItemOrder>>>()
    var placeOrderLiveData = MutableLiveData<ResponseGeneric>()
    var userItemOrderLiveData = MutableLiveData<Response<List<ItemData>>>()

    @Deprecated("API está retornando HTTP 404. Utilizar a v2.")
    fun getItemOrders(url_name : String) = GlobalScope.launch(Dispatchers.IO) {
        val retorno = ItemOrderRepository(getApplication()).getItemOrders(url_name)
        itemOrderLiveData.postValue(retorno)
    }

    fun setItemOrder(order:PlaceOrderRequest, jwt: String) = GlobalScope.launch(Dispatchers.IO){
        val retorno = ItemOrderRepository(getApplication()).setItemOrder(order, jwt)
        placeOrderLiveData.postValue(retorno)
    }

    fun getItemOrderSignInUser(jwt:String) = GlobalScope.launch(Dispatchers.IO){
        val retorno = ItemOrderRepository(getApplication()).getItemOrderSignInUser(jwt)
        retorno.obj?.map { convertItemOrderNameToSignUser(it) }

        userItemOrderLiveData.postValue(retorno)
    }

    fun convertItemOrderNameToSignUser(item: ItemData): ItemData{
        if (item.id.isNullOrEmpty().not()){
            var itemFromDb = ItemRepository(getApplication()).getItemById(item.itemId)

            itemFromDb.item_name?.let{ item_name ->
                item.item_name = item_name
            }
        }

        return item
    }

    fun getItemOrdersV2(url_name : String) = GlobalScope.launch(Dispatchers.IO) {
        val retornoApi = ItemOrderRepository(getApplication()).getItemOrdersV2(url_name)

        var retorno = Response<List<ItemOrder>>()
        retorno.success = retornoApi.success
        retorno.exception = retornoApi.exception
        retorno.message = retornoApi.message

        retorno.obj = retornoApi.obj?.map { convertItemOrderV2ToItemOrderV1(it) }

        itemOrderV2LiveData.postValue(retorno)
    }

    fun convertItemOrderV2ToItemOrderV1(itemOrderV2: ItemOrderV2) : ItemOrder {
        var itemOrder = ItemOrder()

        itemOrder.apply {
            this.id = itemOrderV2.id
            this.order_type = itemOrderV2.type
            this.platinum = itemOrderV2.platinum
            this.platform = itemOrderV2.user?.platform
            this.visible = itemOrderV2.visible ?: false
            this.creation_date = itemOrderV2.createdAt
            this.last_update = itemOrderV2.updatedAt
            this.quantity = itemOrderV2.quantity
            this.user = ItemOrderUser().apply {
                this.id = itemOrderV2.user?.id
                this.avatar = itemOrderV2.user?.avatar
                this.ingame_name = itemOrderV2.user?.ingameName
                this.status = itemOrderV2.user?.status
                this.reputation = itemOrderV2.user?.reputation
                this.locale = itemOrderV2.user?.locale
                this.last_seen = itemOrderV2.user?.lastSeen
            }
        }

        return itemOrder
    }
}