package com.elaniin.poke.android.Interactors

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.elaniin.poke.android.Api.Globales
import com.elaniin.poke.android.Interfaces.apiGexRetrofit
import com.elaniin.poke.android.Model.EquipoModel
import com.elaniin.poke.android.Model.PokedexModel
import com.elaniin.poke.android.Model.Pokemo
import com.elaniin.poke.android.Model.PokemonModel
import com.elaniin.poke.android.R
import com.elaniin.poke.android.View.SelectPokemon
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.victordev.pokegroup.Interfaces.InterfaceGlobal
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import java.util.*


class EquipoInteractorImpl(present: InterfaceGlobal.EquipoPresenter) : InterfaceGlobal.EquipoInteractor{

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
    //variable de presentador
    private val presenter = present
    lateinit var equipoModel:EquipoModel

    override fun Componets(ctx: Context, func: Globales,mRetrofit: Retrofit) {
        funciones = func
        contexto = ctx
        retrofit = mRetrofit
        apiService = retrofit!!.create(apiGexRetrofit::class.java)
        //validar conexion
        conexion = funciones!!.isNetworkAvailable(contexto!!)

        //inicializar firebase Auth
        firebaseAuth = Globales.firebaseAuth
        //obtener instancia
        firebaseDatabase = Globales.firebaseDatabase
    }

    override fun CargarEquipos() {

        val dataBaseReference = firebaseDatabase!!.getReference("Equipos")
        //val id = dataBaseReference.push().key

        //obtener datos
        dataBaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var Listaequipo: ArrayList<EquipoModel> = ArrayList()
                    Listaequipo.clear()
                    for(data in dataSnapshot.children) {
                        //serealizar datos
                        val dataSnapshotValue = data.value as HashMap<*, *>
                        if(!dataSnapshotValue.toString().equals("null",true)){
                            val jsonString = Gson().toJson(dataSnapshotValue)
                            val mEquipoModel = Gson().fromJson<EquipoModel>(jsonString, EquipoModel::class.java)
                            //presenter.showSnack(mEquipoModel.nombre)
                            Listaequipo.add(mEquipoModel)
                        }
                    }
                    presenter.showFondo(false)
                    //llenas datos
                    presenter.Equipos(Listaequipo)

                }else{
                    presenter.showFondo(true)
                    presenter.showSnack("No tienes equipos creados")
                    presenter.showProgress(false)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
                presenter.showFondo(true)
                presenter.showSnack("Error")
                presenter.showProgress(false)
            }
        })
    }

    @SuppressLint("InflateParams")
    override fun showBottomSheetDialogPoke(bottom_sheet: View, pokemon:Pokemo) {

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
        Numero.typeface = Typeface.createFromAsset(contexto!!.assets, "font/Roboto-Bold.ttf")
        Descripcion.typeface = Typeface.createFromAsset(contexto!!.assets, "font/Roboto-Bold.ttf")

        //background
        funciones!!.fondoURL(contexto!!,pokemon.imagen,ImgPoke)
        //seteo de textos
        Nombre.text = pokemon.nombre
        Numero.text = pokemon.numero
        Tipo.text = pokemon.tipo
        Region.text = pokemon.region
        var descip = ""
        if(pokemon.descripcion.isNotEmpty()){
            Descripcion.text = pokemon.descripcion
            descip = pokemon.descripcion
        }else{
            textDescripcion.visibility = View.GONE
            Descripcion.visibility = View.GONE
        }
        btnAgregar.visibility = View.GONE
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

    @SuppressLint("InflateParams")
    override fun showBottomSheetDialogList(bottom_sheet: View, mEquipoModel:EquipoModel) {

        //vista BottomSheet
        val mBehavior = BottomSheetBehavior.from(bottom_sheet)

        if (mBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val inflater: LayoutInflater = LayoutInflater.from(contexto)
        val view = inflater.inflate(R.layout.sheet_list, null)

        //instancia a controles
        val btnShare: LinearLayout = view.findViewById(R.id.btnShare)
        val btnEditNombre: LinearLayout = view.findViewById(R.id.btnEditNombre)
        val btnEditPoke: LinearLayout = view.findViewById(R.id.btnEditPoke)
        val btnEliminar: LinearLayout = view.findViewById(R.id.btnEliminar)
        val Editt: LinearLayout = view.findViewById(R.id.Editt)
        val textCompatir: TextView = view.findViewById(R.id.textCompatir)
        val textEditarNombre: TextView = view.findViewById(R.id.textEditarNombre)
        val textEditar: TextView = view.findViewById(R.id.textEditar)
        val textEliminar: TextView = view.findViewById(R.id.textEliminar)
        val EdittNombre: EditText = view.findViewById(R.id.EdittNombre)
        val btnRenombrar: Button = view.findViewById(R.id.btnRenombrar)

        //fuentes
        textCompatir.typeface = Typeface.createFromAsset(contexto!!.assets, "font/helveticabold.ttf")
        textEditarNombre.typeface = Typeface.createFromAsset(contexto!!.assets, "font/helveticabold.ttf")
        textEditar.typeface = Typeface.createFromAsset(contexto!!.assets, "font/helveticabold.ttf")
        textEliminar.typeface = Typeface.createFromAsset(contexto!!.assets, "font/helveticabold.ttf")
        EdittNombre.typeface = Typeface.createFromAsset(contexto!!.assets, "font/Helvetica-Regular.ttf")
        btnRenombrar.typeface = Typeface.createFromAsset(contexto!!.assets, "font/helveticabold.ttf")

        btnShare.setOnClickListener {
            mBottomSheetDialog!!.dismiss()
            presenter.showSnack("Compatir Equipo")
        }

        btnEliminar.setOnClickListener {
            EditarBase("eliminar",mEquipoModel.id,"")
            mBottomSheetDialog!!.dismiss()
        }
        btnEditNombre.setOnClickListener {
            Editt.visibility = View.VISIBLE
        }
        btnRenombrar.setOnClickListener {
            //validar nombre de equipo
            if(EdittNombre.text.isNotEmpty()) {
                EditarBase("nombre", mEquipoModel.id, EdittNombre.text.toString())
                //quitar error
                EdittNombre.error = null
                mBottomSheetDialog!!.dismiss()
            }else{
                EdittNombre.error = "Escribe un nuevo nombre."
                //presenter.showSnack("Tienes que escribir un nombre para tu equipo.")
            }
        }
        btnEditPoke.setOnClickListener {
            equipoModel = mEquipoModel
            var url = Globales().decrypt(Globales().URL_API)+Globales().decrypt(funciones!!.SUFIJO_REGION)+mEquipoModel.idRegion+"/"
            presenter.Pokedex(url)
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

    fun EditarBase(editar: String,id: String,nombre: String){

        val dataBaseReference = firebaseDatabase!!.reference
        val dataBase = dataBaseReference.child("Equipos").orderByChild("id").equalTo(id)

        //obtener datos
        dataBase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(data in dataSnapshot.children) {
                        when(editar){
                            "eliminar"-> data.ref.removeValue()
                            "nombre"-> data.ref.child("nombre").setValue(nombre)
                        }
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
                presenter.showSnack("Error")
            }
        })
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
                            presenter.EditarEquipo(SelectPokemon::class.java,Gson().toJson(equipoModel),true)
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
}