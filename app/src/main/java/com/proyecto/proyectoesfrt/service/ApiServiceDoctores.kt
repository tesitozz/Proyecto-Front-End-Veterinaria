package com.proyecto.proyectoesfrt.service

import com.proyecto.proyectoesfrt.entidad.Doctor
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiServiceDoctores {

    @GET("api/doctores/listar")
    suspend fun getAllDoctores(): Response<List<Doctor>>

    @POST("api/doctores/insertar")
    suspend fun insertarDoctor(@Body doctor: Doctor): Response<Doctor>

    @PUT("api/doctores/actualizar/{id}")
    suspend fun actualizarDoctor(@Path("id") id: Int, @Body doctor: Doctor): Response<Doctor>

    @DELETE("api/doctores/eliminar/{id}")
    suspend fun eliminarDoctor(@Path("id") id: Int): Response<Void>
}