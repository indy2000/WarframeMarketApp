package com.fukajima.warframemarket.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.fukajima.warframerepo.entity.Item
import com.fukajima.warframerepo.entity.Response
import com.fukajima.warframerepo.repository.ItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ItemViewModel(application: Application) : AndroidViewModel(application) {

    var itemLiveData = MutableLiveData<Response<List<Item>>>()

    fun getItems(requestCode: Int) = GlobalScope.launch(Dispatchers.IO) {
        val retorno = ItemRepository(getApplication()).getItems()
        retorno.requestCode = requestCode
        itemLiveData.postValue(retorno)
    }
}