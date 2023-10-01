package com.ecomapp.wear.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecomapp.wear.FullProduct
import com.ecomapp.wear.Models.BagModel
import com.ecomapp.wear.R
import com.ecomapp.wear.databinding.BagSingleBinding

class BagAdapter(myContext: Context, myBagModel: ArrayList<BagModel>, val removeFromBagFun: (String) -> Unit) :
    RecyclerView.Adapter<BagAdapter.BagViewHolder>() {

    var context = myContext
    var bagProductList = myBagModel

    class BagViewHolder(bagSingleBinding: BagSingleBinding) :
        RecyclerView.ViewHolder(bagSingleBinding.getRoot()) {

        var bagSingleBinding: BagSingleBinding
        init {
            this.bagSingleBinding = bagSingleBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BagViewHolder {

        val binding: BagSingleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.bag_single, parent, false
        )
        return BagViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return bagProductList.size
    }

    override fun onBindViewHolder(holder: BagViewHolder, position: Int) {

        var singelItem = bagProductList.get(position)

        Glide.with(context).load(singelItem.productMainImage).into(holder.bagSingleBinding.itemImage)
        holder.bagSingleBinding.itemTitle.text = singelItem.productTitle
        holder.bagSingleBinding.itemSize.text = "Size : "+singelItem.productSelectedSize
        holder.bagSingleBinding.itemPrice.text = singelItem.productPrice.toString()+"₹"

        holder.itemView.setOnClickListener {

            val intent = Intent(context, FullProduct::class.java)
            intent.putExtra("productId",singelItem.productId)
            context.startActivity(intent)

        }

        holder.bagSingleBinding.threeDots.setOnClickListener {
            bagProductList.remove(singelItem)
            notifyDataSetChanged()
            removeFromBagFun.invoke(singelItem.productId!!)
        }
    }
}