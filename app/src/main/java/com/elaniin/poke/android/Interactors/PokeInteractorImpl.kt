package com.elaniin.poke.android.Interactors

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.elaniin.poke.android.Api.Globales
import com.elaniin.poke.android.Interfaces.apiGexRetrofit
import com.elaniin.poke.android.Model.EquipoModel
import com.elaniin.poke.android.Model.Pokemo
import com.elaniin.poke.android.Model.PokemonDetalle
import com.elaniin.poke.android.R
import com.elaniin.poke.android.View.Equipos
import com.elaniin.poke.android.View.SelectPokemon
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.victordev.pokegroup.Interfaces.InterfaceGlobal
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import java.util.*

class PokeInteractorImpl(present: InterfaceGlobal.PokePresenter) : InterfaceGlobal.PokeInteractor{

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
    // Firebase Database
    var firebaseDatabase: FirebaseDatabase? = null
    var mBottomSheetDialog: BottomSheetDialog? = null
    var Activity: SelectPokemon? = null
    var NombreEquipo:EditText? = null

    //variable de presentador
    private val presenter = present

    var ListPokemon: ArrayList<Pokemo> = ArrayList()

    override fun Componets(ctx: Context, func: Globales,mRetrofit: Retrofit,EdittEquipo: EditText) {
        funciones = func
        contexto = ctx
        retrofit = mRetrofit
        NombreEquipo = EdittEquipo
        apiService = retrofit!!.create(apiGexRetrofit::class.java)
        //validar conexion
        conexion = funciones!!.isNetworkAvailable(contexto!!)

        //obtener instancia
        firebaseDatabase = Globales.firebaseDatabase

        ListPokemon = Globales.ListPokemon
    }

