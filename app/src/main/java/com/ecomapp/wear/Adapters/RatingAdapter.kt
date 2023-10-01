package com.ecomapp.wear.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecomapp.wear.Models.RatingModel
import com.ecomapp.wear.R
import com.ecomapp.wear.databinding.RatingSingleBinding

class RatingAdapter(myContext: Context, myItemModel: ArrayList<RatingModel>) : RecyclerView.Adapter<RatingAdapter.RatingViewHolder>() {

    var context = myContext
    var ratingList = myItemModel

    class RatingViewHolder(ratingSingleBinding: RatingSingleBinding) :
        RecyclerView.ViewHolder(ratingSingleBinding.getRoot()) {

        var ratingSingleBinding: RatingSingleBinding

        init {
            this.ratingSingleBinding = ratingSingleBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {

        val binding: RatingSingleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.rating_single, parent, false
        )
        return RatingViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return ratingList.size
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {

        var singleReview = ratingList.get(position)

        Glide.with(context).load(singleReview.userPhoto).placeholder(R.drawable.bigprofileactivated).into(holder.ratingSingleBinding.userImage)
        holder.ratingSingleBinding.username.text = singleReview.username

        holder.ratingSingleBinding.userRating.rating = singleReview.userRating!!

        holder.ratingSingleBinding.userReviewDate.text = singleReview.reviewDate
        holder.ratingSingleBinding.userMainReview.text = singleReview.reviewContent

    }
}