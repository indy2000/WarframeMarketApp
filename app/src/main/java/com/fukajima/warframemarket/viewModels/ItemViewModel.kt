package com.fukajima.warframemarket.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.fukajima.warframerepo.entity.Item
import com.fukajima.warframerepo.entity.ItemDataV2
import com.fukajima.warframerepo.entity.Response
import com.fukajima.warframerepo.repository.ItemDataBase
import com.fukajima.warframerepo.repository.ItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ItemViewModel(application: Application) : AndroidViewModel(application) {

    var itemLiveData = MutableLiveData<Response<List<Item>>>()


    @Deprecated("API está retornando HTTP 404. Utilizar a v2.")
    fun getItems(requestCode: Int) = GlobalScope.launch(Dispatchers.IO) {
        val retorno = ItemRepository(getApplication()).getItems()
        retorno.requestCode = requestCode
        itemLiveData.postValue(retorno)
    }

    var itemV2LiveData = MutableLiveData<Response<List<Item>>>()
    fun getItemsV2(requestCode: Int) = GlobalScope.launch(Dispatchers.IO) {
        val retornoApi = ItemRepository(getApplication()).getItemsV2()

        var retorno = Response<List<Item>>()
        retorno.requestCode = requestCode
        retorno.success = retornoApi.success
        retorno.message = retornoApi.message
        retorno.exception = retornoApi.exception

        retorno.obj = retornoApi.obj?.map { convertItemV2ToItemV1(it) }
        if (retorno.obj != null){
            //ItemRepository(getApplication()).deleteAll()
            ItemRepository(getApplication()).insertItemOnDataBase(retorno.obj!!)
        }


        itemV2LiveData.postValue(retorno)
    }

    fun convertItemV2ToItemV1(itemV2: ItemDataV2) : Item {
        var item = Item()
        item.apply {
            this.id = itemV2.id
            this.item_name = itemV2.i18n?.en?.name ?: itemV2.i18n?.pt?.name
            this.url_name = itemV2.slug
            this.thumb = itemV2.i18n?.en?.thumb ?: itemV2.i18n?.pt?.thumb
        }

        return item
    }

}