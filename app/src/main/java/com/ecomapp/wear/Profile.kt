package com.ecomapp.wear

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ecomapp.wear.Factories.HomeScreenVMF
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Viewmodels.HomeScreenViewModel
import com.ecomapp.wear.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Profile : Fragment() {

    lateinit var homeScreenViewModel: HomeScreenViewModel

    @Inject
    lateinit var homeScreenVMF: HomeScreenVMF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentProfileBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false)

        homeScreenViewModel = ViewModelProvider(requireActivity(),homeScreenVMF).get(HomeScreenViewModel::class.java)

        homeScreenViewModel.getUserInfo()

        homeScreenViewModel.user_info_liveData.observe(viewLifecycleOwner,{
            when(it){
                is Response.Sucess -> {
                    binding.userName.text = it.data?.username
                    binding.userEmail.text = it.data?.email
                    Glide.with(this).load(it.data!!.userImage).placeholder(R.drawable.bigprofileactivated).into(binding.userImage)

                }
                is Response.Error -> {

                }
                is Response.Loading -> {

                }
            }
        })

        binding.myOreders.setOnClickListener {
            val intent = Intent(activity,MyOrders::class.java)
            startActivity(intent)
        }

        binding.notification.setOnClickListener{
            val intent = Intent(activity,Notifcations::class.java)
            startActivity(intent)
        }

        binding.shippingAddress.setOnClickListener {
            val intent = Intent(activity,ShippingAddress::class.java)
            startActivity(intent)
        }

        binding.myreviews.setOnClickListener {
            val intent = Intent(activity,MyReviewItems::class.java)
            startActivity(intent)
        }

        binding.settings.setOnClickListener {
            val intent = Intent(activity,Settings::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}