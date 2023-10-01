package com.ecomapp.wear.Viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.wear.Models.AddressModel
import com.ecomapp.wear.Repositories.DataRepository
import com.ecomapp.wear.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class AddressViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    var address_mutableLiveData = MutableLiveData<Response<AddressModel>>()
    val address_liveData : LiveData<Response<AddressModel>>
        get() = address_mutableLiveData

    fun LoadAddress(){
        viewModelScope.async {
            val result = dataRepository.LoadAddress()
            address_mutableLiveData.postValue(result)
        }
    }

}