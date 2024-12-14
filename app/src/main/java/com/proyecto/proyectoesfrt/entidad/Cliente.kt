        package com.proyecto.proyectoesfrt.entidad

        import jakarta.persistence.* // Asegúrate de usar las dependencias correctas
        import java.io.Serializable

        data class Cliente(
            var id: Long? = null,
            var nombres: String,
            var apellidos: String,
            var dni: String,
            var genero: String? = null, // Campo opcional
            var correo: String? = null, // Campo opcional
            var celular: String,
            var direccion: String? = null // Campo opcional
        ) : Serializable