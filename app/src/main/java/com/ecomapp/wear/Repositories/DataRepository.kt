package com.ecomapp.wear.Repositories

import android.content.Context
import android.net.Uri
import com.ecomapp.wear.Models.AddressModel
import com.ecomapp.wear.Models.BagModel
import com.ecomapp.wear.Models.BannerModel
import com.ecomapp.wear.Models.MainCatModel
import com.ecomapp.wear.Models.NotificationModel
import com.ecomapp.wear.Models.OrderIdModel
import com.ecomapp.wear.Models.OrderModel
import com.ecomapp.wear.Models.ProductIdModel
import com.ecomapp.wear.Models.ProductImageModel
import com.ecomapp.wear.Models.ProuctModel
import com.ecomapp.wear.Models.RatingModel
import com.ecomapp.wear.Models.SizeModel
import com.ecomapp.wear.Models.SubCatModel
import com.ecomapp.wear.Models.UserModel
import com.ecomapp.wear.Utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DataRepository @Inject constructor(
    val fireStoreDb: FirebaseFirestore,
    val auth: FirebaseAuth,
    val storage: FirebaseStorage
) {

    var bannerList = ArrayList<BannerModel>()
    var mainCatList = ArrayList<MainCatModel>()
    var subCatList = ArrayList<SubCatModel>()
    var productIdList = ArrayList<ProductIdModel>()
    var productList = ArrayList<ProuctModel>()
    var AllProductList = ArrayList<ProuctModel>()
    var randomProductList = ArrayList<ProuctModel>()
    var productDetail = ProuctModel()
    var imageList = ArrayList<ProductImageModel>()
    var sizeList = ArrayList<SizeModel>()
    var cartProductsList = ArrayList<BagModel>()
    var favProductsList = ArrayList<ProuctModel>()
    var userInfo = UserModel()
    var pendingOrderList = ArrayList<OrderModel>()
    var searchProductsList = ArrayList<ProuctModel>()
    var suggestedList = ArrayList<ProuctModel>()
    var ratingList = ArrayList<RatingModel>()
    var rateProductModel = ProuctModel()
    var notificationList = ArrayList<NotificationModel>()


    suspend fun LoadHomeBanners(): Response<ArrayList<BannerModel>> {

        val snapshot = withContext(Dispatchers.IO) {
            fireStoreDb.collection("HomeBanners").get().await()
        }

        val fetching = withContext(Dispatchers.IO) {
            bannerList.addAll(snapshot.toObjects(BannerModel::class.java))
        }

        return try {
            Response.Sucess(bannerList)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun LoadMainCategories(catName: String): Response<ArrayList<MainCatModel>> {

        val snapshot = withContext(Dispatchers.IO) {
            fireStoreDb.collection("Categories").document(catName).collection("MainCategories")
                .get().await()
        }

        val fetching = withContext(Dispatchers.IO) {
            mainCatList.addAll(snapshot.toObjects(MainCatModel::class.java))
        }

        return try {
            Response.Sucess(mainCatList)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }

    }

    suspend fun LoadSubCatigories(
        parentCatName: String,
        mainCatName: String
    ): Response<ArrayList<SubCatModel>> {

        val snapshot = withContext(Dispatchers.IO) {
            fireStoreDb.collection("Categories")
                .document(parentCatName)
                .collection("MainCategories")
                .document(mainCatName)
                .collection("SubCategories").get().await()
        }

        val fetching = withContext(Dispatchers.IO) {
            subCatList.addAll(snapshot.toObjects(SubCatModel::class.java))
        }

        return try {
            Response.Sucess(subCatList)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun LoadProducts(
        parentCatName: String,
        mainCatName: String,
        subCatName: String
    ): Response<ArrayList<ProuctModel>> {

        val snapshot = withContext(Dispatchers.IO) {
            fireStoreDb.collection("Categories")
                .document(parentCatName)
                .collection("MainCategories")
                .document(mainCatName)
                .collection("SubCategories")
                .document(subCatName)
                .collection("Products").get().await()
        }


        val productIds = ArrayList<String>()

        val fetching = withContext(Dispatchers.IO) {

            for (document in snapshot.getDocuments()) {
                val productId = document.getString("productId")
                productIds.add(productId!!)
            }
        }


        val snapshot2 = withContext(Dispatchers.IO) {
            fireStoreDb.collection("AllProducts").get().await()
        }
        val fetching2 = withContext(Dispatchers.IO) {
            for (document in snapshot2.getDocuments()) {
                val productId = document.getString("productId")
                for (id in productIds) {
                    if (id.equals(productId)) {
                        productList.add(document.toObject(ProuctModel::class.java)!!)
                    }
                }
            }
        }


        return try {
            Response.Sucess(productList)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun LoadAllProducts(): Response<ArrayList<ProuctModel>> {

        val snapshot = withContext(Dispatchers.IO) {
            fireStoreDb.collection("AllProducts").get().await()
        }

        val fetching = withContext(Dispatchers.IO) {
            AllProductList.addAll(snapshot.toObjects(ProuctModel::class.java))
        }

        return try {
            Response.Sucess(AllProductList)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun LoadProductImages(productId: String): Response<ArrayList<ProductImageModel>> {

        val snapshot = withContext(Dispatchers.IO) {
            fireStoreDb.collection("AllProducts")
                .document(productId)
                .collection("ProductImages")
                .get().await()
        }

        val fetching = withContext(Dispatchers.IO) {
            imageList.addAll(snapshot.toObjects(ProductImageModel::class.java))
        }

        return try {
            Response.Sucess(imageList)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun LoadProductSizes(productId: String): Response<ArrayList<SizeModel>> {
        val snapshot = withContext(Dispatchers.IO) {
            fireStoreDb.collection("AllProducts")
                .document(productId).collection("Sizes")
                .get().await()
        }

        val fetching = withContext(Dispatchers.IO) {
            sizeList.addAll(snapshot.toObjects(SizeModel::class.java))
        }

        return try {
            Response.Sucess(sizeList)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun LoadProductDetails(productId: String): Response<ProuctModel> {

        val snapshot = withContext(Dispatchers.IO) {
            fireStoreDb.collection("AllProducts")
                .document(productId)
                .get().await()
        }

        val fetching = withContext(Dispatchers.IO) {
            productDetail = snapshot.toObject(ProuctModel::class.java)!!
        }

        return try {
            Response.Sucess(productDetail)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun LoadRandomProducts(): Response<ArrayList<ProuctModel>> {

        val snapshot = withContext(Dispatchers.IO) {
            fireStoreDb.collection("AllProducts").orderBy("productSubTitle").get().await()
        }

        val fetching = withContext(Dispatchers.IO) {
            randomProductList.addAll(snapshot.toObjects(ProuctModel::class.java))
        }

        return try {
            Response.Sucess(randomProductList)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun LoadBannersProducts(loadUsing: String): Response<ArrayList<ProuctModel>> {

        val snapshot = withContext(Dispatchers.IO) {
            fireStoreDb.collection("HomeBanners")
                .document(loadUsing)
                .collection("Products")
                .get().await()
        }


        val productIds = ArrayList<String>()

        val fetching = withContext(Dispatchers.IO) {

            for (document in snapshot.getDocuments()) {
                val productId = document.getString("productId")
                productIds.add(productId!!)
            }
        }

        val snapshot2 = withContext(Dispatchers.IO) {
            fireStoreDb.collection("AllProducts")
                .orderBy("productId", Query.Direction.DESCENDING)
                .get().await()
        }

        val fetching2 = withContext(Dispatchers.IO) {
            for (document in snapshot2.getDocuments()) {
                val productId = document.getString("productId")
                for (favID in productIds) {
                    if (favID.equals(productId)) {
                        productList.add(document.toObject(ProuctModel::class.java)!!)
                    }
                }
            }
        }

        return try {
            Response.Sucess(productList)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun addToCart(bagModel: BagModel): Response<String> {

        val snapshot = withContext(Dispatchers.IO) {
            fireStoreDb.collection("users")
                .document(auth.uid!!)
                .collection("cart")
                .document(bagModel.productId!!)
                .set(bagModel).await()
        }

        return try {
            Response.Sucess("Sucess")
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun loadCartProducts(): Response<ArrayList<BagModel>> {

        val snapshot = withContext(Dispatchers.IO) {
            fireStoreDb.collection("AllProducts").get().await()
        }

        val productIds = ArrayList<String>()

        val fetching = withContext(Dispatchers.IO) {

            for (document in snapshot.getDocuments()) {
                val productId = document.getString("productId")
                productIds.add(productId!!)
            }
        }

        val snapshot2 = withContext(Dispatchers.IO) {
            fireStoreDb.collection("users")
                .document(auth.uid!!)
                .collection("cart")
                .orderBy("productId", Query.Direction.DESCENDING)
                .get().await()
        }

        val fetching2 = withContext(Dispatchers.IO) {
            for (document in snapshot2.getDocuments()) {
                val productId = document.getString("productId")
                for (id in productIds) {
                    if (id.equals(productId)) {
                        cartProductsList.add(document.toObject(BagModel::class.java)!!)
                    }
                }
            }
        }

        return try {
            Response.Sucess(cartProductsList)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }


//        val snapshot2 = withContext(Dispatchers.IO) {
//            fireStoreDb.collection("users")
//                .document(auth.uid!!)
//                .collection("cart")
//                .orderBy("productId", Query.Direction.DESCENDING)
//                .get().await()
//        }
//        val fetching = withContext(Dispatchers.IO){
//            cartProductsList.addAll(snapshot2.toObjects(BagModel::class.java))
//        }
//
//        return try {
//            Response.Sucess(cartProductsList)
//        } catch (e: Exception) {
//            Response.Error(e.message.toString())
//        }
    }

    suspend fun removeFromCart(productId: String): Response<String> {

        val snapshot = withContext(Dispatchers.IO) {
            fireStoreDb.collection("users")
                .document(auth.uid!!)
                .collection("cart")
                .document(productId)
                .delete().await()
        }

        return try {
            Response.Sucess("Sucess")
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun addToFavourites(productId: String): Response<String> {

        val productIdModel = ProductIdModel(productId)

        val snapshot = withContext(Dispatchers.IO) {
            fireStoreDb.collection("users")
                .document(auth.uid!!)
                .collection("favourites")
                .document(productId!!)
                .set(productIdModel).await()
        }

        return try {
            Response.Sucess("Sucess")
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun loadFavProducts(): Response<ArrayList<ProuctModel>> {

        val snapshot = withContext(Dispatchers.IO) {
            fireStoreDb.collection("users")
                .document(auth.uid!!)
                .collection("favourites")
                .orderBy("productId", Query.Direction.DESCENDING)
                .get().await()
        }

        val favoriteProductIds = ArrayList<String>()

        val fetching = withContext(Dispatchers.IO) {

            for (document in snapshot.getDocuments()) {
                val favoriteProductId = document.getString("productId")
                favoriteProductIds.add(favoriteProductId!!)
            }
        }

        val snapshot2 = withContext(Dispatchers.IO) {
            fireStoreDb.collection("AllProducts")
                .orderBy("productId", Query.Direction.DESCENDING)
                .get()
                .await()
        }

        val fetching2 = withContext(Dispatchers.IO) {
            for (document in snapshot2.getDocuments()) {
                val productId = document.getString("productId")
                for (favID in favoriteProductIds) {
                    if (favID.equals(productId)) {
                        favProductsList.add(document.toObject(ProuctModel::class.java)!!)
                    }
                }
            }
        }


        return try {
            Response.Sucess(favProductsList)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun removeFromFav(productId: String): Response<String> {

        val snapshot = withContext(Dispatchers.IO) {
            fireStoreDb.collection("users")
                .document(auth.uid!!)
                .collection("favourites")
                .document(productId!!)
                .delete().await()
        }

        return try {
            Response.Sucess("Sucess")
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun getUserInfo(): Response<UserModel> {

        val snapshot = withContext(Dispatchers.IO) {
            fireStoreDb.collection("users")
                .document(auth.uid!!)
                .get().await()
        }

        val fetching = withContext(Dispatchers.IO) {
            userInfo = snapshot.toObject(UserModel::class.java)!!
        }

        return try {
            Response.Sucess(userInfo)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun submitOrder(orderList: ArrayList<OrderModel>,notificationList: ArrayList<NotificationModel>): Response<String> {

        var addressModel: AddressModel? = null

        val Orders = withContext(Dispatchers.IO) {

            var i: Int = 1

            for (singleOrder in orderList) {

                singleOrder.userId = auth.uid

                addressModel = AddressModel(
                    singleOrder.name, singleOrder.address, singleOrder.city, singleOrder.state,
                    singleOrder.zipCode, singleOrder.phoneNO
                )

                fireStoreDb.collection("Orders")
                    .document(singleOrder.orderId!!)
                    .set(singleOrder)
                    .await()
                i++
            }
        }

        val PendingOrders = withContext(Dispatchers.IO) {

            var i: Int = 1

            for (singleOrder in orderList) {

                val orderId = OrderIdModel(singleOrder.orderId!!)

                fireStoreDb.collection("PendingOrders")
                    .document(singleOrder.orderId!!)
                    .set(orderId)
                    .await()
                i++
            }
        }

        val UserPendingOrders = withContext(Dispatchers.IO) {

            for (singleOrder in orderList) {

                val orderId = OrderIdModel(singleOrder.orderId!!)

                fireStoreDb.collection("users")
                    .document(auth.uid!!)
                    .collection("pendingOrders")
                    .document(singleOrder.orderId!!)
                    .set(orderId)
                    .await()

            }
        }

        val saveAddress = withContext(Dispatchers.IO) {

            fireStoreDb.collection("users")
                .document(auth.uid!!)
                .collection("Address")
                .document("userAddress")
                .set(addressModel!!)
                .await()
        }

        withContext(Dispatchers.IO){

            for (notifcation in notificationList) {

                val notificationId = notifcation.notificationText
                notifcation.notificationText = "Order submitted successfully, your tracking no is : "+notificationId

                fireStoreDb.collection("users")
                    .document(auth.uid!!)
                    .collection("notifications")
                    .document(notificationId!!)
                    .set(notifcation).await()

            }
        }

        return try {
            Response.Sucess("Sucess")
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun LoadOrders(orderCat: String): Response<ArrayList<OrderModel>> {

        val snapshot = withContext(Dispatchers.IO) {
            fireStoreDb.collection("users")
                .document(auth.uid!!)
                .collection(orderCat)
                .get().await()
        }

        val orderIds = ArrayList<String>()

        val fetching = withContext(Dispatchers.IO) {

            for (document in snapshot.getDocuments()) {
                val orderId = document.getString("orderId")
                orderIds.add(orderId!!)
            }
        }
        val snapshot2 = withContext(Dispatchers.IO) {
            fireStoreDb.collection("Orders").get().await()
        }

        val fetching2 = withContext(Dispatchers.IO) {
            for (document in snapshot2.getDocuments()) {
                val orderId = document.getString("orderId")
                for (id in orderIds) {
                    if (id.equals(orderId)) {
                        pendingOrderList.add(document.toObject(OrderModel::class.java)!!)
                    }
                }
            }
        }

        return try {
            Response.Sucess(pendingOrderList)
        }catch (e : Exception){
            Response.Error(e.message.toString())
        }
    }

    suspend fun LoadAddress() : Response<AddressModel>{

        var addressModel = AddressModel()

        val saveAddress = withContext(Dispatchers.IO){

            fireStoreDb.collection("users")
                .document(auth.uid!!)
                .collection("Address")
                .document("userAddress")
                .get()
                .await()

        }
        val fetching = withContext(Dispatchers.IO){
            addressModel = saveAddress.toObject(AddressModel::class.java)!!
        }

        return try {
            Response.Sucess(addressModel)
        }catch (e : Exception){
            Response.Error(e.message.toString())
        }
    }

    suspend fun getTNC() : Response<OrderIdModel>{

        val termCondition : OrderIdModel

        val saveAddress = withContext(Dispatchers.IO){

            fireStoreDb.collection("TNC")
                .document("TermCondition")
                .get()
                .await()

        }
        val fetching = withContext(Dispatchers.IO){
            termCondition = saveAddress.toObject(OrderIdModel::class.java)!!
        }

        return try {
            Response.Sucess(termCondition)
        }catch (e : Exception){
            Response.Error(e.message.toString())
        }
    }

    suspend fun updateUserInfo(userModel :  UserModel,uploadPhoto : Boolean) : Response<String>{

        if(uploadPhoto){
            val upload = withContext(Dispatchers.IO){
                val ref =  storage.reference.child("Users")
                    .child("userProfilePhotos").child(System.currentTimeMillis().toString())
                ref.putFile(Uri.parse(userModel.userImage)).await()
                ref.downloadUrl.await()
            }

            val snapshot = withContext(Dispatchers.IO){
                userModel.userImage = upload.toString()
                fireStoreDb.collection("users")
                    .document(auth.uid!!)
                    .set(userModel).await()
            }

        }else{
            val snapshot = withContext(Dispatchers.IO){
                fireStoreDb.collection("users")
                    .document(auth.uid!!)
                    .set(userModel).await()
            }
        }

        return try {
            Response.Sucess("Success")
        }catch (e : Exception){
            Response.Error(e.message.toString())
        }
    }

    suspend fun searchProduct(search : String) : Response<ArrayList<ProuctModel>>{
        val snapshot2 = withContext(Dispatchers.IO){
            fireStoreDb.collection("AllProducts")
                .whereEqualTo("productTitle",search).get().await()
        }

        val fetching2 = withContext(Dispatchers.IO){
            searchProductsList.addAll(snapshot2.toObjects(ProuctModel::class.java))
        }

        return try {
            Response.Sucess(searchProductsList)
        }catch (e : Exception){
            Response.Error(e.message.toString())
        }
    }

    suspend fun rateProduct (ratingModel : RatingModel, productId: String) : Response<String>{

        val rate = withContext(Dispatchers.IO){
            fireStoreDb.collection("AllProducts")
                .document(productId)
                .collection("Rating")
                .document(System.currentTimeMillis().toString())
                .set(ratingModel)
                .await()

        }

        val snapshot = withContext(Dispatchers.IO){

            val getRateReference = fireStoreDb.collection("AllProducts")
                .document(productId)

            fireStoreDb.runTransaction {
                val prouctModel : ProuctModel = it.get(getRateReference).toObject(ProuctModel::class.java)!!

                val oldRating : Float = prouctModel.rate!!
                val oldNoOfRating : Float = prouctModel.noOfRating!!

                val totalOldRating : Float = oldRating*oldNoOfRating

                var newRating : Float = ratingModel.userRating!!

                val totalNewRating : Float = totalOldRating+newRating

                val totalNewNoOfRating : Float = oldNoOfRating+1

                newRating = totalNewRating/totalNewNoOfRating
                it.update(getRateReference,"rate",newRating,"noOfRating",totalNewNoOfRating)

            }.await()
        }

        return try {
            Response.Sucess("success")
        }catch (e : Exception){
            Response.Error(e.message.toString())
        }
    }

    suspend fun getRating (productId: String) : Response<ArrayList<RatingModel>>{

        val snapshot = withContext(Dispatchers.IO){
            fireStoreDb.collection("AllProducts")
                .document(productId).collection("Rating")
                .get()
                .await()
        }

        val fetching = withContext(Dispatchers.IO){
            ratingList.addAll(snapshot.toObjects(RatingModel::class.java))
        }

        return try {
            Response.Sucess(ratingList)
        }catch (e : Exception){
            Response.Error(e.message.toString())
        }
    }

    suspend fun getNotifications() : Response<ArrayList<NotificationModel>>{

        val snapshot = withContext(Dispatchers.IO){
            fireStoreDb.collection("users")
                .document(auth.uid!!)
                .collection("notifications")
                .get().await()
        }

        val fetching = withContext(Dispatchers.IO){
            notificationList.addAll(snapshot.toObjects(NotificationModel::class.java))
        }

        return try {
            Response.Sucess(notificationList)
        }catch (e : Exception){
            Response.Error(e.message.toString())
        }
    }

}