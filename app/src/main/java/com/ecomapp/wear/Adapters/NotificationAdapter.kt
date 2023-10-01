package com.ecomapp.wear.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ecomapp.wear.FullProduct
import com.ecomapp.wear.Models.NotificationModel
import com.ecomapp.wear.MyOrders
import com.ecomapp.wear.R
import com.ecomapp.wear.databinding.BagSingleBinding
import com.ecomapp.wear.databinding.NotificationSingleBinding

class NotificationAdapter(myContext : Context, myList : ArrayList<NotificationModel>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    val context = myContext
    var notificationList = myList

    class NotificationViewHolder(notificationSingleBinding: NotificationSingleBinding) :
        RecyclerView.ViewHolder(notificationSingleBinding.getRoot()) {

        var notificationSingleBinding: NotificationSingleBinding
        init {
            this.notificationSingleBinding = notificationSingleBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding: NotificationSingleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.notification_single, parent, false
        )
        return NotificationAdapter.NotificationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val singleItem = notificationList.get(position)

        holder.notificationSingleBinding.notificationText.text = singleItem.notificationText

        if(singleItem.notificationType.equals("Delivered")){
            holder.notificationSingleBinding.notificationIcon.setImageResource(R.drawable.correct)
        }
        else if(singleItem.notificationType.equals("Submit")){
            holder.notificationSingleBinding.notificationIcon.setImageResource(R.drawable.correct)
        }
        else if(singleItem.notificationType.equals("Cancelled")){
            holder.notificationSingleBinding.notificationIcon.setImageResource(R.drawable.cancelled)
        }

        else if(singleItem.notificationType.equals("Sale")){
            holder.notificationSingleBinding.notificationIcon.setImageResource(R.drawable.sale)
        }

        else if(singleItem.notificationType.equals("New")){
            holder.notificationSingleBinding.notificationIcon.setImageResource(R.drawable.newimg)
        }

        else{
            holder.notificationSingleBinding.notificationIcon.setImageResource(R.drawable.newimg)
        }

        Glide.with(context)
            .load(singleItem.productMainImage)
            .into(holder.notificationSingleBinding.productImage)

        holder.itemView.setOnClickListener {

            val notificationType = singleItem.notificationType
            var tabPosition : Int = 0

            if(notificationType.equals("Submit")){
                tabPosition = 0
            }else if (notificationType.equals("Delivered")){
                tabPosition = 1
            }else if (notificationType.equals("Cancelled")){
                tabPosition = 2
            }

            val intent = Intent(context, MyOrders::class.java)
            intent.putExtra("tabPost",tabPosition)
            context.startActivity(intent)
        }

        holder.notificationSingleBinding.productImage.setOnClickListener {
            val intent = Intent(context, FullProduct::class.java)
            intent.putExtra("productId",singleItem.productId)
            context.startActivity(intent)
        }
    }
}