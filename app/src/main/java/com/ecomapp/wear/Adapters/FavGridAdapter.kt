package com.ecomapp.wear.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecomapp.wear.FullProduct
import com.ecomapp.wear.Models.ProuctModel
import com.ecomapp.wear.R
import com.ecomapp.wear.databinding.ProductGridSingleItemBinding

class FavGridAdapter(
    myContext: Context,
    myItemModel: ArrayList<ProuctModel>,
    val removeFromFavFun : (String) -> Unit
) :
    RecyclerView.Adapter<FavGridAdapter.ProductGridViewHolder>(){

    var context = myContext
    var itemModel = myItemModel

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
        return  itemModel.size
    }

    override fun onBindViewHolder(holder: ProductGridViewHolder, position: Int) {

        var singleItem = itemModel.get(position)

        Glide.with(context).load(singleItem.productMainImage).into(holder.productGridItemBinding.itemImage)

        holder.productGridItemBinding.itemName.text = singleItem.productTitle
        holder.productGridItemBinding.itemText.text = singleItem.productSubTitle

        holder.productGridItemBinding.itemOldPrice.text = singleItem.productOldPrice
        holder.productGridItemBinding.itemNewPrice.text = singleItem.productPrice

        holder.productGridItemBinding.itemRating.rating = singleItem.rate!!
        holder.productGridItemBinding.itemNumberOfRating.text = "("+singleItem.noOfRating.toString().replace(".0","").toString()+")"


        holder.productGridItemBinding.removeImage.visibility = View.VISIBLE
        holder.productGridItemBinding.itemLike.visibility = View.GONE

        holder.productGridItemBinding.removeImage.setOnClickListener {
            itemModel.remove(singleItem)
            notifyDataSetChanged()
            removeFromFavFun.invoke(singleItem.productId!!)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context,FullProduct::class.java)
            intent.putExtra("productId",singleItem.productId)
            context.startActivity(intent)
        }
    }

}