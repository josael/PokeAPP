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
import com.elaniin.poke.android.Model.PokemonEntry
import com.elaniin.poke.android.R
import com.elaniin.poke.android.View.SelectPokemon


class PokemonAdapter(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    var Titulo: TextView = itemView.findViewById(R.id.Titulo)
    var ImgPoke: ImageView = itemView.findViewById(R.id.ImgPoke)
    var ImgCheck: ImageView = itemView.findViewById(R.id.ImgCheck)

    //adapters
    class RecyclerPokemonAdapter(context: Context, pokemon_entries: List<PokemonEntry>, globales: Globales?, selected: MutableList<Int>)
        : androidx.recyclerview.widget.RecyclerView.Adapter<PokemonAdapter>() {

        //variables
        var contexto: Context = context
        var mInflater: LayoutInflater? = null
        val item: List<PokemonEntry>? = pokemon_entries
        var funciones = globales
        var selectedPositions = selected

        private val inflater: LayoutInflater = LayoutInflater.from(contexto)

        override fun getItemCount(): Int {
            return item!!.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonAdapter {
            val itemView = inflater.inflate(R.layout.item_list_pokemon, parent, false)
            return PokemonAdapter(itemView)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: PokemonAdapter, position: Int) {

            //tipos de fuentes
            holder.Titulo.typeface = Typeface.createFromAsset(contexto.assets, "font/Roboto-Regular.ttf")
            //seteo de datos
            holder.Titulo.text = item!![position].pokemon_species.name.toLowerCase().trim().capitalize()

            //obtener imagen pokemon
            var urlPoke = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
            var imgPokemon = item[position].pokemon_species.url.replace("https://pokeapi.co/api/v2/pokemon-species/", "")
            imgPokemon = urlPoke+imgPokemon.replace("/", "")+".png"
            //background
            funciones!!.fondoURL(contexto,imgPokemon,holder.ImgPoke)

            actualizaUIXNoSave(item[position].entry_number,holder.ImgCheck)
            holder.itemView.setOnClickListener {
                actualizaUIX(item[position].entry_number,holder.ImgCheck,item[position].pokemon_species.url,imgPokemon,item[position].pokemon_species.name.toLowerCase().trim().capitalize())
            }


        }
        fun actualizaUIX(id: Int, check: ImageView,URL:String,imgPokemon:String,nombre:String){
            val selectedIndex = selectedPositions.indexOf(id)
            if (selectedIndex > -1) {
                (contexto as SelectPokemon).AgregarItem(id,false)
                check.visibility = View.GONE
                (contexto as SelectPokemon).DeletePoke(nombre)
            } else {
                //(contexto as SelectPokemon).AgregarItem(id,true)
                //check.visibility = View.VISIBLE
                //agregar pokemon
                (contexto as SelectPokemon).DatosPoke(URL,imgPokemon,id,true)
            }
        }
        fun actualizaUIXNoSave(id_sub_servicio: Int, check: ImageView){
            val selectedIndex = selectedPositions.indexOf(id_sub_servicio)
            if (selectedIndex > -1) {
                check.visibility = View.VISIBLE
            } else {
                check.visibility = View.GONE
            }
        }
    }
}


