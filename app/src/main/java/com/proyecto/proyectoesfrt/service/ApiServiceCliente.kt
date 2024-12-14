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
    @GET("/api/clientes/listar")
    suspend fun getAllClientes(): Response<List<Cliente>>

    @POST("/api/clientes/crear")
    suspend fun createCliente(@Body cliente: Cliente): Response<Cliente>

    @PUT("/api/clientes/actualizar")
    suspend fun updateCliente(@Body cliente: Cliente): Response<Cliente>

    @PUT("/api/clientes/actualizar-nombres-de/{id}")
    suspend fun updateClienteNombres(@Path("id") id: Long, @Body nombres: String): Response<Cliente>

    @PUT("/api/clientes/actualizar-apellidos-de/{id}")
    suspend fun updateClienteApellidos(@Path("id") id: Long, @Body apellidos: String): Response<Cliente>

    @PUT("/api/clientes/actualizar-dni-de/{id}")
    suspend fun updateClienteDni(@Path("id") id: Long, @Body dni: String): Response<Cliente>

    @PUT("/api/clientes/actualizar-genero-de/{id}")
    suspend fun updateClienteGenero(@Path("id") id: Long, @Body genero: String): Response<Cliente>

    @PUT("/api/clientes/actualizar-correo-de/{id}")
    suspend fun updateClienteCorreo(@Path("id") id: Long, @Body correo: String): Response<Cliente>

    @PUT("/api/clientes/actualizar-celular-de/{id}")
    suspend fun updateClienteCelular(@Path("id") id: Long, @Body celular: String): Response<Cliente>

    @PUT("/api/clientes/actualizar-direccion-de/{id}")
    suspend fun updateClienteDireccion(@Path("id") id: Long, @Body direccion: String): Response<Cliente>

    @GET("/api/clientes/buscar-por-id/{id}")
    suspend fun getClienteById(@Path("id") id: Long): Response<Cliente>

    @DELETE("/api/clientes/eliminar-por-id/{id}")
    suspend fun deleteCliente(@Path("id") id: Long): Response<Void>

    @GET("/api/clientes/buscar-por-dni/{dni}")
    suspend fun getClienteByDni(@Path("dni") dni: String): Response<Cliente>

    @GET("/api/clientes/buscar-por-correo/{correo}")
    suspend fun getClienteByCorreo(@Path("correo") correo: String): Response<Cliente>

    @GET("/api/clientes/buscar-por-nombres/{nombres}")
    suspend fun getClientesByNombres(@Path("nombres") nombres: String): Response<List<Cliente>>

    @GET("/api/clientes/buscar-por-apellidos/{apellidos}")
    suspend fun getClientesByApellidos(@Path("apellidos") apellidos: String): Response<List<Cliente>>

    @GET("/api/clientes/buscar-por-genero/{genero}")
    suspend fun getClientesByGenero(@Path("genero") genero: String): Response<List<Cliente>>

    @GET("/api/clientes/buscar-por-direccion/{direccion}")
    suspend fun getClientesByDireccion(@Path("direccion") direccion: String): Response<List<Cliente>>

    @GET("/api/clientes/buscar-por-celular/{celular}")
    suspend fun getClientesByCelular(@Path("celular") celular: String): Response<List<Cliente>>

}