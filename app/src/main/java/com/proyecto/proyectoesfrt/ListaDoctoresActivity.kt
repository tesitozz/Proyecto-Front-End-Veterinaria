package com.proyecto.proyectoesfrt

import DoctorAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.proyectoesfrt.api.RetrofitClient
import kotlinx.coroutines.launch

class ListaDoctoresActivity : AppCompatActivity() {

    private lateinit var rvDoctores: RecyclerView
    private lateinit var btnAgregarDoctor: Button
    private lateinit var btnRegresarDoctor: Button

    companion object {
        private const val REQUEST_CODE = 100 // Código de solicitud para el Intent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.lista_doctores_activity_main)

        // Configuración de márgenes para el contenido de la vista
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvDoctores = findViewById(R.id.rvDoctores)
        btnAgregarDoctor = findViewById(R.id.btnAgregarDoctor)
        btnRegresarDoctor = findViewById(R.id.btnRegresarDoctor)

        // Cargar datos desde la API
        cargarDoctorDesdeApi()

        // Configurar el botón para agregar un nuevo animal
        btnAgregarDoctor.setOnClickListener {
            agregarNuevoDoctor()
        }

        btnRegresarDoctor.setOnClickListener {
            regresarDoctor()
        }
    }

    fun regresarDoctor(){
            var intent= Intent(this,MainActivity::class.java)
            startActivity(intent)

    }



    private fun  agregarNuevoDoctor(){
        //Cambiar esta parte
        var intent= Intent(this,AgregarDoctoresActivity::class.java)
        startActivity(intent)
    }

    private fun cargarDoctorDesdeApi() {
        // Llamada a la API
        lifecycleScope.launch {
            val response = RetrofitClient.apiServiceDoctor.getAllDoctores()
            if (response.isSuccessful) {
                val doctoresList = response.body() ?: emptyList()
                val adaptador = DoctorAdapter(this@ListaDoctoresActivity, doctoresList)
                rvDoctores.adapter = adaptador
                rvDoctores.layoutManager = LinearLayoutManager(this@ListaDoctoresActivity)
            } else {
                // Manejar errores de la respuesta
                Log.e("ListaAnimalesActivity", "Error al cargar Doctor: ${response.message()}")
                Toast.makeText(this@ListaDoctoresActivity, "Error al cargar Doctores", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            cargarDoctorDesdeApi()
        }
    }
}
