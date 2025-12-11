package com.fukajima.warframemarket.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.fukajima.warframerepo.entity.Response
import com.fukajima.warframerepo.entity.UserData
import com.fukajima.warframerepo.repository.LoginRepository
import com.fukajima.warframerepo.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application): AndroidViewModel(application) {
    var profileLiveData = MutableLiveData<Response<UserData>>()

    fun getUserProfile(jwt:String) = GlobalScope.launch(Dispatchers.IO){
        val retorno = ProfileRepository(getApplication()).getUserProfile(jwt)
        profileLiveData.postValue(retorno)
    }

}