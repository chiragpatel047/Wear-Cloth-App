package com.ecomapp.wear

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ecomapp.wear.Factories.MainActivityVMF
import com.ecomapp.wear.Models.UserModel
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Viewmodels.MainActivityViewModel
import com.ecomapp.wear.databinding.ActivityMainBinding
import com.ecomapp.wear.databinding.TryagainDialogBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class MainActivity : AppCompatActivity() {

    lateinit var mainActivityViewModel: MainActivityViewModel

    @Inject
    lateinit var mainActivityVMF: MainActivityVMF

    lateinit var binding : ActivityMainBinding

    @Inject
    lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        supportActionBar?.hide()

        mainActivityViewModel = ViewModelProvider(this,mainActivityVMF).get(MainActivityViewModel::class.java)

        FirebaseApp.initializeApp(this)

        isUserAlreadyLogin()

        var loadingView = LayoutInflater.from(this).inflate(R.layout.loading_dialog,null)
        var loadingDialog = AlertDialog.Builder(this).setView(loadingView).create()
        loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        loadingDialog.setCancelable(false)

        val dialogBinding : TryagainDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.tryagain_dialog,null,false)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root).create()

        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding.btnYes.setOnClickListener {
            alertDialog.dismiss()
        }

        mainActivityViewModel.user_liveData.observe(this, Observer {
            when(it){
                is Response.Sucess ->{
                    loadingDialog.dismiss()
                    val intent = Intent(this,HomeScreen::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                is Response.Error -> {
                    loadingDialog.dismiss()
                    alertDialog.show()
                    dialogBinding.msg.text=it.errorMsg.toString()
                }
                is Response.Loading ->{
                    loadingDialog.show()
                }
            }
        })

        binding.alreadyAccount.setOnClickListener {
            val intent = Intent(this,LoginScreen::class.java)
            startActivity(intent)
        }

        binding.imgErrorClose.setOnClickListener {

            binding.signinNameError.visibility=View.GONE
            binding.usernameView.setBackgroundResource(R.drawable.edittextback)
            binding.imgErrorClose.visibility = View.GONE
            binding.signInUsername.editText?.setText("")
        }

        binding.emailErrorClose.setOnClickListener {

            binding.signinEmailError.visibility=View.GONE
            binding.emailView.setBackgroundResource(R.drawable.edittextback)
            binding.emailErrorClose.visibility = View.GONE
            binding.signInEmail.editText?.setText("")
        }

        binding.passwordErrorClose.setOnClickListener {

            binding.signinPasswordError.visibility=View.GONE
            binding.passwordView.setBackgroundResource(R.drawable.edittextback)
            binding.passwordErrorClose.visibility = View.GONE
            binding.signInPassword.editText?.setText("")
        }

        binding.txtSingUp.setOnClickListener {

            if(binding.signInUsername.editText?.text.toString().isEmpty()){

                binding.signinNameError.visibility=View.VISIBLE
                binding.usernameView.setBackgroundResource(R.drawable.erroredittext)
                binding.signinNameError.text="Can't be empty"
                binding.imgErrorClose.visibility = View.VISIBLE

                return@setOnClickListener

            }

            if(binding.signInEmail.editText?.text.toString().isEmpty() && !binding.signInEmail.editText?.text.toString().contains("@")){

                binding.signinEmailError.visibility=View.VISIBLE
                binding.emailView.setBackgroundResource(R.drawable.erroredittext)
                binding.signinEmailError.text="Invalid email"
                binding.emailErrorClose.visibility = View.VISIBLE

                return@setOnClickListener

            }

            if(binding.signInPassword.editText?.text.toString().isEmpty()){

                binding.signinPasswordError.visibility=View.VISIBLE
                binding.passwordView.setBackgroundResource(R.drawable.erroredittext)
                binding.signinPasswordError.text="Can't be empty"
                binding.passwordErrorClose.visibility = View.VISIBLE

                return@setOnClickListener

            }

            loadingDialog.show()

            val username = binding.signInUsername.editText?.text.toString()
            val email = binding.signInEmail.editText?.text?.trim().toString()
            val password = binding.signInPassword.editText?.text.toString()

            val userModel = UserModel("",username,email, password,"","")
            mainActivityViewModel.customUserRegister(userModel)

        }
    }

    fun isUserAlreadyLogin() {
        if(firebaseAuth.currentUser!=null){
            val intent = Intent(this,HomeScreen::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}