package com.elaniin.poke.android.View

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.elaniin.poke.android.Adapters.PokemonAdapter
import com.elaniin.poke.android.Api.App
import com.elaniin.poke.android.Api.Globales
import com.elaniin.poke.android.Model.EquipoModel
import com.elaniin.poke.android.Model.Pokemo
import com.elaniin.poke.android.Model.PokemonDetalle
import com.elaniin.poke.android.Model.PokemonEntry
import com.elaniin.poke.android.Presenters.PokePresenterImpl
import com.elaniin.poke.android.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.victordev.pokegroup.Interfaces.InterfaceGlobal
import kotlinx.android.synthetic.main.activity_select_pokemon.*
import retrofit2.Retrofit
import java.util.*
import javax.inject.Inject


class SelectPokemon : AppCompatActivity(), InterfaceGlobal.PokemonView {

    //variables
    var contexto: Context? = null
    //varable de conexion
    var conexion: Boolean? = false
    @Inject
    lateinit var funciones: Globales
    @Inject
    lateinit var retrofit: Retrofit
    // Firebase Auth Object.
    var firebaseAuth: FirebaseAuth? = null
    //dialogo
    var dialog: Dialog? = null
    private var parent_view: View? = null
    //private var bottom_sheet: View? = null
    private var presenter: InterfaceGlobal.PokePresenter? = null

    //inizializar inject
    val app = App()

    var mBottomSheetDialog: BottomSheetDialog? = null

    var ImgPokemon:String? = null
    var ListaPokemon: ArrayList<Pokemo> = ArrayList()
    var idGrupo:String = ""

    var selectedPositions: MutableList<Int> = ArrayList()
    //adapter
    var adapter:PokemonAdapter.RecyclerPokemonAdapter? = null

