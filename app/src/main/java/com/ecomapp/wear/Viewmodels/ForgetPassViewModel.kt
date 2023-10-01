package com.ecomapp.wear.Viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Repositories.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ForgetPassViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {

    var forget_mutableLiveData = MutableLiveData<Response<String>>()
    val forget_liveData : LiveData<Response<String>>
        get() = forget_mutableLiveData

    fun sendPasswordResetLink(email : String){
        viewModelScope.launch {
            val result = userRepository.sendPasswordResetLink(email)
            forget_mutableLiveData.postValue(result)
        }
    }

}