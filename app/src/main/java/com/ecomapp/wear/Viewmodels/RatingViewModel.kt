package com.ecomapp.wear.Viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.wear.Models.ProuctModel
import com.ecomapp.wear.Models.RatingModel
import com.ecomapp.wear.Models.UserModel
import com.ecomapp.wear.Repositories.DataRepository
import com.ecomapp.wear.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class RatingViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    var rate_mutableLiveData = MutableLiveData<Response<String>>()
    val rate_liveData : LiveData<Response<String>>
        get() = rate_mutableLiveData

    var user_mutableLiveData = MutableLiveData<Response<UserModel>>()
    val user_liveData : LiveData<Response<UserModel>>
        get() = user_mutableLiveData

    var allrate_mutableLiveData = MutableLiveData<Response<ArrayList<RatingModel>>>()
    val allrate_liveData : LiveData<Response<ArrayList<RatingModel>>>
        get() = allrate_mutableLiveData

    var productDetails_mutableLiveData = MutableLiveData<Response<ProuctModel>>()
    val productDetails_liveData : LiveData<Response<ProuctModel>>
        get() = productDetails_mutableLiveData
    fun rateProduct(ratingModel : RatingModel, productId: String){
        viewModelScope.async {
            val result = dataRepository.rateProduct(ratingModel,productId)
            rate_mutableLiveData.postValue(result)
        }
    }

    fun getUserInfo(){
        viewModelScope.async {
            val result = dataRepository.getUserInfo()
            user_mutableLiveData.postValue(result)
        }
    }

    fun getRating(productId: String){
        viewModelScope.async {
            val result = dataRepository.getRating(productId)
            allrate_mutableLiveData.postValue(result)
        }
    }

    fun LoadProductDetails(productId : String){
        viewModelScope.async {
            val result = dataRepository.LoadProductDetails(productId)
            productDetails_mutableLiveData.postValue(result)
        }
    }

}