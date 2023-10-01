package com.ecomapp.wear

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ecomapp.wear.Factories.TermsConditionVMF
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Viewmodels.TermsConditionVIewModel
import com.ecomapp.wear.databinding.ActivityMyReviewItemsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class MyReviewItems : AppCompatActivity() {

    lateinit var binding: ActivityMyReviewItemsBinding

    lateinit var termsConditionVIewModel: TermsConditionVIewModel

    @Inject
    lateinit var termsConditionVMF: TermsConditionVMF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_review_items)
        supportActionBar?.hide()

        binding.imgBack.setOnClickListener {
            finish()
        }

        termsConditionVIewModel = ViewModelProvider(this,termsConditionVMF).get(TermsConditionVIewModel::class.java)
        termsConditionVIewModel.getTNC()

        termsConditionVIewModel.tnc_liveData.observe(this,{
            when(it){
                is Response.Sucess -> {
                    binding.content.text = it.data?.orderId
                }
                is Response.Error -> {

                }
                is Response.Loading -> {

                }

            }
        })

    }
}