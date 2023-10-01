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

class FavViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel() {

    var favProducts_mutableLiveData = MutableLiveData<Response<ArrayList<ProuctModel>>?>()
    val favProducts_liveData : MutableLiveData<Response<ArrayList<ProuctModel>>?>
        get() = favProducts_mutableLiveData

    var add_to_fav_mutableLiveData = MutableLiveData<Response<String>?>()
    val add_to_fav_liveData : MutableLiveData<Response<String>?>
        get() = add_to_fav_mutableLiveData

    var remove_from_fav_mutableLiveData = MutableLiveData<Response<String>?>()
    val remove_from_fav_liveData : MutableLiveData<Response<String>?>
        get() = remove_from_fav_mutableLiveData

    fun loadFavProducts(){
        viewModelScope.async {
            val result = dataRepository.loadFavProducts()
            favProducts_mutableLiveData.postValue(result)
        }
    }

    fun addToFavourites(productId : String){
        viewModelScope.async {
            val result = dataRepository.addToFavourites(productId)
            add_to_fav_mutableLiveData.postValue(result)
        }
    }
    fun removeFromFav(productId : String){
        viewModelScope.async {
            val result = dataRepository.removeFromFav(productId)
            remove_from_fav_mutableLiveData.postValue(result)
        }
    }

    fun clearLiveData(){
        favProducts_mutableLiveData.value = null
        remove_from_fav_mutableLiveData.value = null
        add_to_fav_mutableLiveData.value = null
    }

}