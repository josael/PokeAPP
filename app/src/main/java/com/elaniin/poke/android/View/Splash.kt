package com.elaniin.poke.android.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.elaniin.poke.android.Api.App
import com.elaniin.poke.android.Api.Globales
import com.elaniin.poke.android.R
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

class Splash : AppCompatActivity(){
    //variables
    var contexto: Context? = null
    internal val SPLASH_TIME_OUT = 1000

    //varable de conexion
    var conexion: Boolean? = false
    @Inject
    lateinit var funciones: Globales

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //inicializar datos
        contexto = this
        try {
            //inizializar inject
            val app = App()
            app.getNetComponent("", contexto!!, this)!!.inject(this)
        } catch (e: Exception) {
            System.out.println("Error: $e")
        }
        //fondologin
        funciones.fondo(contexto!!, R.drawable.logo, logoSplash)

        //fuente
        version.typeface = Typeface.createFromAsset(assets, "font/Roboto-Bold.ttf")
        //version App
        version.text = "V.${funciones.getVersionApp(contexto!!)}"

        //iniciar timer
        CargarLogin()
    }

  fun CargarLogin() {
        //timer de splah
        val timer = object : CountDownTimer(SPLASH_TIME_OUT.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                //IrActividad
                startActivity(Intent(contexto, Login::class.java))
            }
        }.start()

    }
}
