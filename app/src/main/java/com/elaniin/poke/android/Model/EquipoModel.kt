package com.elaniin.poke.android.Model

data class EquipoModel(
    val id: String,
    val idRegion: String,
    val nombre: String,
    val pokemon: List<Pokemo>
)

data class Pokemo(
    val id: String,
    val numero: String,
    val nombre: String,
    val imagen: String,
    val tipo: String,
    val region: String,
    val descripcion: String
)
