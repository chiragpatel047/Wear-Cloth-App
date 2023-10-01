package com.ecomapp.wear.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ecomapp.wear.Models.MyReviewItemModel
import com.ecomapp.wear.R
import com.ecomapp.wear.databinding.MyreviewsingleitemBinding

class MyReviewItemAdapter(myContext: Context, myReviewItemModel: ArrayList<MyReviewItemModel>)
    : RecyclerView.Adapter<MyReviewItemAdapter.MyReviewViewHolder>(){

    var context = myContext
    var reviewItems = myReviewItemModel

    class MyReviewViewHolder(myReviewBinding: MyreviewsingleitemBinding) :
        RecyclerView.ViewHolder(myReviewBinding.getRoot()) {

        var myReviewBinding: MyreviewsingleitemBinding
        init {
            this.myReviewBinding = myReviewBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReviewViewHolder {

        val binding: MyreviewsingleitemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.myreviewsingleitem, parent, false
        )
        return MyReviewViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return reviewItems.size
    }

    override fun onBindViewHolder(holder: MyReviewViewHolder, position: Int) {
        var singleItem = reviewItems.get(position)

        holder.myReviewBinding.itemImage.setImageResource(singleItem.main_image!!)
        holder.myReviewBinding.itemText.text = singleItem.title
        holder.myReviewBinding.itemDesc.text = singleItem.desc

        holder.itemView.setOnClickListener {
//            val intent = Intent(context, MyReviews::class.java)
//            context.startActivity(intent)
        }
    }
}