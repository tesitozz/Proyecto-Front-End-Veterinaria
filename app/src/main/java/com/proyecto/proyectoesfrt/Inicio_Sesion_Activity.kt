package com.proyecto.proyectoesfrt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.proyecto.proyectoesfrt.api.RetrofitClient
import com.proyecto.proyectoesfrt.entidad.LoginRequest
import com.proyecto.proyectoesfrt.entidad.Usuario
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class Inicio_Sesion_Activity : AppCompatActivity() {

    private lateinit var btnUsuarioLogin: Button
    private lateinit var txtUsuarioLogin: TextInputEditText
    private lateinit var txtClave: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.inicio_sesion_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnUsuarioLogin=findViewById(R.id.btnUsuarioLogin)
        txtUsuarioLogin=findViewById(R.id.txtUsuarioLogin)
        txtClave=findViewById(R.id.txtClave)

        btnUsuarioLogin.setOnClickListener { logearse() }

    }

    //FUNCION LOGIN - INICIO SESIÓN
    fun logearse() {
        val username = txtUsuarioLogin.text.toString().trim()
        val password = txtClave.text.toString().trim()

        // Validar que los campos no estén vacíos
        if (username.isEmpty()) {
            mostrarError("Por favor ingresa tu usuario")
            return
        }

        if (password.isEmpty()) {
            mostrarError("Por favor ingresa tu contraseña")
            return
        }

        // Validación del campo de usuario (evitar caracteres especiales)
        val regexUsuario = "^[a-zA-Z0-8]+$".toRegex() // Solo permite letras y números
        if (!username.matches(regexUsuario)) {
            mostrarError("El usuario no puede contener caracteres especiales.")
            return
        }

        // Validación de la contraseña (mínimo 9 caracteres)
        if (password.length < 8) {
            mostrarError("La contraseña debe tener al menos 9 caracteres.")
            return
        }

        // Validación de la contraseña (debe contener al menos una mayúscula)
        val regexContrasenaMayuscula = ".*[A-Z].*".toRegex() // La contraseña debe tener al menos una letra mayúscula
        if (!password.matches(regexContrasenaMayuscula)) {
            mostrarError("La contraseña debe contener al menos una letra mayúscula.")
            return
        }

        // Crear el objeto de solicitud de inicio de sesión
        val loginRequest = LoginRequest(username, password)

        // Llamada a la API usando Retrofit
        val call = RetrofitClient.apiLogeoUsuario.login(loginRequest)
        call.enqueue(object : retrofit2.Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.isSuccessful) {
                    val usuario = response.body()

                    // No es necesario verificar el rol ya que no existe
                    if (usuario != null) {
                        // Redirigir a MainActivity
                        val intent = Intent(this@Inicio_Sesion_Activity, MainActivity::class.java)
                        startActivity(intent)
                        finish() // Finaliza la actividad actual
                    } else {
                        mostrarError("Usuario no encontrado")
                    }
                } else {
                    mostrarError("Contraseña o usuario incorrecto")
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                // Mostrar un mensaje de error si falla la conexión con el servidor
                mostrarError("Error de conexión: ${t.message}")
                Log.e("LoginError", t.message.toString())
            }
        })
    }



    private fun mostrarError(mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .show()
    }

}