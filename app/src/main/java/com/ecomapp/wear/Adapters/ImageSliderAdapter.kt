package com.ecomapp.wear.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.ecomapp.wear.Models.ProductImageModel
import com.ecomapp.wear.R
import com.ecomapp.wear.databinding.ImageSliderSingleBinding
import com.smarteist.autoimageslider.SliderViewAdapter

class ImageSliderAdapter(myContext: Context, myImageList: ArrayList<ProductImageModel>) : SliderViewAdapter<ImageSliderAdapter.ImageViewHolder>() {

    var context = myContext
    var imageList = myImageList

    class ImageViewHolder(imageSliderBinding : ImageSliderSingleBinding)
        : SliderViewAdapter.ViewHolder(imageSliderBinding.root) {

        var imageSliderBinding: ImageSliderSingleBinding

        init {
            this.imageSliderBinding = imageSliderBinding
        }
    }

    override fun getCount(): Int {
        return imageList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ImageViewHolder {

        val binding: ImageSliderSingleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent!!.context),
            R.layout.image_slider_single, parent, false
        )

        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ImageViewHolder?, position: Int) {
        var singleItem = imageList.get(position)
        Glide.with(context).load(singleItem.imageUrl).into(viewHolder?.imageSliderBinding!!.sliderImage)

    }
}