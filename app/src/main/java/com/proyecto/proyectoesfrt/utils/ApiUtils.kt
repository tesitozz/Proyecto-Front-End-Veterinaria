package com.example.appproyecto.utils

import com.proyecto.proyectoesfrt.api.RetrofitClient
import com.proyecto.proyectoesfrt.service.ApiServiceAnimal
import com.proyecto.proyectoesfrt.service.ApiServiceCliente
import com.proyecto.proyectoesfrt.service.ApiServiceDoctores
import com.proyecto.proyectoesfrt.service.ApiServiceLogeo

class ApiUtils {
    companion object {
        // Función para obtener la API de Animales
        fun getApiAnimal(): ApiServiceAnimal {
            return RetrofitClient.apiService // Cambia aquí para usar apiServiceAnimal
        }

        // Función para obtener la API de Clientes
        fun getApiCliente(): ApiServiceCliente {
            return RetrofitClient.apiServiceCliente // Cambia aquí para usar apiServiceCliente
        }

        fun getApiDoctor() : ApiServiceDoctores{
            return RetrofitClient.apiServiceDoctor
        }

        fun getApiLogeo() : ApiServiceLogeo{
            return RetrofitClient.apiLogeoUsuario
        }
    }
}
