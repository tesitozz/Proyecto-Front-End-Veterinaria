package com.proyecto.proyectoesfrt.service

import com.proyecto.proyectoesfrt.entidad.Cliente
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiServiceCliente {

    @GET("api/clientes/listar")
    suspend fun getAllClientes(): Response<List<Cliente>>

    @POST("api/clientes/insertar")
    suspend fun insertarCliente(@Body cliente: Cliente): Response<Cliente>

    @PUT("api/clientes/actualizar/{id}")
    suspend fun actualizarCliente(@Path("id") id: Int, @Body cliente: Cliente): Response<Cliente>

    @DELETE("api/clientes/eliminar/{id}")
    suspend fun eliminarCliente(@Path("id") id: Int): Response<Void>
}