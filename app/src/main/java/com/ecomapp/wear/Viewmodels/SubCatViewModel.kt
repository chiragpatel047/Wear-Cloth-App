package com.ecomapp.wear.Viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecomapp.wear.Models.SubCatModel
import com.ecomapp.wear.Repositories.DataRepository
import com.ecomapp.wear.Repositories.Response
import kotlinx.coroutines.async
import javax.inject.Inject

class SubCatViewModel @Inject constructor(val dataRepository: DataRepository) : ViewModel(){

    var subCat_mutableLiveData = MutableLiveData<Response<ArrayList<SubCatModel>>>()
    val subCat_liveData : LiveData<Response<ArrayList<SubCatModel>>>
        get() = subCat_mutableLiveData

    fun LoadSubCatigories(parentCatName : String,mainCatName : String){
        viewModelScope.async {
            val result = dataRepository.LoadSubCatigories(parentCatName,mainCatName)
            subCat_mutableLiveData.postValue(result)
        }
    }

}