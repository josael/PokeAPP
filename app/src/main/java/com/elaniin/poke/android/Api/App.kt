package com.elaniin.poke.android.Api

import android.app.Activity
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.elaniin.poke.android.Componente.DaggerInyectComponent
import com.elaniin.poke.android.Componente.InyectComponent
import com.elaniin.poke.android.Modulo.LibModule
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.fabric.sdk.android.Fabric



class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        //iniicalizar Crashlytics
        Fabric.with(this, Crashlytics())
        //intancia FirebaseAuth
        Globales.firebaseAuth = FirebaseAuth.getInstance()
        //intancia FirebaseDatabase
        Globales.firebaseDatabase = FirebaseDatabase.getInstance()
        //inicializacion sdk facebook
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }

    private lateinit var mNetComponent: InyectComponent

    fun getNetComponent(url:String, ctx: Context, actv: Activity): InyectComponent? {

        mNetComponent = DaggerInyectComponent.builder()
            .libModule(LibModule(url,ctx,actv))
            .build()
        return mNetComponent
    }
}