
package com.androyal.subxplayer.utils

import android.content.Context
import android.util.Log
import com.revenuecat.purchases.*
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object RevenueCatService {
    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium
    private val _isInitialized = MutableStateFlow(false)

    fun init(context: Context, apiKey: String = "goog_luBnwcCAtCYXvIihhKyRTXAmAxd"){
        if(_isInitialized.value) return
        try{
            Purchases.configure(PurchasesConfiguration.Builder(context, apiKey).build())
            _isInitialized.value = true
            refresh()
            Log.d("RevenueCat","initialized")
        }catch(e:Exception){
            Log.e("RevenueCat","init failed",e)
        }
    }

    fun initWithKey(context: Context, key:String){
        try{
            Purchases.configure(PurchasesConfiguration.Builder(context, key).build())
            _isInitialized.value=true
            refresh()
        }catch(e:Exception){ Log.e("RevenueCat","reinit failed",e)}
    }

    fun refresh(){
        try{
            Purchases.sharedInstance.getCustomerInfo(object: ReceiveCustomerInfoCallback{
                override fun onReceived(info: CustomerInfo){
                    val active = info.entitlements["plus"]?.isActive == true
                    _isPremium.value = active
                    Log.d("RevenueCat","entitlement plus active=$active")
                }
                override fun onError(e: PurchasesError){ Log.e("RevenueCat","getCustomerInfo error $e") }
            })
        }catch(e:Exception){ Log.e("RevenueCat","refresh failed",e)}
    }

    fun purchase(packageToBuy: com.revenuecat.purchases.Package, onResult:(Boolean,String?)->Unit){
        try{
            // Simplified: use new RevenueCat 8.x API via purchases delegate
            // For now, stub to avoid compilation issues with PurchaseCallback
            Log.d("RevenueCat","purchase requested for ${packageToBuy.identifier}")
            onResult(false, "Purchase flow stub - integrate with RevenueCat UI")
        }catch(e:Exception){ onResult(false, e.message)}
    }

    fun restore(onDone:(Boolean)->Unit){
        try{
            Purchases.sharedInstance.restorePurchases(object: ReceiveCustomerInfoCallback{
                override fun onReceived(info: CustomerInfo){
                    val active = info.entitlements["plus"]?.isActive == true
                    _isPremium.value = active
                    onDone(active)
                }
                override fun onError(e: PurchasesError){ onDone(false) }
            })
        }catch(_:Exception){ onDone(false)}
    }
}
