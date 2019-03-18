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
import com.elaniin.poke.android.Adapters.RecyclerListEquipoAdapter
import com.elaniin.poke.android.Api.App
import com.elaniin.poke.android.Api.Globales
import com.elaniin.poke.android.Model.EquipoModel
import com.elaniin.poke.android.Model.Pokedexe
import com.elaniin.poke.android.Model.Pokemo
import com.elaniin.poke.android.Presenters.EquipoPresenterImpl
import com.elaniin.poke.android.R
import com.google.firebase.auth.FirebaseAuth
import com.victordev.pokegroup.Interfaces.InterfaceGlobal
import kotlinx.android.synthetic.main.activity_equipos.*
import retrofit2.Retrofit
import java.util.*
import javax.inject.Inject

class Equipos : AppCompatActivity(), InterfaceGlobal.EquipoView {

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

    private var presenter: InterfaceGlobal.EquipoPresenter? = null

    //inizializar inject
    val app = App()
    //adapter
    var adapter:RecyclerListEquipoAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipos)
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
        funciones.fondo(contexto!!, R.drawable.pokemon_icon, logoPokeEquipo)
        funciones.fondo(contexto!!, R.drawable.pokeballs, ImgPokeEquipos)
        funciones.fondo(contexto!!, R.drawable.gengar, ImgFondo)

        //fuentes
        textEquipos.typeface = Typeface.createFromAsset(assets, "font/Roboto-Bold.ttf")

        //inicializar presentador
        presenter = EquipoPresenterImpl(this)
        presenter!!.Componets(contexto!!,funciones,retrofit)
        presenter!!.CargarEquipos()
        presenter!!.showProgress(true)

    }

    override fun Equipos(mEquipoModel: ArrayList<EquipoModel>) {
        if(mEquipoModel.isNotEmpty()) {
            LLenarRecycler(mEquipoModel)
            presenter!!.showProgress(false)
        }else{
            presenter!!.showSnack("No tienes equipos pokémon")
            presenter!!.showProgress(false)
        }
    }

    //llenar lista Equipos
    @SuppressLint("WrongConstant")
    fun LLenarRecycler(mEquipoModel: ArrayList<EquipoModel>) {
        ListEquipos.setHasFixedSize(true)
        ListEquipos.isNestedScrollingEnabled = false
        //reciclerview
        ListEquipos.adapter = null
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(contexto, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        ListEquipos.layoutManager = linearLayoutManager
        //adapter
        adapter = RecyclerListEquipoAdapter(contexto!!, mEquipoModel,funciones)
        ListEquipos.adapter = adapter
        adapter!!.notifyDataSetChanged()
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
    override fun showFondo(cargador: Boolean) {
        if (cargador) {
            ImgFondo.visibility = View.VISIBLE
        } else {
            ImgFondo.visibility = View.GONE
        }
    }
    override fun navigateToActivity(act: Class<*>) {
        irActividad(act)
    }

    override fun showSnack(text:String){
        parent_view = findViewById(android.R.id.content)
        funciones.SnackbarCustom(parent_view!!, text,contexto!!)
    }

    override fun SnackConection(text:String){
        parent_view = findViewById(android.R.id.content)
        funciones.SnackbarConection(parent_view!!, text,contexto!!)
    }

    override fun irActividad(act: Class<*>) {
        val intent = Intent(applicationContext, act)
        intent.action = Intent.ACTION_MAIN
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun irActivity(act: Class<*>) {
        val intent = Intent(applicationContext, act)
        intent.action = Intent.ACTION_MAIN
        startActivity(intent)
        finish()
    }

    override fun EditarEquipo(act: Class<*>,datos:String,boolean:Boolean) {
        val intent = Intent(applicationContext, act)
        intent.action = Intent.ACTION_MAIN
        intent.putExtra("Datos",datos)
        intent.putExtra("Editar",boolean)
        startActivity(intent)
        finish()
    }

    override fun PokemonDetalle(pokemon:Pokemo) {
        if(pokemon.nombre.isNotEmpty()){
            //mostrar BottomSheet
            presenter!!.showBottomSheetDialogPoke(bottomEquipo,pokemon)
        }
    }
    override fun DetalleEquipo(mEquipoModel:EquipoModel) {
        if(mEquipoModel.nombre.isNotEmpty()){
            //mostrar BottomSheet
            presenter!!.showBottomSheetDialogList(bottomEquipo,mEquipoModel)
        }
    }

    override fun Pokedex(URL:String){
        app.getNetComponent(URL, contexto!!, this)!!.inject(this)
        presenter!!.Componets(contexto!!,funciones,retrofit)
        presenter!!.GetPokedex()
    }

    override fun DatosPokedex(pokedexes: List<Pokedexe>) {
        if(pokedexes.isNotEmpty()){
            app.getNetComponent(pokedexes[0].url, contexto!!, this)!!.inject(this)
            presenter!!.Componets(contexto!!,funciones,retrofit)
            presenter!!.GetPokemon()
        }
    }
}
