package com.elaniin.poke.android.Presenters

import android.content.Context
import android.view.View
import com.elaniin.poke.android.Api.Globales
import com.elaniin.poke.android.Interactors.EquipoInteractorImpl
import com.elaniin.poke.android.Model.EquipoModel
import com.elaniin.poke.android.Model.Pokedexe
import com.elaniin.poke.android.Model.Pokemo
import com.victordev.pokegroup.Interfaces.InterfaceGlobal
import retrofit2.Retrofit
import java.util.*

class EquipoPresenterImpl() : InterfaceGlobal.EquipoPresenter{

    private var view:InterfaceGlobal.EquipoView? = null
    private var interactor:InterfaceGlobal.EquipoInteractor? = null

    constructor(view: InterfaceGlobal.EquipoView) : this() {
        this.view = view
        interactor = EquipoInteractorImpl(this)
    }

    override fun Componets(ctx: Context, func: Globales,mRetrofit: Retrofit) {
        interactor!!.Componets(ctx,func,mRetrofit)
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

    override fun CargarEquipos() {
        interactor!!.CargarEquipos()
    }

    override fun Equipos(mEquipoModel: ArrayList<EquipoModel>) {
        if (view != null) {
            view!!.Equipos(mEquipoModel)
        }
    }
    override fun showBottomSheetDialogPoke(bottom_sheet: View, pokemon: Pokemo) {
        interactor!!.showBottomSheetDialogPoke(bottom_sheet,pokemon)
    }

    override fun PokemonDetalle(pokemon:Pokemo) {
        if(view != null){
            view!!.PokemonDetalle(pokemon)
        }
    }

    override fun showBottomSheetDialogList(bottom_sheet: View, mEquipoModel:EquipoModel) {
        interactor!!.showBottomSheetDialogList(bottom_sheet,mEquipoModel)
    }

    override fun DetalleEquipo(mEquipoModel:EquipoModel) {
        if(view != null){
            view!!.DetalleEquipo(mEquipoModel)
        }
    }

    override fun EditarEquipo(act: Class<*>,datos:String,boolean:Boolean) {
        if(view != null){
            view!!.EditarEquipo(act,datos,boolean)
        }
    }

    override fun Pokedex(URL:String){
        if(view != null){
            view!!.Pokedex(URL)
        }
    }

    override fun GetPokedex() {
        interactor!!.GetPokedex()
    }

    override fun DatosPokedex(pokedexes: List<Pokedexe>) {
        if(view != null){
            view!!.DatosPokedex(pokedexes)
        }
    }

    override fun GetPokemon() {
        interactor!!.GetPokemon()
    }


    override fun showFondo(cargador: Boolean) {
        if(view != null){
            view!!.showFondo(cargador)
        }
    }
}