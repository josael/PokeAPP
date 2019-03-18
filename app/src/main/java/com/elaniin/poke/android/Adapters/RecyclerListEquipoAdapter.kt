package com.elaniin.poke.android.Adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.elaniin.poke.android.Api.Globales
import com.elaniin.poke.android.Model.EquipoModel
import com.elaniin.poke.android.R
import com.elaniin.poke.android.View.Equipos
import java.util.*


class RecyclerListEquipoAdapter(context: Context, item: ArrayList<EquipoModel>, globales: Globales?)
    : androidx.recyclerview.widget.RecyclerView.Adapter<RecyclerListEquipoAdapter.ItemRowHolder>() {

    //variables
    var context: Context = context
    var data = item
    var funciones = globales

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRowHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_equipos, null)
        return ItemRowHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ItemRowHolder, position: Int) {
        //llenar de datos
        val items = data[position]
        val nombre = items.nombre

        var itemListDataAdapter = SeccionListEquipoAdapter(context, items.pokemon,funciones)
        holder.ListPoke.setHasFixedSize(true)
        holder.ListPoke.isNestedScrollingEnabled = false
        holder.ListPoke.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        holder.ListPoke.adapter = itemListDataAdapter

        //Fuentes
        holder.itemTitle.typeface = Typeface.createFromAsset(context.assets, "font/helveticabold.ttf")
        holder.itemTitle.text = nombre

        holder.btnMore.setOnClickListener {
            (context as Equipos).DetalleEquipo(items)
        }

    }

    inner class ItemRowHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        var itemTitle: TextView = view.findViewById(R.id.itemTitle)
        var btnMore: Button = view.findViewById(R.id.btnMore)
        var ListPoke: androidx.recyclerview.widget.RecyclerView = view.findViewById(R.id.ListPoke)
       // var btnMore: Button = view.findViewById(R.id.btnMore) as Button
    }

}