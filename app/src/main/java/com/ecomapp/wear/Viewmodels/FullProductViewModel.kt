package com.ecomapp.wear.Viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.wear.Models.BagModel
import com.ecomapp.wear.Models.ProductImageModel
import com.ecomapp.wear.Models.ProuctModel
import com.ecomapp.wear.Models.SizeModel
import com.ecomapp.wear.Repositories.DataRepository
import com.ecomapp.wear.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class FullProductViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel(){

    var product_mutableLiveData = MutableLiveData<Response<ArrayList<ProductImageModel>>>()
    val product_liveData : LiveData<Response<ArrayList<ProductImageModel>>>
        get() = product_mutableLiveData

    var productDetails_mutableLiveData = MutableLiveData<Response<ProuctModel>>()
    val productDetails_liveData : LiveData<Response<ProuctModel>>
        get() = productDetails_mutableLiveData

    var newProducts_mutableLiveData = MutableLiveData<Response<ArrayList<ProuctModel>>>()
    val newProducts_liveData : LiveData<Response<ArrayList<ProuctModel>>>
        get() = newProducts_mutableLiveData

    var size_mutableLiveData = MutableLiveData<Response<ArrayList<SizeModel>>>()
    val size_liveData : LiveData<Response<ArrayList<SizeModel>>>
        get() = size_mutableLiveData

    var add_to_cart_mutableLiveData = MutableLiveData<Response<String>>()
    val add_to_cart_liveData : LiveData<Response<String>>
        get() = add_to_cart_mutableLiveData

    var cart_mutableLiveData = MutableLiveData<Response<ArrayList<BagModel>>>()
    val cart_liveData : LiveData<Response<ArrayList<BagModel>>>
        get() = cart_mutableLiveData

    var remove_from_cart_mutableLiveData = MutableLiveData<Response<String>>()
    val remove_from_cart_liveData : LiveData<Response<String>>
        get() = remove_from_cart_mutableLiveData

    var add_to_fav_mutableLiveData = MutableLiveData<Response<String>>()
    val add_to_fav_liveData : LiveData<Response<String>>
        get() = add_to_fav_mutableLiveData

    var favProducts_mutableLiveData = MutableLiveData<Response<ArrayList<ProuctModel>>>()
    val favProducts_liveData : LiveData<Response<ArrayList<ProuctModel>>>
        get() = favProducts_mutableLiveData

    var remove_from_fav_mutableLiveData = MutableLiveData<Response<String>>()
    val remove_from_fav_liveData : LiveData<Response<String>>
        get() = remove_from_fav_mutableLiveData

    fun LoadFullProduct(productId : String){
        viewModelScope.async {
            val result = dataRepository.LoadProductImages(productId)
            product_mutableLiveData.postValue(result)
        }
    }

    fun LoadProductDetails(productId : String){
        viewModelScope.async {
            val result = dataRepository.LoadProductDetails(productId)
            productDetails_mutableLiveData.postValue(result)
        }
    }

    fun LoadNewProducts(){
        viewModelScope.async {
            val result = dataRepository.LoadRandomProducts()
            newProducts_mutableLiveData.postValue(result)
        }
    }

    fun LoadProductSizes(productId : String){
        viewModelScope.async {
            val result = dataRepository.LoadProductSizes(productId)
            size_mutableLiveData.postValue(result)
        }
    }
    fun addToCart(bagModel : BagModel){
        viewModelScope.async {
            val result = dataRepository.addToCart(bagModel)
            add_to_cart_mutableLiveData.postValue(result)
        }
    }

    fun removeFromCart(productId : String){
        viewModelScope.async {
            val result = dataRepository.removeFromCart(productId)
            remove_from_cart_mutableLiveData.postValue(result)
        }
    }

    fun loadCartProducts(){
        viewModelScope.async {
            val result = dataRepository.loadCartProducts()
            cart_mutableLiveData.postValue(result)
        }
    }

    fun addToFavourites(productId : String){
        viewModelScope.async {
            val result = dataRepository.addToFavourites(productId)
            add_to_fav_mutableLiveData.postValue(result)
        }
    }
    fun loadFavProducts(){
        viewModelScope.async {
            val result = dataRepository.loadFavProducts()
            favProducts_mutableLiveData.postValue(result)
        }
    }

    fun removeFromFav(productId : String){
        viewModelScope.async {
            val result = dataRepository.removeFromFav(productId)
            remove_from_fav_mutableLiveData.postValue(result)
        }
    }

}