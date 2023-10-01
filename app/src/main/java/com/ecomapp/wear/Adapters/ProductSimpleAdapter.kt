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
import com.ecomapp.wear.databinding.ProductSimpleSingleItemBinding

class ProductSimpleAdapter(
    myContext: Context,
    myItemModel: ArrayList<ProuctModel>,
    val removeFromFavFun: (String) -> Unit,
    val addToFavFun: (String) -> Unit
) : RecyclerView.Adapter<ProductSimpleAdapter.ProductSimpleViewHolder>() {

    var context = myContext
    var productList = myItemModel

    class ProductSimpleViewHolder(productSimpleItemBinding: ProductSimpleSingleItemBinding) :
        RecyclerView.ViewHolder(productSimpleItemBinding.getRoot()) {

        var productSimpleItemBinding: ProductSimpleSingleItemBinding

        init {
            this.productSimpleItemBinding = productSimpleItemBinding
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSimpleViewHolder {
        val binding: ProductSimpleSingleItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.product_simple_single_item, parent, false
        )

        return ProductSimpleViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductSimpleViewHolder, position: Int) {

        var singleItem = productList.get(position)

        Glide.with(context).load(singleItem.productMainImage).into(holder.productSimpleItemBinding.itemImage)
        holder.productSimpleItemBinding.itemName.text = singleItem.productTitle
        holder.productSimpleItemBinding.itemText.text = singleItem.productSubTitle

        holder.productSimpleItemBinding.itemOldPrice.text = singleItem.productOldPrice+"₹"
        holder.productSimpleItemBinding.itemNewPrice.text = singleItem.productPrice+"₹"

        holder.productSimpleItemBinding.itemRating.rating = singleItem.rate!!
        holder.productSimpleItemBinding.itemNumberOfRating.text = "("+singleItem.noOfRating.toString().replace(".0","").toString()+")"


        if(Products.favIdList.contains(singleItem.productId)){
            holder.productSimpleItemBinding.itemLike.setImageResource(R.drawable.likeactivated)
            holder.productSimpleItemBinding.itemLike.setColorFilter(ContextCompat.getColor(context,R.color.likecolor))

        }else{
            holder.productSimpleItemBinding.itemLike.setImageResource(R.drawable.likeinactive)
            holder.productSimpleItemBinding.itemLike.setColorFilter(ContextCompat.getColor(context,R.color.unlikecolor))
        }

        holder.productSimpleItemBinding.itemLike.setOnClickListener {
            if(Products.favIdList.contains(singleItem.productId)){
                Products.favIdList.remove(singleItem.productId)
                holder.productSimpleItemBinding.itemLike.setImageResource(R.drawable.likeinactive)
                holder.productSimpleItemBinding.itemLike.setColorFilter(ContextCompat.getColor(context,R.color.unlikecolor))
                removeFromFavFun.invoke(singleItem.productId!!)
            }else{
                Products.favIdList.add(singleItem.productId!!)
                holder.productSimpleItemBinding.itemLike.setImageResource(R.drawable.likeactivated)
                holder.productSimpleItemBinding.itemLike.setColorFilter(ContextCompat.getColor(context,R.color.likecolor))

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