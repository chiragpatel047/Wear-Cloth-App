package com.ecomapp.wear

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecomapp.wear.Adapters.DeliveryItemAdapter
import com.ecomapp.wear.Factories.MyOrderVMF
import com.ecomapp.wear.Models.OrderModel
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Viewmodels.MyOrderViewModel
import com.ecomapp.wear.databinding.ActivityMyOrdersBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class MyOrders : AppCompatActivity() {

    lateinit var binding: ActivityMyOrdersBinding
    lateinit var deliveryItemList: ArrayList<OrderModel>
    lateinit var deliveryItemAdapter: DeliveryItemAdapter

    lateinit var myOrderViewModel: MyOrderViewModel

    @Inject
    lateinit var myOrderVMF: MyOrderVMF

    var selectedTab: String = "pendingOrders"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_orders)
        supportActionBar?.hide()

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.deliveryItemsRecv.layoutManager = LinearLayoutManager(this)

        myOrderViewModel = ViewModelProvider(this, myOrderVMF).get(MyOrderViewModel::class.java)

        val tabPos = intent.getIntExtra("tabPost", 0)

        if (tabPos.equals(0)) {
            selectedTab = "pendingOrders"
        } else if (tabPos.equals(1)) {
            selectedTab = "deliveredOrders"
        } else if (tabPos.equals(2)) {
            selectedTab = "cancelledOrders"
        } else {
            selectedTab = "pendingOrders"
        }

        myOrderViewModel.LoadOrders(selectedTab)

        deliveryItemList = ArrayList()
        deliveryItemAdapter = DeliveryItemAdapter(this, deliveryItemList, selectedTab)

        binding.deliveryItemsRecv.adapter = deliveryItemAdapter

        binding.topTabBar.selectTab(binding.topTabBar.getTabAt(tabPos))

        myOrderViewModel.order_liveData.observe(this, {
            when (it) {
                is Response.Sucess -> {
                    deliveryItemList.clear()
                    for (single in it.data!!) {
                        deliveryItemList.add(0, single)
                    }
                    binding.loadingAnim2.visibility = View.GONE
                    if (deliveryItemList.isEmpty()) {
                        binding.noFoundText2.visibility = View.VISIBLE
                    } else {
                        binding.noFoundText2.visibility = View.GONE
                    }
                    it.data.clear()
                    deliveryItemAdapter.notifyDataSetChanged()
                }

                is Response.Error -> {

                }

                is Response.Loading -> {

                }
            }
        })

        binding.topTabBar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                deliveryItemList.clear()
                binding.loadingAnim2.visibility = View.VISIBLE
                binding.noFoundText2.visibility = View.GONE

                when (tab?.position) {
                    0 -> {
                        selectedTab = "pendingOrders"
                        deliveryItemAdapter =
                            DeliveryItemAdapter(this@MyOrders, deliveryItemList, selectedTab)
                        binding.deliveryItemsRecv.adapter = deliveryItemAdapter
                        myOrderViewModel.LoadOrders(selectedTab)
                    }

                    1 -> {
                        selectedTab = "deliveredOrders"
                        deliveryItemAdapter =
                            DeliveryItemAdapter(this@MyOrders, deliveryItemList, selectedTab)
                        binding.deliveryItemsRecv.adapter = deliveryItemAdapter
                        myOrderViewModel.LoadOrders(selectedTab)
                    }

                    2 -> {
                        selectedTab = "cancelledOrders"
                        deliveryItemAdapter =
                            DeliveryItemAdapter(this@MyOrders, deliveryItemList, selectedTab)
                        binding.deliveryItemsRecv.adapter = deliveryItemAdapter
                        myOrderViewModel.LoadOrders(selectedTab)
                    }
                }
                deliveryItemAdapter.notifyDataSetChanged()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }
}