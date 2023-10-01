package com.ecomapp.wear.Viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.wear.Models.UserModel
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Repositories.UserRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginScreenViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel(){

    var user_mutableLiveData = MutableLiveData<Response<FirebaseUser>>()
    val user_liveData : LiveData<Response<FirebaseUser>>
        get() = user_mutableLiveData

    fun customUserLogin(userModel: UserModel){
        viewModelScope.launch {
            val result = userRepository.customUserLogin(userModel)
            user_mutableLiveData.postValue(result)
        }
    }
}