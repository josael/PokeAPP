package com.elaniin.poke.android.Presenters

import android.content.Context
import android.content.Intent
import com.elaniin.poke.android.Api.Globales
import com.elaniin.poke.android.Interactors.LoginInteractorImpl
import com.facebook.CallbackManager
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.victordev.pokegroup.Interfaces.InterfaceGlobal

class LoginPresenterImpl() : InterfaceGlobal.LoginPresenter{


    private var view:InterfaceGlobal.LoginView? = null
    private var interactor:InterfaceGlobal.LoginInteractor? = null

    constructor(view: InterfaceGlobal.LoginView) : this() {
        this.view = view
        interactor = LoginInteractorImpl(this)
    }

    override fun Facebook(btnFacebook: LoginButton) {
        if(view != null){
            view!!.showProgress(true)
            interactor!!.FacebookLogin(btnFacebook)
        }
    }

    override fun irActividad() {
        if (view != null) {
            view!!.navigateToHome()
            view!!.showProgress(false)
        }
    }

    override fun Componets(
        mLogin: com.elaniin.poke.android.View.Login,
        btnFacebook: LoginButton,
        ctx: Context,
        callbackManager: CallbackManager,
        func: Globales
    ) {
        interactor!!.Componets(mLogin,btnFacebook,ctx,callbackManager,func)
    }

    override fun loginGoogle(): Intent {
        if (view != null) {
            view!!.showProgress(true)
        }
        return interactor!!.loginGoogle()
    }

    override fun Sesion() {
        interactor!!.Sesion()
    }

    override fun showProgress(boolean: Boolean) {
        if (view != null) {
            view!!.showProgress(boolean)
        }
    }

    override fun Google(signInAccount: GoogleSignInAccount) {
        interactor!!.firebaseGoogle(signInAccount)
    }

    override fun showSnack(text: String) {
        if (view != null) {
            view!!.showSnack(text)
        }
    }
    override fun SnackConection(text: String) {
        if (view != null) {
            view!!.SnackConection(text)
        }
    }
}