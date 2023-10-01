package com.ecomapp.wear.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ecomapp.wear.Repositories.DataRepository
import com.ecomapp.wear.Viewmodels.ProductsViewModel
import javax.inject.Inject

class ProductVMF @Inject constructor(val dataRepository: DataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductsViewModel(dataRepository) as T
    }
}