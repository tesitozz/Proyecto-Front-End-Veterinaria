package com.proyecto.proyectoesfrt

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
import com.proyecto.proyectoesfrt.adaptador.AnimalesAdapter
import com.proyecto.proyectoesfrt.api.RetrofitClient
import kotlinx.coroutines.launch

class ListaAnimalesActivity : AppCompatActivity() {

    private lateinit var rvAnimales: RecyclerView
    private lateinit var btnNuevoAnimal: Button
    private lateinit var btnRegresar: Button

    companion object {
        private const val REQUEST_CODE = 100 // Código de solicitud para el Intent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.lista_animales_activity_main)

        // Configuración de márgenes para el contenido de la vista
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvAnimales = findViewById(R.id.rvAnimal)
        btnNuevoAnimal = findViewById(R.id.btnAgregarNuevoAnimal)
        btnRegresar = findViewById(R.id.btnRegresarDoctor)

        // Cargar datos desde la API
        cargarAnimalesDesdeApi()

        // Configurar el botón para agregar un nuevo animal
        btnNuevoAnimal.setOnClickListener {
            agregarNuevoAnimal()
        }

        btnRegresar.setOnClickListener {
            regresarAnimales()
        }
    }

    fun regresarAnimales(){
            var intent= Intent(this,MainActivity::class.java)
            startActivity(intent)

    }
    private fun  agregarNuevoAnimal(){
        var intent= Intent(this,AgregarAnimalesActivity::class.java)
        startActivity(intent)
    }

    private fun cargarAnimalesDesdeApi() {
        // Llamada a la API
        lifecycleScope.launch {
            val response = RetrofitClient.apiService.getAllAnimales()
            if (response.isSuccessful) {
                val animalesList = response.body() ?: emptyList()
                val adaptador = AnimalesAdapter(this@ListaAnimalesActivity, animalesList)
                rvAnimales.adapter = adaptador
                rvAnimales.layoutManager = LinearLayoutManager(this@ListaAnimalesActivity)
            } else {
                // Manejar errores de la respuesta
                Log.e("ListaAnimalesActivity", "Error al cargar animales: ${response.message()}")
                Toast.makeText(this@ListaAnimalesActivity, "Error al cargar animales", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Volver a cargar la lista de animales si se eliminó uno
            cargarAnimalesDesdeApi()
        }
    }
}
