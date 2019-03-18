package com.victordev.pokegroup.Interfaces

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.EditText
import com.elaniin.poke.android.Api.Globales
import com.elaniin.poke.android.Model.*
import com.elaniin.poke.android.View.Login
import com.facebook.CallbackManager
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Retrofit
import java.util.*

interface InterfaceGlobal {
    //login
    interface LoginView {
        fun navigateToHome()
        fun showProgress(cargador: Boolean)
        fun showSnack(text: String)
        fun SnackConection(text: String)
    }

    interface LoginPresenter {
        fun irActividad()
        fun Componets(mLogin: Login, btnFacebook: LoginButton, ctx: Context, callbackManager: CallbackManager, func:Globales)
        fun loginGoogle():Intent
        fun Facebook(btnFacebook:LoginButton)
        fun Sesion()
        fun showProgress(boolean: Boolean)
        fun Google(signInAccount: GoogleSignInAccount)
        fun showSnack(text: String)
        fun SnackConection(text: String)
    }

    interface LoginInteractor {
        fun FacebookLogin(btnFacebook: LoginButton)
        fun Componets(mLogin: Login, btnFacebook: LoginButton, ctx: Context, callbackManager: CallbackManager, func:Globales)
        fun loginGoogle(): Intent
        fun Sesion()
        fun firebaseGoogle(signInAccount: GoogleSignInAccount)
    }

    //Home
    interface HomeView {
        fun navigateToActivity(act: Class<*>)
        fun showProgress(boolean: Boolean)
        fun showSnack(text: String)
        fun SnackConection(text: String)
        fun DatosRegion(mRegionModel: List<Result>)
        fun DatosPokedex(pokedexes: List<Pokedexe>)
        fun irActividad(act: Class<*>)
        fun irActivity(act: Class<*>)
    }

    interface HomePresenter {
        fun Componets(ctx: Context, func:Globales,mRetrofit: Retrofit)
        fun showSnack(text: String)
        fun SnackConection(text: String)
        fun showProgress(boolean: Boolean)
        fun GetRegions()
        fun DatosRegion(mRegionModel: List<Result>)
        fun GetPokedex()
        fun DatosPokedex(pokedexes: List<Pokedexe>)
        fun GetPokemon()
        fun configureMenu(bottomNavigationBar: BottomNavigationView)
        fun irActividad(act: Class<*>)
        fun irActivity(act: Class<*>)
    }

    interface HomeInteractor {
        fun Componets(ctx: Context, func:Globales,mRetrofit: Retrofit)
        fun GetRegions()
        fun GetPokedex()
        fun GetPokemon()
        fun configureMenu(bottomNavigationBar: BottomNavigationView)

    }

    //Pokemon
    interface PokemonView {
        fun navigateToActivity(act: Class<*>)
        fun showProgress(cargador: Boolean)
        fun showSnack(text: String)
        fun SnackConection(text: String)
        fun PokemonDetalle(mPokemonDetalle:PokemonDetalle,position:Int, eliminar:Boolean)
        fun AgregarItem(position:Int, eliminar:Boolean)
        fun AgregarPoke(ListPokemon: ArrayList<Pokemo>)
        fun btnGuardar(boolean: Boolean)
        fun irActivity(act: Class<*>)
    }
    interface PokePresenter {
        fun Componets(ctx: Context, func:Globales,mRetrofit: Retrofit,EdittEquipo:EditText)
        fun showSnack(text: String)
        fun SnackConection(text: String)
        fun showProgress(boolean: Boolean)
        fun showBottomSheetDialog(bottom_sheet: View,titulo:String,detalle:String)
        fun GetPokemonDetalle(position:Int, eliminar:Boolean)
        fun PokemonDetalle(mPokemonDetalle:PokemonDetalle,position:Int, eliminar:Boolean)
        fun showBottomSheetDialogPoke(bottom_sheet: View,mPokemonDetalle:PokemonDetalle,imgPokemon:String,position:Int, eliminar:Boolean)
        fun AgregarItem(position:Int, eliminar:Boolean)
        fun AgregarPoke(ListPokemon: ArrayList<Pokemo>)
        fun GuardarEquipo(ListPokemon: ArrayList<Pokemo>,numero:String,validar:Boolean)
        fun btnGuardar(boolean: Boolean)
        fun irActivity(act: Class<*>)
    }
    interface PokeInteractor {
        fun Componets(ctx: Context, func:Globales,mRetrofit: Retrofit,EdittEquipo:EditText)
        fun showBottomSheetDialog(bottom_sheet: View,titulo:String,detalle:String)
        fun GetPokemonDetalle(position:Int, eliminar:Boolean)
        fun showBottomSheetDialogPoke(bottom_sheet: View,mPokemonDetalle:PokemonDetalle,imgPokemon:String,position:Int, eliminar:Boolean)
        fun GuardarEquipo(ListPokemon: ArrayList<Pokemo>,numero:String,validar:Boolean)
    }

    //Equipos
    interface EquipoView {
        fun navigateToActivity(act: Class<*>)
        fun showProgress(cargador: Boolean)
        fun showSnack(text: String)
        fun SnackConection(text: String)
        fun irActividad(act: Class<*>)
        fun irActivity(act: Class<*>)
        fun Equipos(mEquipoModel: ArrayList<EquipoModel>)
        fun PokemonDetalle(pokemon:Pokemo)
        fun DetalleEquipo(mEquipoModel:EquipoModel)
        fun EditarEquipo(act: Class<*>,datos:String,boolean:Boolean)
        fun Pokedex(URL:String)
        fun DatosPokedex(pokedexes: List<Pokedexe>)
        fun showFondo(cargador: Boolean)
    }
    interface EquipoPresenter {
        fun Componets(ctx: Context, func:Globales,mRetrofit: Retrofit)
        fun showProgress(cargador: Boolean)
        fun showSnack(text: String)
        fun SnackConection(text: String)
        fun CargarEquipos()
        fun Equipos(mEquipoModel: ArrayList<EquipoModel>)
        fun showBottomSheetDialogPoke(bottom_sheet: View, pokemon:Pokemo)
        fun PokemonDetalle(pokemon:Pokemo)
        fun showBottomSheetDialogList(bottom_sheet: View, mEquipoModel:EquipoModel)
        fun DetalleEquipo(mEquipoModel:EquipoModel)
        fun EditarEquipo(act: Class<*>,datos:String,boolean:Boolean)
        fun Pokedex(URL:String)
        fun GetPokedex()
        fun DatosPokedex(pokedexes: List<Pokedexe>)
        fun GetPokemon()
        fun showFondo(cargador: Boolean)
    }
    interface EquipoInteractor {
        fun Componets(ctx: Context, func:Globales,mRetrofit: Retrofit)
        fun CargarEquipos()
        fun showBottomSheetDialogPoke(bottom_sheet: View, pokemon:Pokemo)
        fun showBottomSheetDialogList(bottom_sheet: View, mEquipoModel:EquipoModel)
        fun GetPokedex()
        fun GetPokemon()
    }
}