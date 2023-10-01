package com.ecomapp.wear

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecomapp.wear.Adapters.ImageSliderAdapter
import com.ecomapp.wear.Adapters.ItemAdapters
import com.ecomapp.wear.Factories.FullProductVMF
import com.ecomapp.wear.Models.BagModel
import com.ecomapp.wear.Models.ProductImageModel
import com.ecomapp.wear.Models.ProuctModel
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Viewmodels.FullProductViewModel
import com.ecomapp.wear.databinding.ActivityFullProductBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class FullProduct : AppCompatActivity() {

    lateinit var binding : ActivityFullProductBinding
    lateinit var imageList : ArrayList<ProductImageModel>
    lateinit var imageSliderAdapter: ImageSliderAdapter

    lateinit var sizeList : ArrayList<String>
    lateinit var size_spinnerAdapter: ArrayAdapter<String>

    lateinit var productList : ArrayList<ProuctModel>
    lateinit var itemAdapters: ItemAdapters

    lateinit var fullProductViewModel: FullProductViewModel

    @Inject
    lateinit var fullProductVMF: FullProductVMF
    var productPrice : String = ""
    var productTitle : String = ""
    var productDesc : String = ""
    var fullProductId : String? = ""
    var productMainImage : String? = ""
    var productSelectedSize : String? = ""

    var NOOFRating : Float? = 0F
    var itemRating : Float? = 0F

    companion object{
        var buyingItem : ArrayList<BagModel> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_full_product)
        supportActionBar?.hide()

        binding.buynow.isEnabled = false
        binding.buynow.setBackgroundResource(R.drawable.gray_button)

        binding.imgBack.setOnClickListener {
            finish()
        }

        imageList = ArrayList()
        imageSliderAdapter = ImageSliderAdapter(this,imageList)

        binding.imageSlider.setSliderAdapter(imageSliderAdapter)

        val productId = intent.getStringExtra("productId")!!

        fullProductViewModel = ViewModelProvider(this,fullProductVMF).get(FullProductViewModel::class.java)

        sizeList = ArrayList()

        size_spinnerAdapter = ArrayAdapter(this, R.layout.spinner_view,sizeList)

        binding.spinnerSize.adapter = size_spinnerAdapter

        fullProductViewModel.LoadFullProduct(productId)
        fullProductViewModel.LoadProductDetails(productId)
        fullProductViewModel.LoadProductSizes(productId)
        fullProductViewModel.loadCartProducts()
        fullProductViewModel.loadFavProducts()

        binding.goToDetails.setOnClickListener {
            val intent = Intent(this,ProductDetails::class.java)
            intent.putExtra("Desc",productDesc)
            startActivity(intent)
        }

        fullProductViewModel.product_liveData.observe(this,{
            when(it){
                is Response.Sucess ->{
                    imageList.addAll(it.data!!)
                    imageSliderAdapter.notifyDataSetChanged()
                }
                is Response.Error -> {
                    Toast.makeText(this,it.errorMsg,Toast.LENGTH_LONG).show()
                }
                is Response.Loading -> {

                }
            }
        })

        fullProductViewModel.productDetails_liveData.observe(this,{
            when(it){
                is Response.Sucess ->{

                    productPrice = it.data?.productPrice!!
                    productTitle = it.data?.productTitle!!
                    fullProductId = it.data?.productId!!
                    NOOFRating = it.data?.noOfRating!!
                    itemRating = it.data?.rate!!
                    productDesc = it.data?.productDesc!!
                    productMainImage = it.data?.productMainImage!!
                    binding.itemText.text = it.data?.productTitle
                    binding.itemName.text = it.data?.productSubTitle
                    binding.itemPrice.text = it.data?.productPrice+"₹"
                    binding.itemRating.rating = it.data?.rate!!
                    binding.itemNumberOfRating.text = "("+it.data?.noOfRating.toString().replace(".0","").toString()+")"

                    binding.buynow.isEnabled=true
                    binding.buynow.setBackgroundResource(R.drawable.red_button)

                }
                is Response.Error -> {

                }
                is Response.Loading -> {

                }
            }
        })

        fullProductViewModel.size_liveData.observe(this,{
            when(it){
                is Response.Sucess ->{
                    sizeList.clear()
                    sizeList.add("Select size")
                    for(singleSize in it.data!!){
                        sizeList.add(singleSize.sizeName!!)
                    }
                    it.data.clear()
                    size_spinnerAdapter.notifyDataSetChanged()
                }
                is Response.Error -> {

                }
                is Response.Loading -> {

                }
            }
        })

        val cartTempList : ArrayList<String> = ArrayList()

        fullProductViewModel.cart_liveData.observe(this,{
            when(it){
                is Response.Sucess ->{

                    for(single in it.data!!){
                        cartTempList.add(single.productId!!)
                    }

                    if(cartTempList.contains(productId)){
                        binding.addtocart.setBackgroundResource(R.drawable.red_button)
                    }else{
                        binding.addtocart.setBackgroundResource(R.drawable.discard_back)
                    }
                }
                is Response.Error -> {

                }
                is Response.Loading -> {

                }
            }
        })

        binding.buynow.setOnClickListener {

            productSelectedSize = binding.spinnerSize.selectedItem.toString()

            if(productSelectedSize.equals("Select size")){
                Toast.makeText(this,"Please select size",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val singleItem = BagModel(fullProductId,productTitle,productPrice,productMainImage,productSelectedSize)
            buyingItem.clear()
            buyingItem.add(singleItem)

            val intent = Intent(this,Address::class.java)
            intent.putExtra("from", "single")
            startActivity(intent)
        }

        binding.addtocart.setOnClickListener {
            if(cartTempList.contains(productId)){
                binding.addtocart.setBackgroundResource(R.drawable.discard_back)
                fullProductViewModel.removeFromCart(productId)
                cartTempList.remove(productId)

            }else{
                val selectedSize = binding.spinnerSize.selectedItem.toString()

                if(selectedSize.equals("Select size")){
                    Toast.makeText(this,"Please select size",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                binding.addtocart.setBackgroundResource(R.drawable.red_button)

                val bagModel = BagModel(productId,binding.itemText.text.toString(),productPrice,imageList.get(0).imageUrl,selectedSize)
                fullProductViewModel.addToCart(bagModel)
                cartTempList.add(productId)
            }
        }

        fullProductViewModel.add_to_cart_liveData.observe(this,{
            when(it){
                is Response.Sucess ->{

                }
                is Response.Error ->{

                }
                is Response.Loading ->{

                }
            }
        })

        val likeTempList : ArrayList<String> = ArrayList()

        fullProductViewModel.favProducts_liveData.observe(this,{
            when(it){
                is Response.Sucess ->{
                    for(single in it.data!!){
                        likeTempList.add(single.productId!!)
                    }
                    if(likeTempList.contains(productId)){
                        binding.itemLike.setImageResource(R.drawable.likeactivated)
                        binding.itemLike.setColorFilter(ContextCompat.getColor(this,R.color.likecolor))

                    }else{
                        binding.itemLike.setImageResource(R.drawable.likeinactive)
                        binding.itemLike.setColorFilter(ContextCompat.getColor(this,R.color.unlikecolor))

                    }
                }
                is Response.Error -> {

                }

                is Response.Loading -> {

                }
            }
        })

        binding.itemLike.setOnClickListener {

            if(likeTempList.contains(productId)){
                binding.itemLike.setImageResource(R.drawable.likeinactive)
                binding.itemLike.setColorFilter(ContextCompat.getColor(this,R.color.unlikecolor))
                fullProductViewModel.removeFromFav(productId)
                likeTempList.remove(productId)

            }else{
                binding.itemLike.setImageResource(R.drawable.likeactivated)
                binding.itemLike.setColorFilter(ContextCompat.getColor(this,R.color.likecolor))
                fullProductViewModel.addToFavourites(productId)
                likeTempList.add(productId)

            }
        }

        binding.suggestedRecyclerview.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)

        productList = ArrayList<ProuctModel>()
        itemAdapters = ItemAdapters(this, productList, ::removeFromFav, ::addToFav)

        binding.suggestedRecyclerview.adapter=itemAdapters
        fullProductViewModel.LoadNewProducts()

        fullProductViewModel.newProducts_liveData.observe(this,{

            when(it){
                is Response.Sucess ->{

                    val tempList : ArrayList<ProuctModel> = ArrayList()

                    productList.clear()
                    tempList.addAll(it.data!!)

                    tempList.shuffle()
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

        itemAdapters.notifyDataSetChanged()

        binding.goToRatings.setOnClickListener {
            val intent = Intent(this,Ratings::class.java)
            intent.putExtra("productId",productId)
            intent.putExtra("noofRating",NOOFRating)
            intent.putExtra("itemrate",itemRating)
            startActivity(intent)
        }
    }

    fun removeFromFav(productId : String){
        fullProductViewModel.removeFromFav(productId)
    }

    fun addToFav(productId : String){
        fullProductViewModel.addToFavourites(productId)
    }

}