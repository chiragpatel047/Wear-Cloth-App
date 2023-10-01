package com.ecomapp.wear.Viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.wear.Models.BannerModel
import com.ecomapp.wear.Models.MainCatModel
import com.ecomapp.wear.Models.ProuctModel
import com.ecomapp.wear.Models.UserModel
import com.ecomapp.wear.Repositories.DataRepository
import com.ecomapp.wear.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class HomeScreenViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    var bannerData_mutableLiveData = MutableLiveData<Response<ArrayList<BannerModel>>>()
    val bannerData_liveData : LiveData<Response<ArrayList<BannerModel>>>
        get() = bannerData_mutableLiveData

    var mainCat_mutableLiveData = MutableLiveData<Response<ArrayList<MainCatModel>>>()
    val mainCat_liveData : LiveData<Response<ArrayList<MainCatModel>>>
        get() = mainCat_mutableLiveData

    var newProducts_mutableLiveData = MutableLiveData<Response<ArrayList<ProuctModel>>>()
    val newProducts_liveData : LiveData<Response<ArrayList<ProuctModel>>>
        get() = newProducts_mutableLiveData

    var random_mutableLiveData = MutableLiveData<Response<ArrayList<ProuctModel>>>()
    val random_liveData : LiveData<Response<ArrayList<ProuctModel>>>
        get() = random_mutableLiveData

    var add_to_fav_mutableLiveData = MutableLiveData<Response<String>>()
    val add_to_fav_liveData : LiveData<Response<String>>
        get() = add_to_fav_mutableLiveData

    var favProducts_mutableLiveData = MutableLiveData<Response<ArrayList<ProuctModel>>>()
    val favProducts_liveData : LiveData<Response<ArrayList<ProuctModel>>>
        get() = favProducts_mutableLiveData

    var remove_from_fav_mutableLiveData = MutableLiveData<Response<String>>()
    val remove_from_fav_liveData : LiveData<Response<String>>
        get() = remove_from_fav_mutableLiveData

    var user_info_mutableLiveData = MutableLiveData<Response<UserModel>>()
    val user_info_liveData : LiveData<Response<UserModel>>
        get() = user_info_mutableLiveData

    init {
        viewModelScope.async {
            val result = dataRepository.LoadHomeBanners()
            bannerData_mutableLiveData.postValue(result)
        }
    }
    fun LoadMainCategories(catName : String){
        viewModelScope.async {
            val result = dataRepository.LoadMainCategories(catName)
            mainCat_mutableLiveData.postValue(result)
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

    fun getUserInfo(){
        viewModelScope.async {
            val result = dataRepository.getUserInfo()
            user_info_mutableLiveData.postValue(result)
        }
    }
}