package com.elaniin.poke.android.Presenters

import android.content.Context
import com.elaniin.poke.android.Api.Globales
import com.elaniin.poke.android.Interactors.HomeInteractorImpl
import com.elaniin.poke.android.Model.Pokedexe
import com.elaniin.poke.android.Model.Result
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.victordev.pokegroup.Interfaces.InterfaceGlobal
import retrofit2.Retrofit

class HomePresenterImpl() : InterfaceGlobal.HomePresenter{

    private var view:InterfaceGlobal.HomeView? = null
    private var interactor:InterfaceGlobal.HomeInteractor? = null

    constructor(view: InterfaceGlobal.HomeView) : this() {
        this.view = view
        interactor = HomeInteractorImpl(this)
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

    override fun GetRegions() {
        interactor!!.GetRegions()
    }

    override fun DatosRegion(mRegionModel: List<Result>) {
        if(view != null){
            view!!.DatosRegion(mRegionModel)
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

    override fun configureMenu(bottomNavigationBar: BottomNavigationView) {
        interactor!!.configureMenu(bottomNavigationBar)
    }

    override fun irActividad(act: Class<*>){
        if(view != null){
            view!!.irActividad(act)
        }
    }

    override fun irActivity(act: Class<*>) {
        if(view != null){
            view!!.irActivity(act)
        }
    }
}