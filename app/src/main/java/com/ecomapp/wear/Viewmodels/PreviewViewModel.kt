package com.ecomapp.wear.Viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.wear.Models.NotificationModel
import com.ecomapp.wear.Models.OrderModel
import com.ecomapp.wear.Repositories.DataRepository
import com.ecomapp.wear.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class PreviewViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel(){

    var order_mutableLiveData = MutableLiveData<Response<String>>()
    val order_liveData : LiveData<Response<String>>
        get() = order_mutableLiveData

    fun submitOrder(orderList : ArrayList<OrderModel>,notificationList : ArrayList<NotificationModel>){
        viewModelScope.async {
            val result = dataRepository.submitOrder(orderList,notificationList)
            order_mutableLiveData.postValue(result)
        }
    }
}