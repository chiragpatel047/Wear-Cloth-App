package com.ecomapp.wear.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ecomapp.wear.R
import com.ecomapp.wear.databinding.ReviewImageSingleBinding

class ReviewImageAdapter(myContext: Context, myImageList: ArrayList<Int>) : RecyclerView.Adapter<ReviewImageAdapter.RatingViewHolder>(){

    var context = myContext
    var imageList = myImageList

    class RatingViewHolder(reviewImageBinding: ReviewImageSingleBinding) :
        RecyclerView.ViewHolder(reviewImageBinding.getRoot()) {

        var reviewImageBinding: ReviewImageSingleBinding
        init {
            this.reviewImageBinding = reviewImageBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {

        val binding: ReviewImageSingleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.review_image_single, parent, false
        )
        return RatingViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        var singleImage = imageList.get(position)

        holder.reviewImageBinding.reviewImage.setImageResource(singleImage)

    }

}