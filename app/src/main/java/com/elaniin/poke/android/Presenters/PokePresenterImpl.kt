package com.elaniin.poke.android.Presenters

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.EditText
import com.elaniin.poke.android.Api.Globales
import com.elaniin.poke.android.Interactors.PokeInteractorImpl
import com.elaniin.poke.android.Model.Pokemo
import com.elaniin.poke.android.Model.PokemonDetalle
import com.victordev.pokegroup.Interfaces.InterfaceGlobal
import retrofit2.Retrofit
import java.util.*

class PokePresenterImpl() : InterfaceGlobal.PokePresenter{

    private var view:InterfaceGlobal.PokemonView? = null
    private var interactor:InterfaceGlobal.PokeInteractor? = null

    constructor(view: InterfaceGlobal.PokemonView) : this() {
        this.view = view
        interactor = PokeInteractorImpl(this)
    }

    override fun Componets(ctx: Context, func: Globales,mRetrofit: Retrofit,EdittEquipo: EditText) {
        interactor!!.Componets(ctx,func,mRetrofit,EdittEquipo)
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

    override fun showProgress(boolean: Boolean) {
        if (view != null) {
            view!!.showProgress(boolean)
        }
    }

    override fun showBottomSheetDialog(bottom_sheet: View,titulo:String,detalle:String) {
        interactor!!.showBottomSheetDialog(bottom_sheet,titulo,detalle)
    }

    override fun GetPokemonDetalle(position:Int, eliminar:Boolean) {
        interactor!!.GetPokemonDetalle(position,eliminar)
    }

    override fun PokemonDetalle(mPokemonDetalle: PokemonDetalle,position:Int, eliminar:Boolean) {
        if(view != null){
            view!!.PokemonDetalle(mPokemonDetalle,position, eliminar)
        }
    }

    override fun showBottomSheetDialogPoke(bottom_sheet: View, mPokemonDetalle: PokemonDetalle,imgPokemon:String,position:Int, eliminar:Boolean) {
        interactor!!.showBottomSheetDialogPoke(bottom_sheet,mPokemonDetalle,imgPokemon,position, eliminar)
    }

    override fun AgregarItem(position: Int, eliminar: Boolean) {
        if(view != null){
            view!!.AgregarItem(position,eliminar)
        }
    }
    override fun AgregarPoke(ListPokemon: ArrayList<Pokemo>) {
        if(view != null){
            view!!.AgregarPoke(ListPokemon)
        }
    }
    override fun GuardarEquipo(ListPokemon: ArrayList<Pokemo>,numero:String,validar:Boolean) {
        interactor!!.GuardarEquipo(ListPokemon,numero,validar)
    }

    @SuppressLint("RestrictedApi")
    override fun btnGuardar(boolean: Boolean) {
        if(view != null){
            view!!.btnGuardar(boolean)
        }
    }

    override fun irActivity(act: Class<*>) {
        if(view != null){
            view!!.irActivity(act)
        }
    }
}