package com.elaniin.poke.android.Model

data class RegionModel(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<Result>
)

data class Result(
    val name: String,
    val url: String
)