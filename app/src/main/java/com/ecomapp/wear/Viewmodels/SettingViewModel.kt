package com.ecomapp.wear.Viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.wear.Models.UserModel
import com.ecomapp.wear.Repositories.DataRepository
import com.ecomapp.wear.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class SettingViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel(){

    var setting_mutableLiveData = MutableLiveData<Response<UserModel>>()
    val setting_liveData : LiveData<Response<UserModel>>
        get() = setting_mutableLiveData

    var user_mutableLiveData = MutableLiveData<Response<String>>()
    val user_liveData : LiveData<Response<String>>
        get() = user_mutableLiveData

    fun getUserInfo(){
        viewModelScope.async {
            val result = dataRepository.getUserInfo()
            setting_mutableLiveData.postValue(result)
        }
    }

    fun updateUserInfo(userModel: UserModel,uploadPhoto : Boolean){
        viewModelScope.async {
            val result = dataRepository.updateUserInfo(userModel,uploadPhoto)
            user_mutableLiveData.postValue(result)
        }
    }
}