package com.proyecto.proyectoesfrt.entidad

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

data class HistorialClinica(
    var id: Long? = null,
    var fechaDeRegistro: LocalDate,
    var horaDeRegistro: LocalTime,
    var animal: Animal,
    var cliente: Cliente,
    var medico: Medico
) : Serializable