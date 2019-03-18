package com.elaniin.poke.android.Adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.elaniin.poke.android.Api.Globales
import com.elaniin.poke.android.Model.Pokemo
import com.elaniin.poke.android.R
import com.elaniin.poke.android.View.Equipos


class SeccionListEquipoAdapter(context: Context, item: List<Pokemo>, globales: Globales?)
    : androidx.recyclerview.widget.RecyclerView.Adapter<SeccionListEquipoAdapter.ItemRowHolder>() {

    //variables
    var context: Context = context
    var data = item
    var funciones = globales

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRowHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, null)
        return ItemRowHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ItemRowHolder, position: Int) {
        //val sectionName = allSampleData!![position].headerTitle
        val items = data[position]
        val nombre = items.nombre.toLowerCase().trim().capitalize()

        //Fuentes
        holder.itemTitle.typeface = Typeface.createFromAsset(context.assets, "font/helveticabold.ttf")
        holder.itemTitle.text = nombre
        //background
        funciones!!.fondoURL(context,items.imagen,holder.ImgPoke)

        holder.itemView.setOnClickListener {
            (context as Equipos).PokemonDetalle(items)
        }

    }

    inner class ItemRowHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        var itemTitle: TextView = view.findViewById(R.id.Titulo)
        var ImgPoke: ImageView =  view.findViewById(R.id.ImgPoke)
    }

}