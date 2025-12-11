package com.fukajima.warframemarket.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.fukajima.warframerepo.entity.ItemData
import com.fukajima.warframerepo.entity.ItemOrder
import com.fukajima.warframerepo.entity.PlaceOrderRequest
import com.fukajima.warframerepo.entity.Response
import com.fukajima.warframerepo.entity.ResponseGeneric
import com.fukajima.warframerepo.repository.ItemOrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ItemOrderViewModel(application: Application) : AndroidViewModel(application) {
    var itemOrderLiveData = MutableLiveData<Response<List<ItemOrder>>>()
    var placeOrderLiveData = MutableLiveData<ResponseGeneric>()
    var userItemOrderLiveData = MutableLiveData<Response<List<ItemData>>>()

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
        userItemOrderLiveData.postValue(retorno)
    }
}