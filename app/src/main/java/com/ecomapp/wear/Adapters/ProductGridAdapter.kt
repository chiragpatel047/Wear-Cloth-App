package com.ecomapp.wear.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecomapp.wear.FullProduct
import com.ecomapp.wear.Models.ProuctModel
import com.ecomapp.wear.Products
import com.ecomapp.wear.R
import com.ecomapp.wear.databinding.ProductGridSingleItemBinding

class ProductGridAdapter(
    myContext: Context,
    myItemModel: ArrayList<ProuctModel>,
    val removeFromFavFun: (String) -> Unit,
    val addToFavFun: (String) -> Unit
) :
    RecyclerView.Adapter<ProductGridAdapter.ProductGridViewHolder>() {

    var context = myContext
    var productList = myItemModel

    class ProductGridViewHolder(productGridItemBinding: ProductGridSingleItemBinding) :
        RecyclerView.ViewHolder(productGridItemBinding.getRoot()) {

        var productGridItemBinding: ProductGridSingleItemBinding

        init {
            this.productGridItemBinding = productGridItemBinding
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductGridViewHolder {
        val binding: ProductGridSingleItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.product_grid_single_item, parent, false
        )

        return ProductGridViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductGridViewHolder, position: Int) {

        var singleItem = productList.get(position)

        Glide.with(context).load(singleItem.productMainImage).into(holder.productGridItemBinding.itemImage)
        holder.productGridItemBinding.itemName.text = singleItem.productTitle
        holder.productGridItemBinding.itemText.text = singleItem.productSubTitle

        holder.productGridItemBinding.itemOldPrice.text = singleItem.productOldPrice+"₹"
        holder.productGridItemBinding.itemNewPrice.text = singleItem.productPrice+"₹"

        holder.productGridItemBinding.itemRating.rating = singleItem.rate!!
        holder.productGridItemBinding.itemNumberOfRating.text = "("+singleItem.noOfRating.toString().replace(".0","").toString()+")"


        if(Products.favIdList.contains(singleItem.productId)){
            holder.productGridItemBinding.itemLike.setImageResource(R.drawable.likeactivated)
            holder.productGridItemBinding.itemLike.setColorFilter(ContextCompat.getColor(context,R.color.likecolor))

        }else{
            holder.productGridItemBinding.itemLike.setImageResource(R.drawable.likeinactive)
            holder.productGridItemBinding.itemLike.setColorFilter(ContextCompat.getColor(context,R.color.unlikecolor))
        }

        holder.productGridItemBinding.itemLike.setOnClickListener {
            if(Products.favIdList.contains(singleItem.productId)){
                Products.favIdList.remove(singleItem.productId)
                holder.productGridItemBinding.itemLike.setImageResource(R.drawable.likeinactive)
                holder.productGridItemBinding.itemLike.setColorFilter(ContextCompat.getColor(context,R.color.unlikecolor))
                removeFromFavFun.invoke(singleItem.productId!!)
            }else{
                Products.favIdList.add(singleItem.productId!!)
                holder.productGridItemBinding.itemLike.setImageResource(R.drawable.likeactivated)
                holder.productGridItemBinding.itemLike.setColorFilter(ContextCompat.getColor(context,R.color.likecolor))

                addToFavFun.invoke(singleItem.productId!!)
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, FullProduct::class.java)
            intent.putExtra("productId",singleItem.productId)
            context.startActivity(intent)
        }
    }
}