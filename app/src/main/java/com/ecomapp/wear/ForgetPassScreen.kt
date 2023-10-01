package com.ecomapp.wear

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ecomapp.wear.Factories.ForgetPassVMF
import com.ecomapp.wear.Repositories.Response
import com.ecomapp.wear.Viewmodels.ForgetPassViewModel
import com.ecomapp.wear.databinding.ActivityForgetPassScreenBinding
import com.ecomapp.wear.databinding.EmailSendDialogBinding
import com.ecomapp.wear.databinding.LoadingDialogBinding
import com.ecomapp.wear.databinding.TryagainDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint()
class ForgetPassScreen : AppCompatActivity() {

    lateinit var binding : ActivityForgetPassScreenBinding

    lateinit var foregetPassViewModel: ForgetPassViewModel

    @Inject
    lateinit var forgetPassVMF: ForgetPassVMF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_forget_pass_screen)

        supportActionBar?.hide()

        foregetPassViewModel = ViewModelProvider(this,forgetPassVMF).get(ForgetPassViewModel::class.java)

        val emailDialogBinding : LoadingDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.loading_dialog,null,false)

        val loadingDialog = AlertDialog.Builder(this)
            .setView(emailDialogBinding.root).create()

        loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        loadingDialog.setCancelable(false)

        val dialogBinding : TryagainDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.tryagain_dialog,null,false)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root).create()

        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding.btnYes.setOnClickListener {
            alertDialog.dismiss()
        }

        val dialogDoneBinding : EmailSendDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.email_send_dialog,null,false)

        val emailSendDialog = AlertDialog.Builder(this)
            .setView(dialogDoneBinding.root).create()

        emailSendDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogDoneBinding.continueShopping.setOnClickListener {
            emailSendDialog.dismiss()
        }

        binding.emailErrorClose.setOnClickListener {

            binding.signinEmailError.visibility= View.GONE
            binding.emailView.setBackgroundResource(R.drawable.edittextback)
            binding.emailErrorClose.visibility = View.GONE
            binding.signInEmail.editText?.setText("")
        }

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.txtSend.setOnClickListener {

            val email = binding.signInEmail.editText?.text.toString()

            if(email.isEmpty()){
                binding.signinEmailError.visibility= View.VISIBLE
                binding.emailView.setBackgroundResource(R.drawable.erroredittext)
                binding.signinEmailError.text="Invalid email"
                binding.emailErrorClose.visibility = View.VISIBLE
                return@setOnClickListener
            }

            loadingDialog.show()
            foregetPassViewModel.sendPasswordResetLink(email)

        }

        foregetPassViewModel.forget_liveData.observe(this,{
            when(it){
                is Response.Sucess ->{
                    loadingDialog.dismiss()
                    emailSendDialog.show()
                }

                is Response.Error -> {
                    loadingDialog.dismiss()
                    alertDialog.show()
                    dialogBinding.msg.text=it.errorMsg.toString()
                }
                is Response.Loading -> {
                    loadingDialog.show()
                }
            }
        })

    }
}