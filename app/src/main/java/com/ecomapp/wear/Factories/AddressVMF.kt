package com.ecomapp.wear.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ecomapp.wear.Repositories.DataRepository
import com.ecomapp.wear.Viewmodels.AddressViewModel
import javax.inject.Inject

class AddressVMF @Inject constructor(val dataRepository: DataRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddressViewModel(dataRepository) as T
    }
}