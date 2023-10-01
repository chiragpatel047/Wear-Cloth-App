package com.ecomapp.wear

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.ecomapp.wear.databinding.ActivityFilterBinding

class Filter : AppCompatActivity() {

    lateinit var binding : ActivityFilterBinding

    var seekBarMinValue = MutableLiveData<Float>(500.0F)
    var seekBarMaxValue = MutableLiveData<Float>(5000.0F)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_filter)
        supportActionBar?.hide()

        seekBarMinValue.observe(this,{
            binding.fullName.text = it.toInt().toString()+"₹"
        })

        seekBarMaxValue.observe(this,{
            binding.maxPrice.text = it.toInt().toString()+"₹"
        })

        binding.rangeSeekBar.addOnChangeListener { slider, value, fromUser ->
            seekBarMinValue.value = slider.values.get(0)
            seekBarMaxValue.value = slider.values.get(1)
        }

        binding.filterApply.setOnClickListener {
            finish()
        }

        binding.filterDiscard.setOnClickListener {
            finish()
        }
    }
}