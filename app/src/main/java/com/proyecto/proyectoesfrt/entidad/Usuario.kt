package com.proyecto.proyectoesfrt.entidad

data class Usuario(
    val id: Long = 0,
    val usuario: String = "",
    val gmail: String = "",
    val password: String = ""  // Cambiado de contrasena a password
)
