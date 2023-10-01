package com.ecomapp.wear.Viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.wear.Models.BagModel
import com.ecomapp.wear.Repositories.DataRepository
import com.ecomapp.wear.Repositories.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class BagViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel(){

    var cart_mutableLiveData = MutableLiveData<Response<ArrayList<BagModel>>?>()
    val cart_liveData : MutableLiveData<Response<ArrayList<BagModel>>?>
        get() = cart_mutableLiveData

    var remove_mutableLiveData = MutableLiveData<Response<String>?>()
    val remove_liveData : MutableLiveData<Response<String>?>
        get() = remove_mutableLiveData

    fun loadCartProducts(){
        viewModelScope.async {
            val result = dataRepository.loadCartProducts()
            cart_mutableLiveData.postValue(result)
        }
    }

    fun removeFromCart(productId : String){
        viewModelScope.async {
            val result = dataRepository.removeFromCart(productId)
            remove_mutableLiveData.postValue(result)
        }
    }

    fun clearLiveData(){
        cart_mutableLiveData.value = null
        remove_mutableLiveData.value = null
    }

}