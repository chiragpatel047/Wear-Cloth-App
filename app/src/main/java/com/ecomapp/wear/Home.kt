package com.ecomapp.wear

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ecomapp.wear.Adapters.ItemAdapters
import com.ecomapp.wear.Factories.HomeScreenVMF
import com.ecomapp.wear.Models.ProuctModel
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Viewmodels.HomeScreenViewModel
import com.ecomapp.wear.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint()
class Home : Fragment() {

    lateinit var productList : ArrayList<ProuctModel>

    lateinit var random_productList : ArrayList<ProuctModel>

    @Inject
    lateinit var homeScreenVMF: HomeScreenVMF

    lateinit var homeScreenViewModel: HomeScreenViewModel
    companion object{
        var favIdList : ArrayList<String> = ArrayList()
        lateinit var random_itemAdapters: ItemAdapters
        lateinit var itemAdapters: ItemAdapters
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding : FragmentHomeBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_home, container, false)

        binding.newItemRecyclerview.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        binding.saleItemRecyclerview.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

        homeScreenViewModel = ViewModelProvider(requireActivity(),homeScreenVMF).get(HomeScreenViewModel::class.java)
        homeScreenViewModel.loadFavProducts()

        homeScreenViewModel.favProducts_liveData.observe(viewLifecycleOwner,{
            when(it){
                is Response.Sucess ->{
                    favIdList.clear()
                    for(single in it.data!!){
                        favIdList.add(single.productId!!)
                    }

                    it.data.clear()

                    homeScreenViewModel.LoadNewProducts()
                    homeScreenViewModel.LoadRandomProducts()
                }
                is Response.Error ->{

                }
                is Response.Loading ->{

                }
            }
        })

        homeScreenViewModel.bannerData_liveData.observe(viewLifecycleOwner,{

            when(it){
                is Response.Sucess ->{

                    Glide.with(requireActivity()).load(it.data?.get(0)?.mainImage).listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.topBannerImage.visibility = View.VISIBLE
                            binding.topBannerTitle.text = it.data?.get(0)?.mainTitle.toString()
                            return false
                        }
                    }).into(binding.topBannerImage)


                    Glide.with(requireActivity()).load(it.data?.get(1)?.mainImage).listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.middleBannerImage.visibility = View.VISIBLE
                            binding.middleBannerText.text = it.data?.get(1)?.mainTitle.toString()
                            return false
                        }
                    }).into(binding.middleBannerImage)


                    Glide.with(requireActivity()).load(it.data?.get(2)?.mainImage).listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.BottomBannerFirstImage.visibility = View.VISIBLE
                            binding.BottomBannerFirstTitle.text = it.data?.get(2)?.mainTitle.toString()
                            return false
                        }
                    }).into(binding.BottomBannerFirstImage)


                    Glide.with(requireActivity()).load(it.data?.get(3)?.mainImage).listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.BottomBannerSecondTitle.text = it.data?.get(3)?.mainTitle.toString()
                            binding.BottomBannerSecondImage.visibility = View.VISIBLE
                            return false
                        }
                    }).into(binding.BottomBannerSecondImage)


                    Glide.with(requireActivity()).load(it.data?.get(4)?.mainImage).listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.BottomBannerThirdImage.visibility = View.VISIBLE
                            binding.BottomBannerThirdTitle.text = it.data?.get(4)?.mainTitle.toString()
                            binding.BottomBannerOnlyText.text = it.data?.get(5)?.mainTitle.toString()
                            return false
                        }
                    }).into(binding.BottomBannerThirdImage)


                }
                is Response.Error -> {
                    Toast.makeText(activity,it.errorMsg,Toast.LENGTH_LONG).show()
                }
                is Response.Loading -> {

                }
            }
        })

        binding.topBannerImage.setOnClickListener {
            goToProducts("1")
        }
        binding.topBannerBtnCheck.setOnClickListener {
            goToProducts("1")
        }
        binding.middleBannerImage.setOnClickListener {
            goToProducts("2")
        }

        binding.BottomBannerFirstImage.setOnClickListener {
            goToProducts("3")
        }
        binding.BottomBannerSecondImage.setOnClickListener {
            goToProducts("4")
        }

        binding.BottomBannerThirdImage.setOnClickListener {
            goToProducts("5")
        }

        binding.BottomBannerOnlyText.setOnClickListener {
            goToProducts("6")
        }

        binding.newViewAll.setOnClickListener {
            val intent = Intent(activity,Products::class.java)
            intent.putExtra("loadNewOrRandom","new")
            startActivity(intent)
        }

        binding.randomViewAll.setOnClickListener {
            val intent = Intent(activity,Products::class.java)
            intent.putExtra("loadNewOrRandom","random")
            startActivity(intent)
        }

        productList = ArrayList()
        itemAdapters = ItemAdapters(requireActivity(), productList,::removeFromFav,::addToFav)

        random_productList = ArrayList()
        random_itemAdapters = ItemAdapters(
            requireActivity(),
            random_productList,
            ::removeFromFav,
            ::addToFav
        )

        binding.newItemRecyclerview.adapter=itemAdapters
        binding.saleItemRecyclerview.adapter=random_itemAdapters

        homeScreenViewModel.newProducts_liveData.observe(viewLifecycleOwner,{
            when(it){
                is Response.Sucess ->{
                    productList.clear()

                    val tempList : ArrayList<ProuctModel> = ArrayList()

                    for(singleItem in it.data!!){
                        tempList.add(0,singleItem)
                    }
                    var i = 0
                    for(singleProduct in tempList){
                        productList.add(singleProduct)
                        if(i==5){
                            break
                        }
                        i++
                    }
                    it.data.clear()
                    itemAdapters.notifyDataSetChanged()
                }
                is Response.Error ->{

                }
                is Response.Loading ->{

                }
            }
        })

        homeScreenViewModel.random_liveData.observe(viewLifecycleOwner,{
            when(it){
                is Response.Sucess ->{
                    random_productList.clear()

                    val tempList : ArrayList<ProuctModel> = ArrayList()

                    for(singleItem in it.data!!){
                        tempList.add(0,singleItem)
                    }
                    tempList.shuffle()
                    var i = 0
                    for(singleProduct in tempList){
                        random_productList.add(singleProduct)
                        if(i==5){
                            break
                        }
                        i++
                    }


                    it.data.clear()
                    random_itemAdapters.notifyDataSetChanged()
                    binding.loadingAnim.visibility = View.GONE
                    binding.entireView.visibility = View.VISIBLE

                }
                is Response.Error ->{

                }
                is Response.Loading ->{

                }
            }
        })

        return binding.root
    }
    fun goToProducts(loadUsing : String){
        val intent = Intent(activity,Products::class.java)
        intent.putExtra("loadUsing",loadUsing)
        startActivity(intent)
    }

    fun favIdArrayList() : ArrayList<String>{
        return favIdList
    }

    fun removeFromFav(productId : String){
        homeScreenViewModel.removeFromFav(productId)
    }

    fun addToFav(productId : String){
        homeScreenViewModel.addToFavourites(productId)
    }
}