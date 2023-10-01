package com.ecomapp.wear.Viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.wear.Models.OrderIdModel
import com.ecomapp.wear.Repositories.DataRepository
import com.ecomapp.wear.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class TermsConditionVIewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    var tnc_mutableLiveData = MutableLiveData<Response<OrderIdModel>>()
    val tnc_liveData : LiveData<Response<OrderIdModel>>
        get() = tnc_mutableLiveData

    fun getTNC(){
        viewModelScope.async {
            val result = dataRepository.getTNC()
            tnc_mutableLiveData.postValue(result)
        }
    }
}