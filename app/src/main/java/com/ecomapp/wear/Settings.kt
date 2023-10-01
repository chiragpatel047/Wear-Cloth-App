package com.ecomapp.wear

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ecomapp.wear.Factories.SettingVMF
import com.ecomapp.wear.Models.UserModel
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Viewmodels.SettingViewModel
import com.ecomapp.wear.databinding.ActivitySettingsBinding
import com.ecomapp.wear.databinding.LoadingDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class Settings : AppCompatActivity() {

    lateinit var binding: ActivitySettingsBinding

    lateinit var settingViewModel: SettingViewModel

    @Inject
    lateinit var settingVMF: SettingVMF

     var uid : String? = null
     var username : String? = null
     var email : String? = null
     var password: String? = null
     var userImage : String? = null
     var phoneNO : String? = null
     var uploadPhoto : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_settings)
        supportActionBar?.hide()

        binding.imgBack.setOnClickListener {
            finish()
        }

        settingViewModel = ViewModelProvider(this,settingVMF).get(SettingViewModel::class.java)

        settingViewModel.getUserInfo()

        val dialogBinding : LoadingDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.loading_dialog,null,false)

        val loadingDialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root).create()

        loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        loadingDialog.show()

        val sharedPreferences = getSharedPreferences("switch", MODE_PRIVATE)
        binding.switchSale.isChecked = sharedPreferences.getBoolean("switch_sale",true)
        binding.switchNew.isChecked = sharedPreferences.getBoolean("switch_new",true)
        binding.switchStatus.isChecked = sharedPreferences.getBoolean("switch_status",true)

        settingViewModel.setting_liveData.observe(this,{
            when(it){
                is Response.Sucess -> {

                    loadingDialog.dismiss()

                    uid = it.data!!.uid!!
                    username = it.data!!.username!!
                    email = it.data!!.email!!
                    password = it.data!!.password!!
                    userImage = it.data!!.userImage!!
                    phoneNO = it.data!!.phoneNO!!

                    binding.fullName.editText!!.setText(it.data!!.username)
                    binding.phoneNo.editText!!.setText(it.data!!.phoneNO)
                    Glide.with(this).load(it.data!!.userImage).placeholder(R.drawable.bigprofileactivated).into(binding.userImage)
                }

                is Response.Error -> {

                }
                is Response.Loading -> {

                }
            }
        })

        settingViewModel.user_liveData.observe(this,{
            when(it){
                is Response.Sucess -> {
                    finish()
                    Toast.makeText(this,"Updated successfully",Toast.LENGTH_LONG).show()
                }

                is Response.Error -> {

                }
                is Response.Loading -> {

                }
            }
        })

        binding.txtSingUp.setOnClickListener {

            val switch_new : Boolean = binding.switchNew.isChecked
            val switch_sale : Boolean = binding.switchSale.isChecked
            val switch_status : Boolean = binding.switchStatus.isChecked

            val sharedPreferences = getSharedPreferences("switch", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("switch_sale",switch_sale)
            editor.putBoolean("switch_new",switch_new)
            editor.putBoolean("switch_status",switch_status)
            editor.apply()

            loadingDialog.show()

            username =  binding.fullName.editText?.text.toString()
            phoneNO =  binding.phoneNo.editText?.text.toString()

            settingViewModel.updateUserInfo(UserModel(uid, username, email, password, userImage, phoneNO),uploadPhoto)

        }

        binding.profileView.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                1
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==1 && resultCode== RESULT_OK && data!=null){
            userImage = data.data.toString()
            uploadPhoto = true
            Glide.with(this).load(data.data.toString()).into(binding.userImage)
        }
    }
}