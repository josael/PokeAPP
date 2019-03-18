package com.elaniin.poke.android.Interactors

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.elaniin.poke.android.Api.Globales
import com.elaniin.poke.android.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.victordev.pokegroup.Interfaces.InterfaceGlobal
import java.util.*


@SuppressLint("Registered")
class LoginInteractorImpl(present: InterfaceGlobal.LoginPresenter) :AppCompatActivity(), InterfaceGlobal.LoginInteractor{


    //variable de presentador
    private val presenter = present
    //Request codes
    val GOOGLE_LOG_IN_RC = 1
    // Google API Client object.
    var googleApiClient: GoogleApiClient? = null
    var LoginButtonFacebook: LoginButton? = null
    var funciones: Globales? = null
    var contexto: Context? = null
    private var parent_view: View? = null
    //Firebase Auth
    var firebaseAuth: FirebaseAuth? = null

    override fun Componets(mLogin: com.elaniin.poke.android.View.Login, btnFacebook: LoginButton, ctx: Context, callbackManager: CallbackManager, func: Globales) {
        funciones = func
        contexto = ctx
        //inicializar firebase Auth
        firebaseAuth = Globales.firebaseAuth
        //Login Facebook
        ObtenerDatosFacebook(btnFacebook,callbackManager)
        configureGoogle(ctx, mLogin)
    }

    override fun FacebookLogin(btnFacebook: LoginButton) {
        btnFacebook.performClick()
    }

    override fun loginGoogle(): Intent {
        return Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
    }

    override fun firebaseGoogle(signInAccount: GoogleSignInAccount) {
        val AuthCredential = GoogleAuthProvider.getCredential(signInAccount.idToken, null)
        firebaseAuth?.signInWithCredential(AuthCredential)?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user = firebaseAuth?.currentUser
                println("User account ID ${user?.uid}")
                println("Display Name : ${user?.displayName}")
                println("Email : ${user?.email}")
                println("Photo URL : ${user?.photoUrl}")
                println("Provider ID : ${user?.providerId}")
                presenter.irActividad()
            } else {
                presenter.showSnack("Error.")
                presenter.showProgress(false)
            }
        }
    }

    //configuracion btn google
    private fun configureGoogle(ctx: Context, Activity: com.elaniin.poke.android.View.Login) {
        // Configure Google Sign In
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(ctx.getString(R.string.request_client_id))
            .requestEmail()
            .build()
        // Creating and Configuring Google Api Client.
        googleApiClient = GoogleApiClient.Builder(Activity)
            .enableAutoManage(Activity) { }
            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
            .build()
    }

    //configracion de btn facebook
    private fun ObtenerDatosFacebook(btnFacebook: LoginButton, callbackManager: CallbackManager
    ) {
        LoginButtonFacebook = btnFacebook
        //boton facebook
        //solicitar permisos de lectura
        btnFacebook.setReadPermissions(Arrays.asList("email"))
        btnFacebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                //login exitoso
                FacebookAccessToken(result.accessToken)
                //println("token: ${result.accessToken}")
            }

            override fun onCancel() {
                //se cancelo operacion
                // Toast.makeText(this@LoginActivity, "Se cancelo operación", Toast.LENGTH_LONG).show()
                presenter.showSnack("Se cancelo operación.")
                presenter.showProgress(false)
            }

            override fun onError(error: FacebookException) {
                presenter.showSnack("Error.")
                presenter.showProgress(false)

            }
        })
    }

    private fun FacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
     /*               val user = firebaseAuth!!.currentUser
                    println("User account ID ${user?.uid}")
                    println("Display Name : ${user?.displayName}")
                    println("Email : ${user?.email}")
                    println("Photo URL : ${user?.photoUrl}")
                    println("Provider ID : ${user?.providerId}")*/
                    presenter.irActividad()
                } else {
                    presenter.showSnack("Autenticación falló.")
                    presenter.showProgress(false)
                }
            }

    }

    //verificacion sesion
    override fun Sesion() {
        val mUser = FirebaseAuth.getInstance().currentUser
        if (mUser != null) {
            mUser.getIdToken(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken = task.result!!.token
                        if (idToken != null) {
                            presenter.irActividad()
                        }
                    } else {
                        presenter.SnackConection("Comprueba tu conexión.")
                        presenter.showProgress(false)
                    }
                }
        }
    }
}