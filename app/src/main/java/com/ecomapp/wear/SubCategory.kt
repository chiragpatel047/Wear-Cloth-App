package com.ecomapp.wear

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecomapp.wear.Adapters.SubCatAdapter
import com.ecomapp.wear.Factories.SubCatVMF
import com.ecomapp.wear.Models.SubCatModel
import com.ecomapp.wear.Viewmodels.SubCatViewModel
import com.ecomapp.wear.databinding.ActivitySubCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.ecomapp.wear.Repositories.Response


@AndroidEntryPoint()
class SubCategory : AppCompatActivity() {

    lateinit var binding : ActivitySubCategoryBinding

    lateinit var subCatList : ArrayList<SubCatModel>
    lateinit var subCatAdapter: SubCatAdapter

    lateinit var subCatViewModel: SubCatViewModel

    @Inject
    lateinit var subCatVMF: SubCatVMF

    lateinit var parentCatName : String
    lateinit var mainCatName : String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_sub_category)

        supportActionBar?.hide()

        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.subCatRecyclerview.layoutManager = LinearLayoutManager(this)

        subCatViewModel = ViewModelProvider(this,subCatVMF).get(SubCatViewModel::class.java)

        subCatList = ArrayList()
        subCatAdapter = SubCatAdapter(this,subCatList,::getParentCat,::getMainCat)

        binding.subCatRecyclerview.adapter = subCatAdapter

        parentCatName = intent.getStringExtra("parentCat")!!
        mainCatName = intent.getStringExtra("mainCat")!!

        subCatViewModel.LoadSubCatigories(parentCatName!!, mainCatName!!)

        subCatViewModel.subCat_liveData.observe(this,{

            when(it){
                is Response.Sucess ->{
                    subCatList.clear()
                    subCatList.addAll(it.data!!)
                    it.data.clear()
                    subCatAdapter.notifyDataSetChanged()
                    binding.loadingAnim.visibility = View.GONE

                }

                is Response.Error -> {
                    binding.loadingAnim.visibility = View.GONE
                    Toast.makeText(this,it.errorMsg.toString(), Toast.LENGTH_LONG).show()

                }
                is Response.Loading -> {

                }
            }

        })

        binding.txtSingUp.setOnClickListener {
            val intent = Intent(this,Products::class.java)
            intent.putExtra("loadNewOrRandom","new")
            startActivity(intent)
        }
    }
    fun getParentCat() : String{
        return parentCatName
    }

    fun getMainCat() : String{
        return mainCatName
    }
}