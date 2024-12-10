package com.proyecto.proyectoesfrt.service

import com.proyecto.proyectoesfrt.entidad.Animales
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiServiceAnimal {

    @GET("api/animales/listar")
    suspend fun getAllAnimales(): Response<List<Animales>>

    @POST("api/animales/insertar")
    suspend fun insertarAnimal(@Body animal: Animales): Response<Animales>

    @PUT("api/animales/actualizar/{id}")
    suspend fun actualizarAnimal(@Path("id") id: Int, @Body animal: Animales): Response<Animales>

    @DELETE("api/animales/eliminar/{id}")
    suspend fun eliminarAnimal(@Path("id") id: Int): Response<Void>
}