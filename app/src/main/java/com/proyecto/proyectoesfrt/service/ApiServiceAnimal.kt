package com.proyecto.proyectoesfrt.service

import com.proyecto.proyectoesfrt.entidad.Animal
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiServiceAnimal {

    // Obtener todos los animales
    @GET("/api/animales/listar")
    suspend fun getAllAnimales(): Response<List<Animal>>

    // Crear un animal
    @POST("/api/animales/crear")
    suspend fun createAnimal(@Body animal: Animal): Response<Animal>

    // Actualizar un animal completo
    @PUT("/api/animales/actualizar")
    suspend fun updateAnimal(@Body animal: Animal): Response<Animal>

    // Actualizar solo el nombre de un animal
    @PUT("/api/animales/actualizar-nombre-de/{id}")
    suspend fun updateNombreAnimal(@Path("id") id: Long, @Body nombre: String): Response<Animal>

    // Actualizar solo el tipo de un animal
    @PUT("/api/animales/actualizar-tipo-de/{id}")
    suspend fun updateTipoAnimal(@Path("id") id: Long, @Body tipo: String): Response<Animal>

    // Actualizar solo el género de un animal
    @PUT("/api/animales/actualizar-genero-de/{id}")
    suspend fun updateGeneroAnimal(@Path("id") id: Long, @Body genero: String): Response<Animal>

    // Actualizar solo la edad de un animal
    @PUT("/api/animales/actualizar-edad-de/{id}")
    suspend fun updateEdadAnimal(@Path("id") id: Long, @Body edad: Int): Response<Animal>

    // Actualizar solo el peso de un animal
    @PUT("/api/animales/actualizar-peso-de/{id}")
    suspend fun updatePesoAnimal(@Path("id") id: Long, @Body peso: Double): Response<Animal>

    // Actualizar solo la raza de un animal
    @PUT("/api/animales/actualizar-raza-de/{id}")
    suspend fun updateRazaAnimal(@Path("id") id: Long, @Body raza: String): Response<Animal>

    // Actualizar solo el color de un animal
    @PUT("/api/animales/actualizar-color-de/{id}")
    suspend fun updateColorAnimal(@Path("id") id: Long, @Body color: String): Response<Animal>

    // Buscar animal por ID
    @GET("/api/animales/buscar-por-id/{id}")
    suspend fun getAnimalById(@Path("id") id: Long): Response<Animal>

    // Eliminar animal por ID
    @DELETE("/api/animales/eliminar-por-id/{id}")
    suspend fun deleteAnimal(@Path("id") id: Long): Response<Void>

    // Buscar animales por nombre
    @GET("/api/animales/buscar-por-nombre/{nombre}")
    suspend fun getAnimalesByNombre(@Path("nombre") nombre: String): Response<List<Animal>>

    // Buscar animales por tipo
    @GET("/api/animales/buscar-por-tipo/{tipo}")
    suspend fun getAnimalesByTipo(@Path("tipo") tipo: String): Response<List<Animal>>

    // Buscar animales por género
    @GET("/api/animales/buscar-por-genero/{genero}")
    suspend fun getAnimalesByGenero(@Path("genero") genero: String): Response<List<Animal>>

    // Buscar animales por edad
    @GET("/api/animales/buscar-por-edad/{edad}")
    suspend fun getAnimalesByEdad(@Path("edad") edad: Int): Response<List<Animal>>

    // Buscar animales por peso
    @GET("/api/animales/buscar-por-peso/{peso}")
    suspend fun getAnimalesByPeso(@Path("peso") peso: Double): Response<List<Animal>>

    // Buscar animales por raza
    @GET("/api/animales/buscar-por-raza/{raza}")
    suspend fun getAnimalesByRaza(@Path("raza") raza: String): Response<List<Animal>>

    // Buscar animales por color
    @GET("/api/animales/buscar-por-color/{color}")
    suspend fun getAnimalesByColor(@Path("color") color: String): Response<List<Animal>>

    // Buscar animales por cliente ID
    @GET("/api/animales/buscar-por-id-de-cliente/{clienteId}")
    suspend fun getAnimalesByClienteId(@Path("clienteId") clienteId: Long): Response<List<Animal>>

}