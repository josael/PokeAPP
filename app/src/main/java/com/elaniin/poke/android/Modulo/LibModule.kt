package com.elaniin.poke.android.Modulo

import android.app.Activity
import android.content.Context
import com.elaniin.poke.android.Api.AppScope
import com.elaniin.poke.android.Api.Globales
import com.elaniin.poke.android.BuildConfig
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
class LibModule(internal var mBaseUrl: String, internal var mActivity: Context, var app: Activity) {

    //Gson
    @Provides
    @AppScope
    internal fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    //cliente okhttp
    @Provides
    @AppScope
    internal fun provideOkhttpClient(): OkHttpClient {
        try {
            var loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            var builder: OkHttpClient.Builder = OkHttpClient.Builder()
            builder.addInterceptor(loggingInterceptor)
            builder.connectTimeout(300,TimeUnit.SECONDS)
            builder.readTimeout(80,TimeUnit.SECONDS)
            builder.writeTimeout(90,TimeUnit.SECONDS)
            builder.retryOnConnectionFailure(true)
            builder.addNetworkInterceptor(Interceptor {
                val request: Request = it.request().newBuilder().addHeader("Connection", "close").build()
                return@Interceptor it.proceed(request)
            })
            if (BuildConfig.DEBUG) {
                builder.addInterceptor(OkHttpProfilerInterceptor())
            }
            return builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    //retrofit
    @Provides
    @AppScope
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build()
    }

    //globales
    @Provides
    @AppScope
    internal fun provideGlobales(): Globales {
        var funciones: Globales? = null
        funciones = Globales()
        return funciones
    }

}