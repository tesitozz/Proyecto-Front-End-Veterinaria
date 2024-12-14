package com.proyecto.proyectoesfrt.entidad

import jakarta.persistence.*
import java.io.Serializable

data class Animal(
    var id: Long? = null,
    var nombre: String,
    var tipo: String,
    var genero: String,
    var edad: Int,
    var peso: Double,
    var raza: String? = null,
    var color: String? = null,

    @ManyToOne
    @JoinColumn(name = "id_cli", nullable = false) // Relación con Cliente
    var cliente: Cliente
) : Serializable