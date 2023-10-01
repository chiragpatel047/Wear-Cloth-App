package com.ecomapp.wear

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import com.ecomapp.wear.databinding.ActivityOrderDoneBinding
import kotlin.random.Random

class OrderDone : AppCompatActivity() {

    lateinit var binding : ActivityOrderDoneBinding
    val  CHANNEL_ID="ORDER"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_done)
        supportActionBar?.hide()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt()

        val intent = Intent(this,Notifcations::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,0,intent,
            FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationChannel(notificationManager)
        }

        val notification = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("Submitted successfully")
            .setContentText("Your order is submitted successfully")
            .setSmallIcon(R.mipmap.wear_app_icon)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId,notification)

        binding.continueShopping.setOnClickListener {
            val intent = Intent(this,HomeScreen::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun notificationChannel(notificationManager: NotificationManager){
        val channelName = "ORDER_CHANNEL"
        val channel = NotificationChannel(CHANNEL_ID,channelName,IMPORTANCE_HIGH).apply {
            description = "ORDER_CHANNEL_DESCRIPTION"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }
}