    var editar: Boolean? = false
    var validar: Boolean? = false
    var JsonEquipo = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_pokemon)

        //inicializar datos
        contexto = this
        try {
            app.getNetComponent(Globales().decrypt(Globales().URL_API), contexto!!, this)!!.inject(this)
            //configurar dialogo
            configureDialog()
        } catch (e: Exception) {
            System.out.println("Error: $e")
        }
        //background
        funciones.fondo(contexto!!, R.drawable.pokemon_icon, logoPokeSelect)
        funciones.fondo(contexto!!, R.drawable.pokeball, ImgPokeball)

        //fuentes
        textSelect.typeface = Typeface.createFromAsset(assets, "font/Roboto-Bold.ttf")
        EdittEquipo.typeface = Typeface.createFromAsset(assets, "font/Roboto-Bold.ttf")
        textSelect.text = "Región "+Globales.region.toLowerCase().trim().capitalize()

        //inicializar presentador
        presenter = PokePresenterImpl(this)
        presenter!!.Componets(contexto!!,funciones,retrofit,EdittEquipo)
        //mostrar BottomSheet
        presenter!!.showBottomSheetDialog(bottom_sheet!!,getString(R.string.textEquipo),getString(R.string.textDetalle))

        //showBottomSheetDialog()
        DatosPokemon(Globales.pokemon_entries)

        //evento onclick
        FloatingGuardar.setOnClickListener {
            if (ListaPokemon.isNotEmpty()) {
                presenter!!.GuardarEquipo(ListaPokemon, idGrupo,validar!!)
            }
        }

        //se reciben datos por key segun se enviaron
        val extras = intent.extras
        if (extras != null) {
            editar = extras.getBoolean("Editar", false)
            JsonEquipo = extras.getString("Datos", "")

            if(editar!!){
                if(JsonEquipo.isNotEmpty()) {
                    val mEquipoModel = Gson().fromJson<EquipoModel>(JsonEquipo, EquipoModel::class.java)
                    EdittEquipo.setText(mEquipoModel.nombre)
                    for(pokemon in mEquipoModel.pokemon){
                        selectedPositions.add(pokemon.id.toInt())
                        val lista = Pokemo(
                            pokemon.id,
                            pokemon.numero,
                            pokemon.nombre,
                            pokemon.imagen,
                            pokemon.tipo,
                            pokemon.region,
                            pokemon.descripcion)
                        ListaPokemon.add(lista)

                    }
                    if (ListaPokemon.size >= 3) {
                        if (ListaPokemon.size <= 6) {
                            presenter!!.btnGuardar(true)
                        }
                    }else{
                        presenter!!.btnGuardar(false)
                    }
                    idGrupo = mEquipoModel.id
                    Globales.ListPokemon = ListaPokemon
                    AgregarPoke(ListaPokemon)
                }
                validar = true
            }
        }
    }

    fun DatosPokemon(pokemon_entries: List<PokemonEntry>) {
        if(pokemon_entries.isNotEmpty()){
            //LLenarRecycler(pokemon_entries)
            LLenarRecyclerGrid(pokemon_entries)
        }else{
            presenter!!.showSnack("Error al cargar datos.")
        }
    }

    //llenar lista
    @SuppressLint("WrongConstant")
    fun LLenarRecycler(pokemon_entries: List<PokemonEntry>) {
        ListPokemon.setHasFixedSize(true)
        ListPokemon.isNestedScrollingEnabled = false
        //reciclerview
        ListPokemon.adapter = null
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(contexto, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        ListPokemon.layoutManager = linearLayoutManager
        //adapter
        adapter = PokemonAdapter.RecyclerPokemonAdapter(contexto!!, pokemon_entries,funciones,selectedPositions)
        ListPokemon.adapter = adapter
        adapter!!.notifyDataSetChanged()
    }

    //llenar lista
    @SuppressLint("WrongConstant")
    fun LLenarRecyclerGrid(pokemon_entries: List<PokemonEntry>) {
        ListPokemon.setHasFixedSize(true)
        ListPokemon.isNestedScrollingEnabled = false
        // Layout Managers Grid
        ListPokemon.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 3)
        //adapter
        adapter = PokemonAdapter.RecyclerPokemonAdapter(contexto!!, pokemon_entries,funciones,selectedPositions)
        ListPokemon.adapter = adapter
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
    override fun navigateToActivity(act: Class<*>) {
        irActivity(act)
    }

    override fun showSnack(text:String){
        parent_view = findViewById(android.R.id.content)
        funciones.SnackbarCustom(parent_view!!, text,contexto!!)
    }

    override fun SnackConection(text:String){
        parent_view = findViewById(android.R.id.content)
        funciones.SnackbarConection(parent_view!!, text,contexto!!)
    }


    override fun irActivity(act: Class<*>) {
        val intent = Intent(applicationContext, act)
        intent.action = Intent.ACTION_MAIN
        startActivity(intent)
        finish()
    }

    fun DatosPoke(URL:String,imgPokemon:String,position:Int, eliminar:Boolean){
        app.getNetComponent(URL, contexto!!, this)!!.inject(this)
        presenter!!.Componets(contexto!!,funciones,retrofit,EdittEquipo)
        ImgPokemon = imgPokemon
        presenter!!.GetPokemonDetalle(position,eliminar)
    }

    override fun PokemonDetalle(mPokemonDetalle: PokemonDetalle,position:Int, eliminar:Boolean) {
        if(mPokemonDetalle.name.isNotEmpty()){
            //mostrar BottomSheet
            presenter!!.showBottomSheetDialogPoke(bottom_sheet!!,mPokemonDetalle,ImgPokemon!!,position,eliminar)
        }
    }

    override fun AgregarItem(position:Int, eliminar:Boolean){
        if(eliminar){
            selectedPositions.add(position)
        }else{
            selectedPositions.remove(position)
        }
        //notify adapter
        adapter!!.notifyDataSetChanged()
        //DatosPokemon(Globales.pokemon_entries)
        //ListPokemon.layoutManager!!.scrollToPosition(posi)
    }

    fun DeletePoke(nombre:String){
        if(ListaPokemon.isNotEmpty()){
            for (i in 0 until ListaPokemon.size) {
                if (nombre.equals(ListaPokemon[i].nombre,true)) {
                    ListaPokemon.removeAt(i)
                    break
                }
            }
        }
        if (ListaPokemon.size >= 3) {
            if (ListaPokemon.size <= 6) {
                presenter!!.btnGuardar(true)
            }
        }else{
            presenter!!.btnGuardar(false)
        }
    }

    override fun AgregarPoke(ListPokemon: ArrayList<Pokemo>) {
        ListaPokemon = ListPokemon
    }
    @SuppressLint("RestrictedApi")
    override fun btnGuardar(boolean: Boolean) {
        if (boolean) {
            FloatingGuardar.visibility = View.VISIBLE
        } else {
            FloatingGuardar.visibility = View.GONE
        }
    }
}
