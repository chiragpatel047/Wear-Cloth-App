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
import com.ecomapp.wear.databinding.ProductSimpleSingleItemBinding

class FavSimpleAdapter(
    myContext: Context,
    myItemModel: ArrayList<ProuctModel>,
    val removeFromFavFun: (String) -> Unit
) :
    RecyclerView.Adapter<FavSimpleAdapter.ProductSimpleViewHolder>() {

    var context = myContext
    var itemModel = myItemModel

    class ProductSimpleViewHolder(favSimpleItemBinding: ProductSimpleSingleItemBinding) :
        RecyclerView.ViewHolder(favSimpleItemBinding.getRoot()) {

        var favSimpleItemBinding: ProductSimpleSingleItemBinding

        init {
            this.favSimpleItemBinding = favSimpleItemBinding
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
        return itemModel.size
    }

    override fun onBindViewHolder(holder: ProductSimpleViewHolder, position: Int) {

        var singleItem = itemModel.get(position)

        Glide.with(context).load(singleItem.productMainImage).into(holder.favSimpleItemBinding.itemImage)

        holder.favSimpleItemBinding.itemName.text = singleItem.productTitle
        holder.favSimpleItemBinding.itemText.text = singleItem.productSubTitle

        holder.favSimpleItemBinding.itemOldPrice.text = singleItem.productOldPrice+"₹"
        holder.favSimpleItemBinding.itemNewPrice.text = singleItem.productPrice+"₹"

        holder.favSimpleItemBinding.itemRating.rating = singleItem.rate!!
        holder.favSimpleItemBinding.itemNumberOfRating.text = "("+singleItem.noOfRating.toString().replace(".0","").toString()+")"


        holder.favSimpleItemBinding.removeImage.visibility = View.VISIBLE
        holder.favSimpleItemBinding.itemLike.visibility = View.GONE

        holder.favSimpleItemBinding.removeImage.setOnClickListener {
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