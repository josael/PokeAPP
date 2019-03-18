package com.elaniin.poke.android.Interactors

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import com.elaniin.poke.android.Api.CustomTypefaceSpan
import com.elaniin.poke.android.Api.Globales
import com.elaniin.poke.android.Interfaces.apiGexRetrofit
import com.elaniin.poke.android.Model.PokedexModel
import com.elaniin.poke.android.Model.PokemonModel
import com.elaniin.poke.android.Model.RegionModel
import com.elaniin.poke.android.R
import com.elaniin.poke.android.View.Equipos
import com.elaniin.poke.android.View.Login
import com.elaniin.poke.android.View.SelectPokemon
import com.facebook.login.LoginManager
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.victordev.pokegroup.Interfaces.InterfaceGlobal
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class HomeInteractorImpl(present: InterfaceGlobal.HomePresenter) : InterfaceGlobal.HomeInteractor, BottomNavigationView.OnNavigationItemSelectedListener{

    //variables
    var contexto: Context? = null
    //varable de conexion
    var conexion: Boolean? = false
    var funciones: Globales? = null
    //variable para api de descraga
    var apiService: apiGexRetrofit? = null
    var retrofit: Retrofit? = null
    // Firebase Auth Object.
    var firebaseAuth: FirebaseAuth? = null
    //variable de presentador
    private val presenter = present

    override fun Componets(ctx: Context, func: Globales,mRetrofit: Retrofit) {
        funciones = func
        contexto = ctx
        retrofit = mRetrofit
        apiService = retrofit!!.create(apiGexRetrofit::class.java)
        //validar conexion
        conexion = funciones!!.isNetworkAvailable(contexto!!)

        //inicializar firebase Auth
        firebaseAuth = Globales.firebaseAuth
    }

    @SuppressLint("CheckResult")
    override fun GetRegions() {
        //comprobar conexion
        if (conexion as Boolean) {
            //Cargador
            presenter.showProgress(true)
            //add sufijo
            val sufijo = funciones!!.decrypt(funciones!!.SUFIJO_REGION)
            apiService!!.getDatos(sufijo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isSuccessful) {
                        val mRegionModel =
                            Gson().fromJson<RegionModel>(result.body().toString(), RegionModel::class.java)
                        presenter.DatosRegion(mRegionModel!!.results)
                    } else {
                        presenter.showSnack("Error al obtener los datos")
                    }
                    presenter.showProgress(false)
                }, { error ->
                    presenter.showProgress(false)
                    println(error.toString())
                    presenter.showSnack("Error al obtener los datos")
                })
        } else {
            presenter.SnackConection("Comprueba tu conexión.")
            presenter.showProgress(false)
        }
    }

    @SuppressLint("CheckResult")
    override fun GetPokedex() {
        //comprobar conexion
        if (conexion as Boolean) {
            //Cargador
            presenter.showProgress(true)
            //add sufijo
            val sufijo = ""
            apiService!!.getDatos(sufijo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isSuccessful) {
                        val mPokedexModel = Gson().fromJson<PokedexModel>(result.body().toString(), PokedexModel::class.java)
                        presenter.DatosPokedex(mPokedexModel!!.pokedexes)
                        Globales.idRegion = mPokedexModel.id.toString()
                        Globales.region = mPokedexModel.name
                    } else {
                        presenter.showSnack("Error al obtener los datos")
                    }
                    presenter.showProgress(false)
                }, { error ->
                    presenter.showProgress(false)
                    println(error.toString())
                    presenter.showSnack("Error al obtener los datos")
                })
        } else {
            presenter.SnackConection("Comprueba tu conexión.")
            presenter.showProgress(false)
        }
    }

    @SuppressLint("CheckResult")
    override fun GetPokemon() {
        //comprobar conexion
        if (conexion as Boolean) {
            //Cargador
            presenter.showProgress(true)
            //add sufijo
            val sufijo = ""
            apiService!!.getDatos(sufijo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isSuccessful) {
                        val mPokemonModel = Gson().fromJson<PokemonModel>(result.body().toString(), PokemonModel::class.java)
                       // presenter.DatosPokemon(mPokemonModel!!.pokemon_entries)
                        if(mPokemonModel.pokemon_entries.isNotEmpty()){
                            Globales.pokemon_entries = mPokemonModel.pokemon_entries
                            presenter.irActivity(SelectPokemon::class.java)
                        }else{
                            presenter.showSnack("Esta pokedex no contiene pokémon")
                        }
                    } else {
                        presenter.showSnack("Error al obtener los datos")
                    }
                    presenter.showProgress(false)
                }, { error ->
                    presenter.showProgress(false)
                    println(error.toString())
                    presenter.showSnack("Error al obtener los datos")
                })
        } else {
            presenter.SnackConection("Comprueba tu conexión.")
            presenter.showProgress(false)
        }
    }

    //configuracion de menu footer
    override fun configureMenu(bottomNavigationBar:BottomNavigationView) {
        bottomNavigationBar.setOnNavigationItemSelectedListener(this)
        //remover animacion de menu footer
        bottomNavigationBar.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED

        //BottomNavigationViewHelper().disableShiftMode(navigation)
        val font = Typeface.createFromAsset(contexto!!.assets, "font/Roboto-Bold.ttf")
        val typefaceSpan = CustomTypefaceSpan("", font)
        for (i in 0 until bottomNavigationBar.menu.size()) {

            //fuentes de menu
            val menuItem = bottomNavigationBar.menu.getItem(i)
            val spannableTitle = SpannableStringBuilder(menuItem.title)
            spannableTitle.setSpan(typefaceSpan, 0, spannableTitle.length, 0)
            menuItem.title = spannableTitle
            //iconos de menu
            val menuView = bottomNavigationBar.getChildAt(0) as BottomNavigationMenuView
            val iconView = menuView.getChildAt(i).findViewById<View>(R.id.icon)
            val layoutParams = iconView.layoutParams
            val displayMetrics = contexto!!.resources.displayMetrics
            layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25f, displayMetrics).toInt()
            layoutParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25f, displayMetrics).toInt()
            iconView.layoutParams = layoutParams
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.bottomRegion ->{

                return true
            }
            /*R.id.bottomPokemon ->{

                return true
            }*/
            R.id.bottomEquipo ->{
                presenter.irActivity(Equipos::class.java)
                return true
            }
            R.id.bottomCerrar ->{
                firebaseAuth!!.signOut()
                LoginManager.getInstance().logOut()
                presenter.irActividad(Login::class.java)
                return true
            }
        }
        return false
    }
}