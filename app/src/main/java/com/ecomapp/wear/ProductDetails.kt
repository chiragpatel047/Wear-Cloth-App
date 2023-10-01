package com.ecomapp.wear

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ecomapp.wear.databinding.ActivityProductDetailsBinding

class ProductDetails : AppCompatActivity() {

    lateinit var binding : ActivityProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_product_details)
        supportActionBar?.hide()

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.itemDesc2.text = intent.getStringExtra("Desc")

    }
}