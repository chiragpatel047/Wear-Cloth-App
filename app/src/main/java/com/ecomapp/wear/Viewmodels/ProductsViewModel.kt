package com.ecomapp.wear.Viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.wear.Models.ProuctModel
import com.ecomapp.wear.Repositories.DataRepository
import com.ecomapp.wear.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class ProductsViewModel @Inject constructor (val dataRepository: DataRepository) : ViewModel() {

    var products_mutableLiveData = MutableLiveData<Response<ArrayList<ProuctModel>>>()
    val products_liveData : LiveData<Response<ArrayList<ProuctModel>>>
        get() = products_mutableLiveData

    var banner_products_mutableLiveData = MutableLiveData<Response<ArrayList<ProuctModel>>>()
    val banner_products_liveData : LiveData<Response<ArrayList<ProuctModel>>>
        get() = banner_products_mutableLiveData

    var add_to_fav_mutableLiveData = MutableLiveData<Response<String>>()
    val add_to_fav_liveData : LiveData<Response<String>>
        get() = add_to_fav_mutableLiveData

    var favProducts_mutableLiveData = MutableLiveData<Response<ArrayList<ProuctModel>>>()
    val favProducts_liveData : LiveData<Response<ArrayList<ProuctModel>>>
        get() = favProducts_mutableLiveData

    var remove_from_fav_mutableLiveData = MutableLiveData<Response<String>>()
    val remove_from_fav_liveData : LiveData<Response<String>>
        get() = remove_from_fav_mutableLiveData

    var newProducts_mutableLiveData = MutableLiveData<Response<ArrayList<ProuctModel>>>()
    val newProducts_liveData : LiveData<Response<ArrayList<ProuctModel>>>
        get() = newProducts_mutableLiveData

    var random_mutableLiveData = MutableLiveData<Response<ArrayList<ProuctModel>>>()
    val random_liveData : LiveData<Response<ArrayList<ProuctModel>>>
        get() = random_mutableLiveData

    var search_mutableLiveData = MutableLiveData<Response<ArrayList<ProuctModel>>>()
    val search_liveData : LiveData<Response<ArrayList<ProuctModel>>>
        get() = search_mutableLiveData

    fun LoadProducts(parentCatName : String,mainCatName : String,subCatName : String){
        viewModelScope.async {
            val result = dataRepository.LoadProducts(parentCatName,mainCatName,subCatName)
            products_mutableLiveData.postValue(result)
        }
    }

    fun LoadNewProducts(){
        viewModelScope.async {
            val result = dataRepository.LoadAllProducts()
            newProducts_mutableLiveData.postValue(result)
        }
    }

    fun LoadRandomProducts(){
        viewModelScope.async {
            val result = dataRepository.LoadRandomProducts()
            random_mutableLiveData.postValue(result)
        }
    }

    fun LoadBannersProducts(loadUsing : String){
        viewModelScope.async {
            val result = dataRepository.LoadBannersProducts(loadUsing)
            banner_products_mutableLiveData.postValue(result)
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

    fun LoadAllProducts(){
        viewModelScope.async {
            val result = dataRepository.LoadAllProducts()
            search_mutableLiveData.postValue(result)
        }
    }
}