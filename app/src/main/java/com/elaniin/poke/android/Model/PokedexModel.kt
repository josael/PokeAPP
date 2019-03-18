package com.elaniin.poke.android.Model

data class PokedexModel(
    val id: Int,
    val locations: List<Location>,
    val main_generation: MainGeneration,
    val name: String,
    val names: List<Name>,
    val pokedexes: List<Pokedexe>,
    val version_groups: List<VersionGroup>
)


data class Location(
    val name: String,
    val url: String
)

data class MainGeneration(
    val name: String,
    val url: String
)

data class Pokedexe(
    val name: String,
    val url: String
)
