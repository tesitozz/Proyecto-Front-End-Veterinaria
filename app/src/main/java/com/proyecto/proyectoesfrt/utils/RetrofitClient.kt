package com.proyecto.proyectoesfrt.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.proyecto.proyectoesfrt.service.ApiServiceAnimal
import com.proyecto.proyectoesfrt.service.ApiServiceCliente
import com.proyecto.proyectoesfrt.service.ApiServiceMedicos
import com.proyecto.proyectoesfrt.service.ApiServiceLogeo

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/" // URL base para tu API

    // Instancia de Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Servicio para animales
    val apiService: ApiServiceAnimal by lazy {
        retrofit.create(ApiServiceAnimal::class.java)
    }

    // Servicio para clientes
    val apiServiceCliente: ApiServiceCliente by lazy {
        retrofit.create(ApiServiceCliente::class.java)
    }

    // Servicio para clientes
    val apiServiceDoctor: ApiServiceMedicos by lazy {
        retrofit.create(ApiServiceMedicos::class.java)
    }

    // Servicio para Logeo
    val apiLogeoUsuario: ApiServiceLogeo by lazy {
        retrofit.create(ApiServiceLogeo::class.java)
    }
}
