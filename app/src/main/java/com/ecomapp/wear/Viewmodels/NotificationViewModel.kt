package com.ecomapp.wear.Viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.wear.Models.NotificationModel
import com.ecomapp.wear.Repositories.DataRepository
import com.ecomapp.wear.Repositories.Response
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotificationViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    var notification_mutableLiveData = MutableLiveData<Response<ArrayList<NotificationModel>>>()
    val notification_liveData : LiveData<Response<ArrayList<NotificationModel>>>
        get() = notification_mutableLiveData
    fun getNotifications(){
        viewModelScope.launch {
            val result = dataRepository.getNotifications()
            notification_mutableLiveData.postValue(result)
        }
    }
}