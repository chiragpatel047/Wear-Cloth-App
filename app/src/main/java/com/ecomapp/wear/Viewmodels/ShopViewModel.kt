package com.ecomapp.wear.Viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.wear.Models.MainCatModel
import com.ecomapp.wear.Repositories.DataRepository
import com.ecomapp.wear.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class ShopViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel(){

    var mainCat_mutableLiveData = MutableLiveData<Response<ArrayList<MainCatModel>>?>()
    val mainCat_liveData : MutableLiveData<Response<ArrayList<MainCatModel>>?>
        get() = mainCat_mutableLiveData

    fun LoadMainCategories(catName : String){
        viewModelScope.async {
            val result = dataRepository.LoadMainCategories(catName)
            mainCat_mutableLiveData.postValue(result)
        }
    }

    fun clearLiveData(){
        mainCat_mutableLiveData.value = null
    }

}