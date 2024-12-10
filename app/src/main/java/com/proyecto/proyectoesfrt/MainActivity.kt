package com.proyecto.proyectoesfrt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var btnAnimales: Button
    private lateinit var btnClientes: Button
    private lateinit var btnDoctores: Button
    private lateinit var btnCerrarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnAnimales=findViewById(R.id.btnAnimales)
        btnClientes=findViewById(R.id.btnClientes)
        btnDoctores=findViewById(R.id.btnDoctores)
        btnCerrarSesion=findViewById(R.id.btnCerrarSesion)
        btnClientes.setOnClickListener { irClientes() }
        btnAnimales.setOnClickListener { go() }
        btnDoctores.setOnClickListener { irDoctores() }
        btnCerrarSesion.setOnClickListener { cerrarSesion() }
    }
    fun irClientes(){
        var intentClientes= Intent(this,ListaClientesActivity::class.java)
        startActivity(intentClientes)

    }


    fun irDoctores(){
        var intentClientes= Intent(this,ListaDoctoresActivity::class.java)
        startActivity(intentClientes)

    }

    fun go(){
        var intent= Intent(this,ListaAnimalesActivity::class.java)
        startActivity(intent)
    }

    fun cerrarSesion() {
        val sharedPreferences = getSharedPreferences("MiPreferencia", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        // Paso 2: Redirigir a la actividad de inicio de sesión
        val intent = Intent(this, Inicio_Sesion_Activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }



}