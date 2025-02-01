package com.fukajima.warframemarket.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.fukajima.warframerepo.entity.ItemOrder
import com.fukajima.warframerepo.entity.Response
import com.fukajima.warframerepo.repository.ItemOrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ItemOrderViewModel(application: Application) : AndroidViewModel(application) {
    var itemOrderLiveData = MutableLiveData<Response<List<ItemOrder>>>()

    fun getItemOrders(url_name : String) = GlobalScope.launch(Dispatchers.IO) {
        val retorno = ItemOrderRepository(getApplication()).getItemOrders(url_name)
        itemOrderLiveData.postValue(retorno)
    }
}