package com.elaniin.poke.android.Api

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.elaniin.poke.android.Model.Pokemo
import com.elaniin.poke.android.Model.PokemonEntry
import com.elaniin.poke.android.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

open class Globales {
    companion object {
        //Firebase Auth
        var firebaseAuth: FirebaseAuth? = null
        var firebaseDatabase: FirebaseDatabase? = null
        lateinit var pokemon_entries: List<PokemonEntry>
        lateinit var region:String
        lateinit var idRegion:String
        var ListPokemon: ArrayList<Pokemo> = ArrayList()

    }

    //URLS
    var URL_API = "aHR0cHM6Ly9wb2tlYXBpLmNvL2FwaS92Mi8="
    var SUFIJO_REGION = "cmVnaW9uLw=="

    //Obtener version de app
    fun getVersionApp(context: Context): String {
        try {
            var versionapp = context.packageManager.getPackageInfo(context.packageName, 0).versionName
            var versionmobile = ""
            try {
                versionmobile = versionapp.toLowerCase()
                versionmobile = versionmobile.replace("[^\\d.]".toRegex(), "")

            } catch (e: Exception) { }

            versionapp = versionmobile
            return versionapp
        } catch (e: Exception) {
        }
        return ""
    }

    //fondos locales
    @SuppressLint("CheckResult")
    fun fondo(contexto: Context, id: Int, img: ImageView) {
        try {
            val options = RequestOptions()
                .centerCrop()
                .placeholder(id)
                .priority(Priority.NORMAL)
                .format(DecodeFormat.PREFER_RGB_565)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .dontAnimate()
                .fitCenter()

            Glide.with(contexto)
                .asBitmap()
                .load(id)
                .apply(options)
                .into(img)

        } catch (e: Exception) {
        }
    }

    //Fondos de servicio
    @SuppressLint("CheckResult")
    fun fondoURL(contexto: Context, url: String, img: ImageView) {
        try {
            val options = RequestOptions()
                .placeholder(R.drawable.pokeball)
                .priority(Priority.NORMAL)
                .format(DecodeFormat.PREFER_RGB_565)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .fitCenter()

            Glide.with(contexto)
                .asBitmap()
                .load(url)
                .apply(options)
                .into(img)
        } catch (e: Exception) {
        }

    }

    ///detectar si hay conexion
    fun isNetworkAvailable(contexto: Context): Boolean {
        val connectivityManager = contexto.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected

    }
    //encode and decode  string

    fun encrypt(string: String): String {
        // encode
        val encodeValue = android.util.Base64.encode(string.toByteArray(), android.util.Base64.DEFAULT)
        return String(encodeValue)
    }

    fun decrypt(string: String): String {
        //decode
        val decodeValue = android.util.Base64.decode(string, android.util.Base64.DEFAULT)
        return String(decodeValue)
    }

    fun SnackbarCustom(parent_view: View, mensaje: String, contexto: Context) {
        var snackbar = Snackbar.make(parent_view, mensaje, Snackbar.LENGTH_LONG)
        val snackBarView = snackbar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(contexto, R.color.colorApp))
        val textView: TextView = snackBarView.findViewById (R.id.snackbar_text)
        textView.typeface = Typeface.createFromAsset(contexto.assets, "font/Roboto-Bold.ttf")
        textView.setTextColor(ContextCompat.getColor(contexto, R.color.blanco))
        snackbar.show()
    }

    fun SnackbarConection(parent_view: View, mensaje: String, contexto: Context) {
        var snackbar = Snackbar.make(parent_view, mensaje, Snackbar.LENGTH_LONG)
        val snackBarView = snackbar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(contexto, R.color.colorApp))
        val textView: TextView = snackBarView.findViewById (R.id.snackbar_text)
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_wifi_off, 0)
        textView.compoundDrawablePadding = 5
        textView.typeface = Typeface.createFromAsset(contexto.assets, "font/Roboto-Bold.ttf")
        textView.setTextColor(ContextCompat.getColor(contexto, R.color.blanco))
        snackbar.show()
    }
}