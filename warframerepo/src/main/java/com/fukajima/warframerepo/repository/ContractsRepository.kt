package com.fukajima.warframerepo.repository

import android.content.Context
import com.fukajima.warframerepo.Remote
import com.fukajima.warframerepo.entity.ContractBaseResponse
import com.fukajima.warframerepo.entity.ContractWeapon
import com.fukajima.warframerepo.entity.ItemOrder
import com.fukajima.warframerepo.entity.Response

class ContractsRepository(val context: Context) {

    fun getContractByCategory(category: String): Response<List<ContractWeapon>> {
        return Remote(context).getContractByCategory(category)
    }
}