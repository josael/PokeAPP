package  com.elaniin.poke.android.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.elaniin.poke.android.Api.Globales
import com.elaniin.poke.android.R
import com.elaniin.poke.android.View.Home


class RegionAdapter(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    var Titulo: TextView = itemView.findViewById(R.id.Titulo)
    var logoMap: ImageView = itemView.findViewById(R.id.logoMap)

    //adapters
    class RecyclerRegionAdapter(context: Context, region: List<com.elaniin.poke.android.Model.Result>, globales: Globales?)
        : androidx.recyclerview.widget.RecyclerView.Adapter<RegionAdapter>() {

        //variables
        var contexto: Context = context
        var mInflater: LayoutInflater? = null
        val item: List<com.elaniin.poke.android.Model.Result>? = region
        var funciones = globales

        private val inflater: LayoutInflater = LayoutInflater.from(contexto)

        override fun getItemCount(): Int {
            return item!!.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionAdapter {
            val itemView = inflater.inflate(R.layout.item_list, parent, false)
            return RegionAdapter(itemView)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RegionAdapter, position: Int) {

            //tipos de fuentes
            holder.Titulo.typeface = Typeface.createFromAsset(contexto.assets, "font/Roboto-Bold.ttf")
            //seteo de datos
            holder.Titulo.text = item!![position].name.toLowerCase().trim().capitalize()
            //background
            funciones!!.fondo(contexto, R.drawable.map, holder.logoMap)

            holder.itemView.setOnClickListener {
                (contexto as Home).Pokedex(item[position].url)
            }
        }
    }
}