    @SuppressLint("InflateParams")
    override fun showBottomSheetDialog(bottom_sheet: View,titulo:String,detalle:String) {

        //vista BottomSheet
        val mBehavior = BottomSheetBehavior.from(bottom_sheet)

        if (mBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val inflater: LayoutInflater = LayoutInflater.from(contexto)
        val view = inflater.inflate(R.layout.sheet_basic, null)

        //instancia a controles
        val textTitulo: TextView = view.findViewById(R.id.textTitulo)
        val textDetalle: TextView = view.findViewById(R.id.textDetalle)
        val close: Button = view.findViewById(R.id.bt_close)
         //fuentes
        textTitulo.typeface = Typeface.createFromAsset(contexto!!.assets, "font/Roboto-Bold.ttf")
        textDetalle.typeface = Typeface.createFromAsset(contexto!!.assets, "font/Roboto-Regular.ttf")
        //seteo de textos
        textTitulo.text = titulo
        textDetalle.text = detalle

        close.setOnClickListener{
            mBottomSheetDialog!!.dismiss()
        }

        mBottomSheetDialog = BottomSheetDialog(contexto!!)
        mBottomSheetDialog!!.setContentView(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        mBottomSheetDialog!!.show()
        mBottomSheetDialog!!.setOnDismissListener { mBottomSheetDialog = null }
    }

    @SuppressLint("CheckResult")
    override fun GetPokemonDetalle(position:Int, eliminar:Boolean) {
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
                        val mPokemonDetalle = Gson().fromJson<PokemonDetalle>(result.body().toString(), PokemonDetalle::class.java)
                        presenter.PokemonDetalle(mPokemonDetalle,position,eliminar)
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

    @SuppressLint("InflateParams")
    override fun showBottomSheetDialogPoke(bottom_sheet: View,mPokemonDetalle:PokemonDetalle,imgPokemon:String,position:Int, eliminar:Boolean) {

        //vista BottomSheet
        val mBehavior = BottomSheetBehavior.from(bottom_sheet)

        if (mBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val inflater: LayoutInflater = LayoutInflater.from(contexto)
        val view = inflater.inflate(R.layout.pokemon_detalle, null)

        //instancia a controles
        val ImgPoke: ImageView = view.findViewById(R.id.ImgPoke)
        val Nombre: TextView = view.findViewById(R.id.Nombre)
        val Numero: TextView = view.findViewById(R.id.Numero)
        val Tipo: TextView = view.findViewById(R.id.Tipo)
        val Region: TextView = view.findViewById(R.id.Region)
        val Descripcion: TextView = view.findViewById(R.id.Descripcion)
        val textDescripcion: TextView = view.findViewById(R.id.textDescripcion)
        val btnAgregar: Button = view.findViewById(R.id.btnAgregar)
        val close: Button = view.findViewById(R.id.bt_close)
        //fuentes
        Nombre.typeface = Typeface.createFromAsset(contexto!!.assets, "font/Roboto-Bold.ttf")
        Numero.typeface = Typeface.createFromAsset(contexto!!.assets, "font/Roboto-Bold.ttf")
        Tipo.typeface = Typeface.createFromAsset(contexto!!.assets, "font/Roboto-Bold.ttf")
        Region.typeface = Typeface.createFromAsset(contexto!!.assets, "font/Roboto-Bold.ttf")
        Descripcion.typeface = Typeface.createFromAsset(contexto!!.assets, "font/Roboto-Bold.ttf")

        //background
        funciones!!.fondoURL(contexto!!,imgPokemon,ImgPoke)
        //seteo de textos
        Nombre.text = mPokemonDetalle.name.toLowerCase().trim().capitalize()
        Numero.text = mPokemonDetalle.id.toString()
        Tipo.text = mPokemonDetalle.egg_groups[0].name.toLowerCase().trim().capitalize()
        Region.text = Globales.region.toLowerCase().trim().capitalize()
        var descip = ""
        if(mPokemonDetalle.form_descriptions.isNotEmpty()){
            Descripcion.text = mPokemonDetalle.form_descriptions[0].toString()
            descip = mPokemonDetalle.form_descriptions[0].toString()
        }else{
            textDescripcion.visibility = View.GONE
            Descripcion.visibility = View.GONE
        }
        btnAgregar.setOnClickListener{

            val lista = Pokemo(
                position.toString(),
                mPokemonDetalle.id.toString(),
                mPokemonDetalle.name.toLowerCase().trim().capitalize(),
                imgPokemon,
                mPokemonDetalle.egg_groups[0].name.toLowerCase().trim().capitalize(),
                Globales.region.toLowerCase().trim().capitalize(),
                descip)
            ListPokemon.add(lista)

            if (ListPokemon.size >= 3) {
                if (ListPokemon.size <= 6) {
                    presenter.btnGuardar(true)
                }
            }else{
                presenter.btnGuardar(false)
            }

            presenter.AgregarPoke(ListPokemon)
            presenter.AgregarItem(position,eliminar)
            mBottomSheetDialog!!.dismiss()
        }
        close.setOnClickListener{
            mBottomSheetDialog!!.dismiss()
        }

        mBottomSheetDialog = BottomSheetDialog(contexto!!)
        mBottomSheetDialog!!.setContentView(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        mBottomSheetDialog!!.show()
        mBottomSheetDialog!!.setOnDismissListener { mBottomSheetDialog = null }
    }

    override fun GuardarEquipo(ListPokemon: ArrayList<Pokemo>,numero:String,validar:Boolean) {
        if (ListPokemon.size >= 3) {
            if (ListPokemon.size <= 6) {
                //validar nombre de equipo
                if(NombreEquipo!!.text.isNotEmpty()) {
                    val dataBaseReference = firebaseDatabase!!.reference
                    if(validar){
                        //editar
                        val equipo = EquipoModel(numero, Globales.idRegion, NombreEquipo!!.text.toString(), ListPokemon)
                        dataBaseReference.child("Equipos").child(numero).setValue(equipo)
                        presenter.showSnack("Equipo Editado")
                    }else{
                        //crear
                        val id = dataBaseReference.push().key

                        val equipo = EquipoModel(id!!, Globales.idRegion, NombreEquipo!!.text.toString(), ListPokemon)
                        dataBaseReference.child("Equipos").child(id).setValue(equipo)
                        presenter.showSnack("Equipo Agregado")

                    }
                    presenter.irActivity(Equipos::class.java)
                    ListPokemon.clear()
                    Globales.ListPokemon.clear()
                }else{
                    presenter.showSnack("Tienes que escribir un nombre para tu equipo.")
                }
            }else{
                presenter.showSnack("El maximo de pokémon para tu equipo es de 6.")
            }
        }else{
            presenter.showSnack("El mínimo de pokémon para tu equipo es de 3.")
        }
    }
}