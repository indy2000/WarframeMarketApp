package com.fukajima.warframemarket.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.fukajima.warframemarket.R
import com.fukajima.warframerepo.entity.ContractBaseResponse
import com.fukajima.warframerepo.entity.ContractWeapon
import com.fukajima.warframerepo.entity.ItemOrder
import com.fukajima.warframerepo.entity.KuvaWeapon
import com.fukajima.warframerepo.entity.Response
import com.fukajima.warframerepo.entity.ResponseGeneric
import com.fukajima.warframerepo.entity.RivenWeapon
import com.fukajima.warframerepo.entity.SistersWeapon
import com.fukajima.warframerepo.repository.ContractsRepository
import com.fukajima.warframerepo.repository.ItemOrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ContractsViewModel(application: Application) : AndroidViewModel(application) {
    var contractLiveData =MutableLiveData<Response<List<ContractWeapon>>>()


    fun getContractByCategory(category: String) = GlobalScope.launch(Dispatchers.IO){
        val retorno = ContractsRepository(getApplication()).getContractByCategory(category)
        /*when(category){
            "Unveiled Riven Mods" -> rivenLiveData.postValue(retorno)
            "Kuva Lich" -> kuvaLiveData.postValue(retorno)
            "Sisters of Parvos" -> sistersLiveData.postValue(retorno)
        }*/
        contractLiveData.postValue(retorno)

    }
}