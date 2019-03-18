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
import com.elaniin.poke.android.Adapters.RegionAdapter
import com.elaniin.poke.android.Api.App
import com.elaniin.poke.android.Api.Globales
import com.elaniin.poke.android.Model.Pokedexe
import com.elaniin.poke.android.Model.Result
import com.elaniin.poke.android.Presenters.HomePresenterImpl
import com.elaniin.poke.android.R
import com.google.firebase.auth.FirebaseAuth
import com.victordev.pokegroup.Interfaces.InterfaceGlobal
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Retrofit
import javax.inject.Inject

class Home : AppCompatActivity(), InterfaceGlobal.HomeView {

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

    private var presenter:InterfaceGlobal.HomePresenter? = null

    //inizializar inject
    val app = App()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
        funciones.fondo(contexto!!, R.drawable.pokemon_icon, logoPokeHome)
        funciones.fondo(contexto!!, R.drawable.map, logoMap)

        //fuentes
        textRegion.typeface = Typeface.createFromAsset(assets, "font/Roboto-Bold.ttf")

        //inicializar presentador
        presenter = HomePresenterImpl(this)
        presenter!!.Componets(contexto!!,funciones,retrofit)
        presenter!!.GetRegions()

        //inicializar firebase Auth
        firebaseAuth = Globales.firebaseAuth

        //Barra de navegacion inferior
        presenter!!.configureMenu(bottomNavigationBar)

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

    override fun showProgress(boolean: Boolean) {
        if (boolean) {
            dialog!!.show()
        } else {
            dialog!!.dismiss()
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

    override fun DatosRegion(mRegionModel: List<Result>) {
        if(mRegionModel.isNotEmpty()){
            LLenarRecycler(mRegionModel)
        }
    }

    //llenar lista
    @SuppressLint("WrongConstant")
    fun LLenarRecycler(mRegionModel: List<Result>) {
        //reciclerview
        ListRegion.adapter = null
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(contexto, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        ListRegion.layoutManager = linearLayoutManager
        //adapter
        val adapter = RegionAdapter.RecyclerRegionAdapter(contexto!!, mRegionModel,funciones)
        ListRegion.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun Pokedex(URL:String){
        app.getNetComponent(URL, contexto!!, this)!!.inject(this)
        presenter!!.Componets(contexto!!,funciones,retrofit)
        presenter!!.GetPokedex()
    }

    override fun DatosPokedex(pokedexes: List<Pokedexe>) {
        if(pokedexes.isNotEmpty()){
            app.getNetComponent(pokedexes[0].url, contexto!!, this)!!.inject(this)
            presenter!!.Componets(contexto!!,funciones,retrofit)
            presenter!!.GetPokemon()
        }else{
            presenter!!.showSnack("Esta region no contiene pokémon")
        }
    }


    override fun onBackPressed() {
        //super.onBackPressed();
        //mandar a segundo plano
        moveTaskToBack(true)
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationBar.selectedItemId = R.id.bottomRegion
    }
}
