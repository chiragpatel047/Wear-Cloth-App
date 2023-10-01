package com.ecomapp.wear.Viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.wear.Models.OrderModel
import com.ecomapp.wear.Repositories.DataRepository
import com.ecomapp.wear.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class MyOrderViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    var order_mutableLiveData = MutableLiveData<Response<ArrayList<OrderModel>>>()
    val order_liveData : LiveData<Response<ArrayList<OrderModel>>>
        get() = order_mutableLiveData

    fun LoadOrders(orderCat : String){
        viewModelScope.async {
            val result = dataRepository.LoadOrders(orderCat)
            order_mutableLiveData.postValue(result)
        }
    }

}