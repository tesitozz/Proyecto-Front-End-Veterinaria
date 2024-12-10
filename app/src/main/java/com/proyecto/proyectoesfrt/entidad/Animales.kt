package com.proyecto.proyectoesfrt.entidad

class Animales(
        val codigoAnimal: Int? = null, // ID del animal
               val nombreAnimal: String,
               val dueno: String,
               val edad: Int,
               val peso: Double,
               val informacionAnimal: String,
               val generoAnim: String,
               val tipoAnimal: String) {
}