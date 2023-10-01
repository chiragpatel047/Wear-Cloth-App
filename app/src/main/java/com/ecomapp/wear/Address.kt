package com.ecomapp.wear

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ecomapp.wear.Factories.AddressVMF
import com.ecomapp.wear.Models.BagModel
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Viewmodels.AddressViewModel
import com.ecomapp.wear.databinding.ActivityAddressBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class Address : AppCompatActivity() {

    lateinit var binding : ActivityAddressBinding

    lateinit var addressViewModel: AddressViewModel

    @Inject
    lateinit var addressVMF: AddressVMF

    companion object{
        var buyingItem : ArrayList<BagModel> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_address)
        supportActionBar?.hide()

        binding.imgBack.setOnClickListener {
            finish()
        }

        addressViewModel = ViewModelProvider(this,addressVMF).get(AddressViewModel::class.java)
        addressViewModel.LoadAddress()

        val from = intent.getStringExtra("from")

        if(from.equals("single")){
            buyingItem = FullProduct.buyingItem
        }else{
            buyingItem = Bag.bagList
        }

        addressViewModel.address_liveData.observe(this,{
            when(it){
                is Response.Sucess ->{
                    binding.addName.editText!!.setText(it.data!!.name)
                    binding.addAddress.editText!!.setText(it.data!!.address)
                    binding.addCity.editText!!.setText(it.data!!.city)
                    binding.addState.editText!!.setText(it.data!!.state)
                    binding.addZip.editText!!.setText(it.data!!.zipCode)
                    binding.addPhoneNo.editText!!.setText(it.data!!.phoneNO)

                }
                is Response.Error ->{

                }
                is Response.Loading ->{

                }
            }
        })

        binding.saveAddress.setOnClickListener {

            val name = binding.addName.editText?.text.toString()
            val address = binding.addAddress.editText?.text.toString()
            val city = binding.addCity.editText?.text.toString()
            val state = binding.addState.editText?.text.toString()
            val zipCode = binding.addZip.editText?.text.toString()
            val phoneNO = binding.addPhoneNo.editText?.text.toString()

            if(name.isEmpty() ||address.isEmpty() || city.isEmpty()
                || state.isEmpty() || zipCode.isEmpty() || phoneNO.isEmpty() ){

                Toast.makeText(this,"Please fill all detail",Toast.LENGTH_LONG).show()
                return@setOnClickListener

            }

            val intent = Intent(this,Preview::class.java)

            intent.putExtra("name",name)
            intent.putExtra("address",address)
            intent.putExtra("city",city)
            intent.putExtra("state",state)
            intent.putExtra("zipCode",zipCode)
            intent.putExtra("phoneNO",phoneNO)

            startActivity(intent)
        }
    }
}