package com.yourcompany.propertyroi.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import com.yourcompany.propertyroi.data.models.Entitlement
import com.yourcompany.propertyroi.data.repository.EntitlementRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillingManager(
    private val context: Context,
    private val entitlementRepository: EntitlementRepository
) {
    
    companion object {
        const val PRODUCT_ID = "roi_pro_unlock"
    }
    
    private var billingClient: BillingClient? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    
    private val _productDetails = MutableStateFlow<ProductDetails?>(null)
    val productDetails: StateFlow<ProductDetails?> = _productDetails.asStateFlow()
    
    private val _purchaseState = MutableStateFlow<PurchaseState>(PurchaseState.Idle)
    val purchaseState: StateFlow<PurchaseState> = _purchaseState.asStateFlow()
    
    sealed class PurchaseState {
        object Idle : PurchaseState()
        object Loading : PurchaseState()
        object Success : PurchaseState()
        data class Error(val message: String) : PurchaseState()
    }
    
    init {
        setupBillingClient()
    }
    
    private fun setupBillingClient() {
        billingClient = BillingClient.newBuilder(context)
            .setListener { billingResult, purchases ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    handlePurchases(purchases)
                }
            }
            .enablePendingPurchases()
            .build()
        
        connectToBilling()
    }
    
    private fun connectToBilling() {
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryProductDetails()
                    queryPurchases()
                }
            }
            
            override fun onBillingServiceDisconnected() {
                // Retry connection
            }
        })
    }
    
    private fun queryProductDetails() {
        scope.launch {
            val productList = listOf(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(PRODUCT_ID)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            )
            
            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build()
            
            val result = withContext(Dispatchers.IO) {
                billingClient?.queryProductDetails(params)
            }
            
            result?.productDetailsList?.firstOrNull()?.let {
                _productDetails.value = it
            }
        }
    }
    
    fun launchPurchaseFlow(activity: Activity) {
        val productDetails = _productDetails.value ?: return
        
        _purchaseState.value = PurchaseState.Loading
        
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )
        
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()
        
        billingClient?.launchBillingFlow(activity, billingFlowParams)
    }
    
    private fun handlePurchases(purchases: List<Purchase>) {
        purchases.forEach { purchase ->
            if (purchase.products.contains(PRODUCT_ID) && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                if (!purchase.isAcknowledged) {
                    acknowledgePurchase(purchase)
                } else {
                    updateEntitlement(purchase)
                }
            }
        }
    }
    
    private fun acknowledgePurchase(purchase: Purchase) {
        scope.launch {
            val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            
            val result = withContext(Dispatchers.IO) {
                billingClient?.acknowledgePurchase(acknowledgePurchaseParams)
            }
            
            if (result?.responseCode == BillingClient.BillingResponseCode.OK) {
                updateEntitlement(purchase)
                _purchaseState.value = PurchaseState.Success
            }
        }
    }
    
    private fun updateEntitlement(purchase: Purchase) {
        scope.launch {
            val entitlement = Entitlement(
                hasPro = true,
                purchaseToken = purchase.purchaseToken,
                purchaseTime = purchase.purchaseTime
            )
            entitlementRepository.updateEntitlement(entitlement)
        }
    }
    
    fun queryPurchases() {
        scope.launch {
            val params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
            
            val result = withContext(Dispatchers.IO) {
                billingClient?.queryPurchasesAsync(params) { _, purchases ->
                    handlePurchases(purchases)
                }
            }
        }
    }
    
    fun restorePurchases() {
        _purchaseState.value = PurchaseState.Loading
        queryPurchases()
        
        scope.launch {
            kotlinx.coroutines.delay(2000)
            val entitlement = entitlementRepository.getEntitlementOnce()
            if (entitlement?.hasPro == true) {
                _purchaseState.value = PurchaseState.Success
            } else {
                _purchaseState.value = PurchaseState.Error("No purchases found")
            }
        }
    }
    
    fun endConnection() {
        billingClient?.endConnection()
    }
}
