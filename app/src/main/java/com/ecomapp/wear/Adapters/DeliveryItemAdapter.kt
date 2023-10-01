package com.ecomapp.wear.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecomapp.wear.FullProduct
import com.ecomapp.wear.Models.OrderModel
import com.ecomapp.wear.R
import com.ecomapp.wear.databinding.DeliveryItemSingleBinding
import com.ecomapp.wear.databinding.DetailsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class DeliveryItemAdapter(myContext : Context,myDeliveryItemModel: ArrayList<OrderModel>,myOrderType : String) : RecyclerView.Adapter<DeliveryItemAdapter.DeliveryItemViewHolder>() {

    var context = myContext
    var deliveryItemArrayList = myDeliveryItemModel
    var orderType = myOrderType

    class DeliveryItemViewHolder(devliveryItemBinding: DeliveryItemSingleBinding) :
        RecyclerView.ViewHolder(devliveryItemBinding.getRoot()) {

        var devliveryItemBinding: DeliveryItemSingleBinding
        init {
            this.devliveryItemBinding = devliveryItemBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryItemViewHolder {

        val binding: DeliveryItemSingleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.delivery_item_single, parent, false
        )
        return DeliveryItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return deliveryItemArrayList.size
    }

    override fun onBindViewHolder(holder: DeliveryItemViewHolder, position: Int) {

        var singleItem = deliveryItemArrayList.get(position)

        holder.devliveryItemBinding.orderno.text = "Order no : "+singleItem.orderId?.substring(6,14)
        holder.devliveryItemBinding.trackingno.text = singleItem.orderId
        holder.devliveryItemBinding.amount.text = singleItem.productPrice+"₹"
        holder.devliveryItemBinding.quantity.text = "1"

        if(orderType.equals("pendingOrders")){
            holder.devliveryItemBinding.status.text = "Processing"
            holder.devliveryItemBinding.status.setTextColor(Color.parseColor("#FFBA49"))
        }else if(orderType.equals("deliveredOrders")){
            holder.devliveryItemBinding.status.text = "Delivered"
            holder.devliveryItemBinding.status.setTextColor(Color.parseColor("#55D85A"))
        }else{
            holder.devliveryItemBinding.status.text = "Cancelled"
            holder.devliveryItemBinding.status.setTextColor(Color.parseColor("#FF3E3E"))
        }

        holder.itemView.setOnClickListener {

            val bottomSheetView : DetailsBottomSheetBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.details_bottom_sheet,null,false)

            var bottomSheet = BottomSheetDialog(context,R.style.BottomSheetDialogTheme)
            bottomSheet.setContentView(bottomSheetView.root)
            bottomSheet.create()

            bottomSheet.show()

            Glide.with(context).load(singleItem.productImage).into(bottomSheetView.itemImage)
            bottomSheetView.itemSubTitle.text = singleItem.productTitle
            bottomSheetView.itemColor.text = "Size : " + singleItem.selectedSize
            bottomSheetView.trackingno6.text = singleItem.productPrice+"₹"
            bottomSheetView.fullName.text = singleItem.name
            bottomSheetView.middleAddress.text = singleItem.address
            bottomSheetView.topAddress.text = singleItem.city+", "+singleItem.state+" "+singleItem.zipCode
            bottomSheetView.itemPrice.text = singleItem.productPrice+"₹"
            bottomSheetView.trackingno3.text = singleItem.phoneNO
            bottomSheetView.deliveryDate.text = singleItem.deliveryDate

            bottomSheetView.productLayout.setOnClickListener {
                val intent = Intent(context, FullProduct::class.java)
                intent.putExtra("productId",singleItem.productId)
                context.startActivity(intent)
            }

            bottomSheetView.continueShopping.setOnClickListener {
                bottomSheet.dismiss()
            }
        }
    }

}