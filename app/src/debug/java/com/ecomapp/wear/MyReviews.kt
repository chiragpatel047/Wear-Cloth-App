package com.ecomapp.wear

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecomapp.wear.Adapters.RatingAdapter
import com.ecomapp.wear.Models.RatingModel
import com.ecomapp.wear.databinding.ActivityMyReviewsBinding

class MyReviews : AppCompatActivity() {

    lateinit var binding : ActivityMyReviewsBinding
    lateinit var ratingList : ArrayList<RatingModel>
    lateinit var ratingAdapter: RatingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_reviews)
        supportActionBar?.hide()

        binding.reviewRecv.layoutManager = LinearLayoutManager(this)

        ratingList = ArrayList()
        ratingAdapter = RatingAdapter(this,ratingList)

        binding.reviewRecv.adapter = ratingAdapter

        var imageArrayList = ArrayList<Int>()

        imageArrayList.add(R.drawable.temp_review_one)
        imageArrayList.add(R.drawable.temp_review_two)
        imageArrayList.add(R.drawable.temp_review_three)
        imageArrayList.add(R.drawable.temp_review_four)

        ratingAdapter.notifyDataSetChanged()

    }
}