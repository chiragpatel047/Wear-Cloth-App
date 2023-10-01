package com.ecomapp.wear

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecomapp.wear.Adapters.RatingAdapter
import com.ecomapp.wear.Factories.RatingVMF
import com.ecomapp.wear.Models.RatingModel
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Viewmodels.RatingViewModel
import com.ecomapp.wear.databinding.ActivityRatingsBinding
import com.ecomapp.wear.databinding.WriteReviewBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint()
class Ratings : AppCompatActivity() {

    lateinit var binding : ActivityRatingsBinding
    lateinit var ratingList : ArrayList<RatingModel>
    lateinit var ratingAdapter: RatingAdapter

    lateinit var sendReviewView : WriteReviewBottomsheetBinding
    lateinit var sendReviewBottomSheet : BottomSheetDialog

    lateinit var ratingViewModel: RatingViewModel

    @Inject
    lateinit var ratingVMF: RatingVMF

    var userName : String =""
    var userPhoto : String =""
    var dateInStr : String= ""
    var content : String= ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_ratings)
        supportActionBar?.hide()

        binding.imgBack.setOnClickListener {
            finish()
        }

        val productId = intent.getStringExtra("productId")!!

        ratingViewModel = ViewModelProvider(this,ratingVMF).get(RatingViewModel::class.java)
        ratingViewModel.getUserInfo()

        ratingViewModel.LoadProductDetails(productId)
        ratingViewModel.getRating(productId)

        var loadingView = LayoutInflater.from(this).inflate(R.layout.loading_dialog,null)
        var loadingDialog = AlertDialog.Builder(this).setView(loadingView).create()
        loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        loadingDialog.setCancelable(false)

        ratingViewModel.rate_liveData.observe(this,{
            when (it){
                is Response.Sucess->{
                    ratingViewModel.getRating(productId)
                    ratingViewModel.LoadProductDetails(productId)
                }

                is Response.Error -> {

                }

                is Response.Loading -> {

                }
            }
        })

        ratingViewModel.user_liveData.observe(this,{
            when (it){
                is Response.Sucess->{
                    userName = it.data?.username!!
                    userPhoto = it.data?.userImage!!
                }

                is Response.Error -> {

                }

                is Response.Loading -> {

                }
            }
        })

        ratingList = ArrayList()
        ratingAdapter = RatingAdapter(this,ratingList)

        binding.reviewRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.reviewRecyclerview.adapter = ratingAdapter

        ratingViewModel.allrate_liveData.observe(this,{
            when (it){
                is Response.Sucess->{
                    ratingList.clear()
                    for(single in it.data!!){
                        ratingList.add(0,single)
                    }
                    binding.loadingAnim2.visibility = View.GONE
                    it.data.clear()

                    if(ratingList.isEmpty()){
                        binding.noFoundText2.visibility = View.VISIBLE
                    }else{
                        binding.noFoundText2.visibility = View.GONE
                    }
                    loadingDialog.dismiss()
                    ratingAdapter.notifyDataSetChanged()
                }

                is Response.Error -> {

                }

                is Response.Loading -> {

                }
            }
        })

        ratingViewModel.productDetails_liveData.observe(this,{
            when(it){
                is Response.Sucess ->{

                    binding.fiveItemRating.rating = it.data?.rate!!
                    binding.totalUserRated.text = it.data?.noOfRating.toString().replace(".0","")+" ratings"

                    binding.totalRatingText.text=it.data?.rate!!.toString()

                }
                is Response.Error -> {

                }
                is Response.Loading -> {

                }
            }
        })

        sendReviewView = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.write_review_bottomsheet,null,false)

        sendReviewBottomSheet = BottomSheetDialog(this,R.style.BottomSheetDialogTheme)
        sendReviewBottomSheet.setContentView(sendReviewView.root)
        sendReviewBottomSheet.create()

        binding.writeReview.setOnClickListener {
            sendReviewBottomSheet.show()
        }

        sendReviewView.reviewSubmit.setOnClickListener {

            val rating = sendReviewView.fiveItemRating.rating

            if(rating==0F){
                Toast.makeText(this,"Please rate first",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val simpleDate = SimpleDateFormat("dd/MM/yyyy")
            dateInStr = simpleDate.format(Date())

            content = sendReviewView.editTextTextMultiLine.text.toString()

            ratingViewModel.rateProduct(RatingModel(userPhoto,userName,rating,dateInStr,content),productId)
            sendReviewBottomSheet.dismiss()
            loadingDialog.show()

        }
    }


}