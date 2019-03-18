package com.elaniin.poke.android.Interfaces

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


 interface apiGexRetrofit {
    //llamar con varios parametros en get
    @GET("{sufijourl}")
    fun getDatos(
            @Path("sufijourl") sufijourl: String
    ): Observable<Response<JsonObject>>

    //application/json  POST
    //@Headers("Content-Type: application/json")
    @POST("{sufijo}")
    fun DatoPOST(
            @Path("sufijo", encoded = true) sufijourl: String,
            @QueryMap options: Map<String, String>,
            @Body body: RequestBody): Observable<Response<JsonObject>>

    //application/json  POST
    //@Headers("Content-Type: application/json")
    @POST("{sufijo}")
    fun DatoHeaderPOST(
            @HeaderMap headers: Map<String, String>,
            @Path("sufijo", encoded = true) sufijourl: String,
            @QueryMap options: Map<String, String>,
            @Body body: RequestBody): Observable<Response<JsonObject>>

}