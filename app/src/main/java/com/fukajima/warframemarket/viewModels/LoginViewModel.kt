package com.fukajima.warframemarket.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fukajima.warframerepo.entity.Response
import com.fukajima.warframerepo.entity.ResponseGeneric
import com.fukajima.warframerepo.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginViewModel(application: Application): AndroidViewModel(application) {
    var loginLiveData = MutableLiveData<Response<String>>()

    fun handleLogin(jwt: String, address: String, pass: String) = GlobalScope.launch(Dispatchers.IO){
        val retorno = LoginRepository(getApplication()).handleLogin(jwt, address, pass)
        loginLiveData.postValue(retorno)
    }


}