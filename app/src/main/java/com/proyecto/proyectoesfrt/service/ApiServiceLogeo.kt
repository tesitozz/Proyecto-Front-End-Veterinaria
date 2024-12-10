package com.proyecto.proyectoesfrt.service

import com.proyecto.proyectoesfrt.entidad.LoginRequest
import com.proyecto.proyectoesfrt.entidad.Usuario
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServiceLogeo {


    // Método para iniciar sesión
    @POST("/api/administradores/login")
    fun login(@Body loginRequest: LoginRequest): retrofit2.Call<Usuario>
}