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
import com.ecomapp.wear.Home
import com.ecomapp.wear.Models.ProuctModel
import com.ecomapp.wear.R
import com.ecomapp.wear.databinding.SingleItemBinding

class ItemAdapters(
    myContext: Context,
    myItemModel: ArrayList<ProuctModel>,
    val removeFromFavFun: (String) -> Unit,
    val addToFavFun: (String) -> Unit)
    : RecyclerView.Adapter<ItemAdapters.ItemViewHolder>() {

    var context = myContext
    var productList = myItemModel

    class ItemViewHolder(singleItemBinding: SingleItemBinding) :
        RecyclerView.ViewHolder(singleItemBinding.getRoot()) {

        var singleItemBinding: SingleItemBinding
        init {
            this.singleItemBinding = singleItemBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val binding: SingleItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.single_item, parent, false
        )

        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        var singleItem = productList.get(position)

        Glide.with(context).load(singleItem.productMainImage).into(holder.singleItemBinding.itemImage)
        holder.singleItemBinding.itemName.text = singleItem.productTitle
        holder.singleItemBinding.itemText.text = singleItem.productSubTitle

        holder.singleItemBinding.itemOldPrice.text = singleItem.productOldPrice+"₹"
        holder.singleItemBinding.itemNewPrice.text = singleItem.productPrice+"₹"

        holder.singleItemBinding.itemRating.rating = singleItem.rate!!
        holder.singleItemBinding.itemNumberOfRating.text = "("+singleItem.noOfRating.toString().replace(".0","").toString()+")"

        if(Home.favIdList.contains(singleItem.productId)){
            holder.singleItemBinding.itemLike.setImageResource(R.drawable.likeactivated)
            holder.singleItemBinding.itemLike.setColorFilter(ContextCompat.getColor(context,R.color.likecolor))
        }else{
            holder.singleItemBinding.itemLike.setImageResource(R.drawable.likeinactive)
            holder.singleItemBinding.itemLike.setColorFilter(ContextCompat.getColor(context,R.color.unlikecolor))
        }

        holder.singleItemBinding.itemLike.setOnClickListener {
            if(Home.favIdList.contains(singleItem.productId)){
                Home.favIdList.remove(singleItem.productId)
                holder.singleItemBinding.itemLike.setImageResource(R.drawable.likeinactive)
                holder.singleItemBinding.itemLike.setColorFilter(ContextCompat.getColor(context,R.color.unlikecolor))
                removeFromFavFun.invoke(singleItem.productId!!)
            }else{
                Home.favIdList.add(singleItem.productId!!)
                holder.singleItemBinding.itemLike.setImageResource(R.drawable.likeactivated)
                holder.singleItemBinding.itemLike.setColorFilter(ContextCompat.getColor(context,R.color.likecolor))

                addToFavFun.invoke(singleItem.productId!!)
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context,FullProduct::class.java)
            intent.putExtra("productId",singleItem.productId)
            context.startActivity(intent)
        }
    }
}