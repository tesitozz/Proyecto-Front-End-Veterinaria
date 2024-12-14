package com.proyecto.proyectoesfrt.entidad

import jakarta.persistence.*
import java.io.Serializable

data class Medico(
    var id: Long? = null,
    var nombres: String,
    var apellidos: String,
    var dni: String,
    var especialidad: String
) : Serializable