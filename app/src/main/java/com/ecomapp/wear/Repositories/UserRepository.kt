package com.ecomapp.wear.Repositories

import com.ecomapp.wear.Models.UserModel
import com.ecomapp.wear.Utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UserRepository @Inject constructor (val firebaseAuth: FirebaseAuth,val database : FirebaseFirestore) {
     suspend fun customUserRegister (userModel: UserModel)  : Response<FirebaseUser> {

        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(userModel.email!!, userModel.password!!).await()

            userModel.uid = firebaseAuth.uid

            database.collection("users")
                .document(userModel.uid!!)
                .set(userModel).await()

            Response.Sucess(result.user)

        }catch (e : Exception){
            Response.Error(e.message.toString())
        }

    }
    suspend fun customUserLogin(userModel: UserModel) : Response<FirebaseUser>{

        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(userModel.email!!, userModel.password!!).await()
            Response.Sucess(result.user)

        }catch (e : Exception){
            Response.Error(e.message.toString())
        }
    }

    suspend fun sendPasswordResetLink(email : String) : Response<String>{

        return try {
            val result = firebaseAuth.sendPasswordResetEmail(email).await()
            Response.Sucess("Sucess")

        }catch (e : Exception){
            Response.Error(e.message.toString())
        }
    }

}