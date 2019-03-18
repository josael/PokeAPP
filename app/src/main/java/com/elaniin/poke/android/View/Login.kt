package com.elaniin.poke.android.View

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.elaniin.poke.android.Api.App
import com.elaniin.poke.android.Api.Globales
import com.elaniin.poke.android.Presenters.LoginPresenterImpl
import com.elaniin.poke.android.R
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.victordev.pokegroup.Interfaces.InterfaceGlobal
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class Login : AppCompatActivity(), InterfaceGlobal.LoginView {

    //variables
    var contexto: Context? = null
    //varable de conexion
    var conexion: Boolean? = false
    @Inject
    lateinit var funciones: Globales
    //dialogo
    var dialog: Dialog? = null
    val app = App()
    val GOOGLE_LOG_IN_RC = 1
    private var callbackManager: CallbackManager? = null
    // Firebase Auth Object.
    var firebaseAuth: FirebaseAuth? = null
    private var parent_view: View? = null

    private var presenter:InterfaceGlobal.LoginPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //inicializar datos
        contexto = this
        try {
            //inizializar inject
            val app = App()
            app.getNetComponent("", contexto!!, this)!!.inject(this)
            //configurar dialogo
            configureDialog()

        } catch (e: Exception) {
            System.out.println("Error: $e")
        }
        //background
        funciones.fondo(contexto!!, R.drawable.pokemon_icon, logoPoke)
        funciones.fondo(contexto!!, R.drawable.player, logoUser)
        //fuentes
        bienvenido.typeface = Typeface.createFromAsset(assets, "font/Roboto-Bold.ttf")
        registrate.typeface = Typeface.createFromAsset(assets, "font/Roboto-Regular.ttf")

        callbackManager = CallbackManager.Factory.create()
        //inicializar firebase Auth
        firebaseAuth = Globales.firebaseAuth
        //inicializar presentador
        presenter = LoginPresenterImpl(this)
        //obtener conexion
        conexion = funciones.isNetworkAvailable(contexto!!)

        presenter!!.Componets(this,btnFacebook,contexto!!,callbackManager!!,funciones)
        presenter!!.Sesion()
        //eventos on click
        btnFacebookCust.setOnClickListener {
            presenter!!.Facebook(btnFacebook)
        }
        btnGoogle.setOnClickListener {
            val signInIntent = presenter!!.loginGoogle()
            startActivityForResult(signInIntent, GOOGLE_LOG_IN_RC)
        }

        //crear hash de facebook
        //getHash()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Login Facebook
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_LOG_IN_RC) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                presenter!!.Google(result.signInAccount!!)
            } else {
                presenter!!.showSnack("Error.")
            }
        }
    }

    //configuracion de dialogo de carga
    fun configureDialog(){
        //inicializar dialogo
        dialog = Dialog(contexto!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.progress_dialog)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setCancelable(false)
    }

    override fun showProgress(cargador: Boolean) {
        if (cargador) {
            dialog!!.show()
        } else {
            dialog!!.dismiss()
        }
    }
    override fun navigateToHome() {
        irActividad()
    }

    fun irActividad() {
        val intent = Intent(applicationContext, Home::class.java)
        intent.action = Intent.ACTION_MAIN
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun showSnack(text:String){
        parent_view = findViewById(android.R.id.content)
        funciones.SnackbarCustom(parent_view!!,  text,contexto!!)
    }

    override fun SnackConection(text:String){
        parent_view = findViewById(android.R.id.content)
        funciones.SnackbarConection(parent_view!!, text,contexto!!)
    }

   /* @SuppressLint("PackageManagerGetSignatures")
    private fun getHash() {
        val info: PackageInfo
        try {
            info = this.packageManager.getPackageInfo("com.elaniin.pokemon.android", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = String(Base64.encode(md.digest(), 0))
                //String something = new String(Base64.encodeBytes(md.digest()));
                //Log.e("hash key:", something)
                println("hash key: $something")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("no such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }
    }*/
}
