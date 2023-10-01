package com.ecomapp.wear

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecomapp.wear.Adapters.NotificationAdapter
import com.ecomapp.wear.Factories.NotificationVMF
import com.ecomapp.wear.Models.NotificationModel
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Viewmodels.NotificationViewModel
import com.ecomapp.wear.databinding.ActivityNotifcationsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class Notifcations : AppCompatActivity() {

    lateinit var binding : ActivityNotifcationsBinding

    lateinit var notificationViewModel: NotificationViewModel

    @Inject
    lateinit var notificationVMF: NotificationVMF

    lateinit var notificationList : ArrayList<NotificationModel>

    lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_notifcations)
        supportActionBar?.hide()

        notificationViewModel = ViewModelProvider(this,notificationVMF).get(NotificationViewModel::class.java)

        binding.imgBack.setOnClickListener {
            finish()
        }

        notificationList = ArrayList()
        notificationAdapter = NotificationAdapter(this,notificationList)

        binding.notificationRecv.layoutManager = LinearLayoutManager(this)
        binding.notificationRecv.adapter = notificationAdapter

        notificationViewModel.getNotifications()

        notificationViewModel.notification_liveData.observe(this,{
            when(it){
                is Response.Sucess ->{
                    notificationList.clear()
                    notificationList.addAll(it.data!!)
                    notificationList.reverse()
                    notificationAdapter.notifyDataSetChanged()
                    binding.loadingAnim7.visibility = View.GONE
                    it.data.clear()

                    if(notificationList.isEmpty()){
                        binding.noFoundText3.visibility = View.VISIBLE
                    }else{
                        binding.noFoundText3.visibility = View.GONE
                    }
                }
                is Response.Error ->{

                }
                is Response.Loading ->{

                }
                else -> {}
            }
        })
    }
}