package com.proyecto.proyectoesfrt.service

import android.telecom.Call
import com.proyecto.proyectoesfrt.entidad.Animal
import com.proyecto.proyectoesfrt.entidad.Cliente
import com.proyecto.proyectoesfrt.entidad.HistorialClinica
import com.proyecto.proyectoesfrt.entidad.Medico
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.time.LocalDate
import java.time.LocalTime

interface ApiServiceHistorial {

    @GET("/api/historias-clinicas/listar")
    fun getAllHistoriaClinica(): retrofit2.Call<List<HistorialClinica>>

    @POST("/api/historias-clinicas/crear")
    fun createHistoriaClinica(@Body historiaClinica: HistorialClinica): retrofit2.Call<HistorialClinica>

    @PUT("/api/historias-clinicas/actualizar/{id}")
    suspend fun updateHistoriaClinica(
        @Path("id") id: Long,  // Recibe el id de la URL
        @Body historialClinica: HistorialClinica  // Recibe el objeto con los datos actualizados
    ): HistorialClinica

    @PUT("/api/historias-clinicas/actualizar-fecha-registro-de/{id}")
    fun updateFechaRegistroHC(@Path("id") id: Long, @Body fechaDeRegistro: LocalDate): retrofit2.Call<HistorialClinica>

    @PUT("/api/historias-clinicas/actualizar-hora-registro-de/{id}")
    fun updateHoraRegistroHC(@Path("id") id: Long, @Body horaDeRegistro: LocalTime): retrofit2.Call<HistorialClinica>

    @PUT("/api/historias-clinicas/actualizar-animal-de/{id}")
    fun updateAnimalHC(@Path("id") id: Long, @Body animal: Animal): retrofit2.Call<HistorialClinica>

    @PUT("/api/historias-clinicas/actualizar-cliente-de/{id}")
    fun updateClienteHC(@Path("id") id: Long, @Body cliente: Cliente): retrofit2.Call<HistorialClinica>

    @PUT("/api/historias-clinicas/actualizar-medico-de/{id}")
    fun updateMedicoHC(@Path("id") id: Long, @Body medico: Medico): retrofit2.Call<HistorialClinica>

    @GET("/api/historias-clinicas/buscar-por-id/{id}")
    fun findByIDHC(@Path("id") id: Long): retrofit2.Call<HistorialClinica>

    @DELETE("/api/historias-clinicas/eliminar-por-id/{id}")
    fun deleteHistoriaClinica(@Path("id") id: Long): retrofit2.Call<Void>

    @GET("/api/historias-clinicas/buscar-por-animal-id/{animalId}")
    fun findByAnimalIdHC(@Path("animalId") animalId: Long): retrofit2.Call<List<HistorialClinica>>

    @GET("/api/historias-clinicas/buscar-por-cliente-id/{clienteId}")
    fun findByClienteIdHC(@Path("clienteId") clienteId: Long): retrofit2.Call<List<HistorialClinica>>

    @GET("/api/historias-clinicas/buscar-por-medico-id/{medicoId}")
    fun findByMedicoIdHC(@Path("medicoId") medicoId: Long): retrofit2.Call<List<HistorialClinica>>

    @GET("/api/historias-clinicas/buscar-por-anio-fecha/{year}")
    fun findByYearOfFechaDeRegistroHC(@Path("year") year: Int): retrofit2.Call<List<HistorialClinica>>

    @GET("/api/historias-clinicas/buscar-por-mes-fecha/{month}")
    fun findByMonthOfFechaDeRegistroHC(@Path("month") month: Int): retrofit2.Call<List<HistorialClinica>>

    @GET("/api/historias-clinicas/buscar-por-hora-fecha/{hour}")
    fun findByHourOfHoraDeRegistroHC(@Path("hour") hour: Int): retrofit2.Call<List<HistorialClinica>>

    @GET("/api/historias-clinicas/buscar-por-minuto-fecha/{minute}")
    fun findByMinuteOfHoraDeRegistroHC(@Path("minute") minute: Int): retrofit2.Call<List<HistorialClinica>>

    @GET("/api/historias-clinicas/buscar-por-fecha/{fechaDeRegistro}")
    fun findByFechaDeRegistroHC(@Path("fechaDeRegistro") fechaDeRegistro: LocalDate): retrofit2.Call<List<HistorialClinica>>

    @GET("/api/historias-clinicas/buscar-por-hora/{horaDeRegistro}")
    fun findByHoraDeRegistroHC(@Path("horaDeRegistro") horaDeRegistro: LocalTime): retrofit2.Call<List<HistorialClinica>>
}