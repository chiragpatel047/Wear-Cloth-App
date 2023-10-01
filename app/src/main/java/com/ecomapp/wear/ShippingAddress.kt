package com.ecomapp.wear

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ecomapp.wear.Factories.AddressVMF
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Viewmodels.AddressViewModel
import com.ecomapp.wear.databinding.ActivityShippingAddressBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class ShippingAddress : AppCompatActivity() {

    lateinit var binding : ActivityShippingAddressBinding

    lateinit var addressViewModel: AddressViewModel

    @Inject
    lateinit var addressVMF: AddressVMF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_shipping_address)
        supportActionBar?.hide()

        binding.imgBack.setOnClickListener {
            finish()
        }

        addressViewModel = ViewModelProvider(this,addressVMF).get(AddressViewModel::class.java)
        addressViewModel.LoadAddress()

        addressViewModel.address_liveData.observe(this,{
            when(it){
                is Response.Sucess ->{
                    binding.minPrice.text=it.data?.name
                    binding.middleAddress.text=it.data?.address
                    binding.minPrice4.text=it.data?.city+", "+it.data?.state+" "+it.data?.zipCode
                }
                is Response.Error ->{

                }
                is Response.Loading ->{

                }
            }
        })



    }
}