package com.ecomapp.wear.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ecomapp.wear.Repositories.UserRepository
import com.ecomapp.wear.Viewmodels.ForgetPassViewModel
import javax.inject.Inject

class ForgetPassVMF @Inject constructor(val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ForgetPassViewModel(userRepository) as T
    }
}