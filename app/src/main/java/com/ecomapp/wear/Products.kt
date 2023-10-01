package com.ecomapp.wear

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecomapp.wear.Adapters.ProductGridAdapter
import com.ecomapp.wear.Adapters.ProductSimpleAdapter
import com.ecomapp.wear.Factories.ProductVMF
import com.ecomapp.wear.Models.ProuctModel
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Viewmodels.ProductsViewModel
import com.ecomapp.wear.databinding.ActivityProductsBinding
import com.ecomapp.wear.databinding.SortBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class Products : AppCompatActivity() {

    lateinit var binding: ActivityProductsBinding

    lateinit var sort_bottomSheetBinding : SortBottomsheetBinding
    lateinit var sortBottomSheet : BottomSheetDialog

    lateinit var prouctList : ArrayList<ProuctModel>
    lateinit var productGridAdapter: ProductGridAdapter

    lateinit var productSimpleAdapter: ProductSimpleAdapter

    var isGridView : Boolean = true

    lateinit var productsViewModel: ProductsViewModel

    @Inject
    lateinit var productVMF: ProductVMF

    var search : String =""

    companion object{
        var favIdList : ArrayList<String> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_products)
        supportActionBar?.hide()

        binding.imgBack.setOnClickListener {
            finish()
        }

        productsViewModel = ViewModelProvider(this,productVMF).get(ProductsViewModel::class.java)
        prouctList = ArrayList()

        productGridAdapter = ProductGridAdapter(this,prouctList,::removeFromFav,::addToFav)
        productSimpleAdapter = ProductSimpleAdapter(this,prouctList,::removeFromFav,::addToFav)

        binding.productsRecylcerview.layoutManager = GridLayoutManager(this,2)
        binding.productsRecylcerview.adapter = productGridAdapter

        binding.switchProductImage.setOnClickListener {
            if(isGridView){
                binding.productsRecylcerview.layoutManager = LinearLayoutManager(this)
                binding.productsRecylcerview.adapter = productSimpleAdapter
                binding.switchProductImage.setImageResource(R.drawable.grid)
                isGridView = false
            }else{
                binding.productsRecylcerview.layoutManager = GridLayoutManager(this,2)
                binding.productsRecylcerview.adapter = productGridAdapter
                binding.switchProductImage.setImageResource(R.drawable.list)
                isGridView = true
            }
        }

        binding.searchImg.setOnClickListener {
            search = binding.searchText.editText?.text.toString()
            binding.loadingAnim.visibility = View.VISIBLE
            binding.noFoundText.visibility = View.GONE
            prouctList.clear()
            productGridAdapter.notifyDataSetChanged()
            productSimpleAdapter.notifyDataSetChanged()
            productsViewModel.LoadAllProducts()
        }

        var loadUsing : String? = null
        loadUsing = intent.getStringExtra("loadUsing")

        var loadNewOrRandom : String? = null
        loadNewOrRandom = intent.getStringExtra("loadNewOrRandom")

        productsViewModel.loadFavProducts()

        productsViewModel.favProducts_liveData.observe(this,{
            when(it){
                is Response.Sucess ->{
                    favIdList.clear()
                    for(single in it.data!!){
                        favIdList.add(single.productId!!)
                    }
                    it.data.clear()

                    if(loadUsing!=null){
                        productsViewModel.LoadBannersProducts(loadUsing)
                    }else if(loadNewOrRandom!=null){
                        if(loadNewOrRandom.equals("new")){
                            productsViewModel.LoadNewProducts()
                        }else{
                            productsViewModel.LoadRandomProducts()
                        }
                    }
                    else{
                        val parentCat : String = intent.getStringExtra("parentCat")!!
                        val mainCat : String = intent.getStringExtra("mainCat")!!
                        val subCat : String = intent.getStringExtra("subCat")!!

                        productsViewModel.LoadProducts(parentCat,mainCat,subCat)
                    }
                }
                is Response.Error ->{

                }
                is Response.Loading ->{

                }
            }
        })

        productsViewModel.banner_products_liveData.observe(this,{
            when(it){
                is Response.Sucess ->{

                    prouctList.clear()
                    prouctList.addAll(it.data!!)
                    it.data.clear()

                    prouctList.shuffle()

                    productGridAdapter.notifyDataSetChanged()
                    productSimpleAdapter.notifyDataSetChanged()
                    binding.loadingAnim.visibility = View.GONE

                    if(prouctList.isEmpty()){

                        binding.noFoundText.visibility = View.VISIBLE

                    }else{

                        binding.noFoundText.visibility = View.GONE
                    }
                }
                is Response.Error ->{

                }
                is Response.Loading ->{

                }
            }
        })

        productsViewModel.products_liveData.observe(this,{
            when(it){
                is Response.Sucess ->{

                    prouctList.clear()
                    prouctList.addAll(it.data!!)
                    it.data.clear()

                    prouctList.shuffle()

                    productGridAdapter.notifyDataSetChanged()
                    productSimpleAdapter.notifyDataSetChanged()
                    binding.loadingAnim.visibility = View.INVISIBLE

                    if(prouctList.isEmpty()){

                        binding.noFoundText.visibility = View.VISIBLE

                    }else{

                        binding.noFoundText.visibility = View.GONE
                    }
                }

                is Response.Error ->{

                }
                is Response.Loading ->{

                }
            }
        })

        productsViewModel.newProducts_liveData.observe(this,{
            when(it){
                is Response.Sucess ->{

                    prouctList.clear()
                    for(singleItem in it.data!!){
                        prouctList.add(0,singleItem)
                    }
                    it.data.clear()

                    productGridAdapter.notifyDataSetChanged()
                    productSimpleAdapter.notifyDataSetChanged()
                    binding.loadingAnim.visibility = View.INVISIBLE

                    if(prouctList.isEmpty()){

                        binding.noFoundText.visibility = View.VISIBLE

                    }else{

                        binding.noFoundText.visibility = View.GONE
                    }
                }
                is Response.Error ->{

                }
                is Response.Loading ->{

                }
            }
        })

        productsViewModel.random_liveData.observe(this,{
            when(it){
                is Response.Sucess ->{

                    prouctList.clear()
                    for(singleItem in it.data!!){
                        prouctList.add(0,singleItem)
                    }
                    it.data.clear()

                    prouctList.shuffle()
                    productGridAdapter.notifyDataSetChanged()
                    productSimpleAdapter.notifyDataSetChanged()
                    binding.loadingAnim.visibility = View.INVISIBLE

                    if(prouctList.isEmpty()){

                        binding.noFoundText.visibility = View.VISIBLE

                    }else{

                        binding.noFoundText.visibility = View.GONE
                    }
                }
                is Response.Error ->{

                }
                is Response.Loading ->{

                }
            }
        })

        productsViewModel.search_liveData.observe(this,{
            when(it){
                is Response.Sucess ->{

                    prouctList.clear()
                    for(singleItem in it.data!!){
                        if(singleItem.productTitle?.contains(search)!!
                            || singleItem.productSubTitle?.contains(search)!!
                            || singleItem.productDesc?.contains(search)!!){
                            prouctList.add(0,singleItem)
                        }
                    }

                    it.data.clear()

                    if(prouctList.isEmpty()){

                        binding.noFoundText.visibility = View.VISIBLE

                    }else{

                        binding.noFoundText.visibility = View.GONE
                    }
                    productGridAdapter.notifyDataSetChanged()
                    productSimpleAdapter.notifyDataSetChanged()
                    binding.loadingAnim.visibility = View.INVISIBLE
                }
                is Response.Error ->{

                }
                is Response.Loading ->{

                }
            }
        })

    }
    fun removeFromFav(productId : String){
        productsViewModel.removeFromFav(productId)
    }

    fun addToFav(productId : String){
        productsViewModel.addToFavourites(productId)
    }
}