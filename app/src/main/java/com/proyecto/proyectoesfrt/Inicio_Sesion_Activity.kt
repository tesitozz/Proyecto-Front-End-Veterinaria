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
import retrofit2.Call
import retrofit2.Response

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

        ///btnUsuarioLogin.setOnClickListener { logearse() }

    }

    //FUNCION LOGIN - INICIO SESIÓN



    private fun mostrarError(mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .show()
    }

}