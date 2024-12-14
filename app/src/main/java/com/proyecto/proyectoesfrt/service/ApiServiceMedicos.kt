package com.proyecto.proyectoesfrt.service

import com.proyecto.proyectoesfrt.entidad.Medico
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiServiceMedicos {

    @GET("/api/medicos/listar")
    suspend fun getAllMedicos(): Response<List<Medico>>

    @POST("/api/medicos/crear")
    suspend fun createMedico(@Body medico: Medico): Response<Medico>

    @PUT("/api/medicos/actualizar")
    suspend fun updateMedico(@Body medico: Medico): Response<Medico>

    @PUT("/api/medicos/actualizar-nombres-de/{id}")
    suspend fun updateMedicoNombres(@Path("id") id: Long, @Body nombres: String): Response<Medico>

    @PUT("/api/medicos/actualizar-apellidos-de/{id}")
    suspend fun updateMedicoApellidos(@Path("id") id: Long, @Body apellidos: String): Response<Medico>

    @PUT("/api/medicos/actualizar-especialidad-de/{id}")
    suspend fun updateMedicoEspecialidad(@Path("id") id: Long, @Body especialidad: String): Response<Medico>

    @PUT("/api/medicos/actualizar-dni-de/{id}")
    suspend fun updateMedicoDni(@Path("id") id: Long, @Body dni: String): Response<Medico>

    @GET("/api/medicos/buscar-por-id/{id}")
    suspend fun getMedicoById(@Path("id") id: Long): Response<Medico>

    @DELETE("/api/medicos/eliminar-por-id/{id}")
    suspend fun deleteMedico(@Path("id") id: Long): Response<Void>

    @GET("/api/medicos/buscar-por-dni/{dni}")
    suspend fun getMedicoByDni(@Path("dni") dni: String): Response<Medico>

    @GET("/api/medicos/buscar-por-nombres/{nombres}")
    suspend fun getMedicosByNombres(@Path("nombres") nombres: String): Response<List<Medico>>

    @GET("/api/medicos/buscar-por-apellidos/{apellidos}")
    suspend fun getMedicosByApellidos(@Path("apellidos") apellidos: String): Response<List<Medico>>

    @GET("/api/medicos/buscar-por-especialidad/{especialidad}")
    suspend fun getMedicosByEspecialidad(@Path("especialidad") especialidad: String): Response<List<Medico>>
}