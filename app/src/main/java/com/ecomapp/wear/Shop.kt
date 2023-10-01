package com.ecomapp.wear

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecomapp.wear.Adapters.MainCatAdapter
import com.ecomapp.wear.Factories.ShopVMF
import com.ecomapp.wear.Models.MainCatModel
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Viewmodels.ShopViewModel
import com.ecomapp.wear.databinding.FragmentShopBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class Shop : Fragment() {

    lateinit var binding : FragmentShopBinding

    lateinit var mainCatList : ArrayList<MainCatModel>
    lateinit var mainCatAdapter: MainCatAdapter

    @Inject
    lateinit var shopVMF: ShopVMF

    lateinit var shopViewModel: ShopViewModel

    var selectedCat : String = "Mens"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_shop, container, false)

        shopViewModel = ViewModelProvider(requireActivity(),shopVMF).get(ShopViewModel::class.java)

        mainCatList = ArrayList()
        mainCatAdapter = MainCatAdapter(requireActivity(),mainCatList,::getMainCatname)

        binding.catItemsRecyclerview.layoutManager = LinearLayoutManager(activity)
        binding.catItemsRecyclerview.adapter = mainCatAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedCat  = "Mens"

        shopViewModel.LoadMainCategories(selectedCat)

//        binding.topTabBar.addTab(binding.topTabBar.newTab().setText("Beauty"))
//        binding.topTabBar.addTab(binding.topTabBar.newTab().setText("Home & leaving"))
//        binding.topTabBar.addTab(binding.topTabBar.newTab().setText("Mobile & Electronics"))
//        binding.topTabBar.addTab(binding.topTabBar.newTab().setText("Grocery"))
//        binding.topTabBar.addTab(binding.topTabBar.newTab().setText("Everything else"))

        binding.topTabBar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                mainCatList.clear()
                when(tab?.position){
                    0 ->{
                        selectedCat  = "Mens"
                        binding.loadingAnim.visibility = View.VISIBLE
                        shopViewModel.LoadMainCategories(selectedCat)
                    }
                    1 ->{
                        selectedCat  = "Womens"
                        binding.loadingAnim.visibility = View.VISIBLE
                        shopViewModel.LoadMainCategories(selectedCat)
                    }
                    2 ->{
                        selectedCat  = "Kids"
                        binding.loadingAnim.visibility = View.VISIBLE
                        shopViewModel.LoadMainCategories(selectedCat)
                    }
                }
                mainCatAdapter.notifyDataSetChanged()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })


        shopViewModel.mainCat_liveData.observe(viewLifecycleOwner,{
            when (it){
                is Response.Sucess->{
                    mainCatList.clear()
                    mainCatList.addAll(it.data!!)
                    it.data.clear()
                    mainCatAdapter.notifyDataSetChanged()
                    binding.loadingAnim.visibility = View.GONE
                }

                is Response.Error -> {
                    binding.loadingAnim.visibility = View.GONE
                }

                is Response.Loading -> {

                }

                else -> {}
            }
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        shopViewModel.clearLiveData()
    }

    fun getMainCatname() : String{
        return selectedCat
    }

}