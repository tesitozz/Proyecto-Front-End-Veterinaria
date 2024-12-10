package com.proyecto.proyectoesfrt

import android.content.Intent
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.appproyecto.utils.ApiUtils
import com.google.android.material.textfield.TextInputEditText
import com.proyecto.proyectoesfrt.entidad.Animales
import com.proyecto.proyectoesfrt.service.ApiServiceAnimal
import kotlinx.coroutines.launch

class AgregarAnimalesActivity : AppCompatActivity() {

    private lateinit var txtNombreAnimal: TextInputEditText
    private lateinit var txtDueno: TextInputEditText
    private lateinit var txtEdad: TextInputEditText
    private lateinit var txtPeso: TextInputEditText
    private lateinit var txtInformacionAnimal: TextInputEditText
    private lateinit var spnSexo: AutoCompleteTextView
    private lateinit var spnTipo: AutoCompleteTextView
    private lateinit var btnRegistrar: Button


    //Api
    private lateinit var api: ApiServiceAnimal


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.animales_agregar_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtNombreAnimal=findViewById(R.id.txtNombreAnimalAgregar)
        txtDueno=findViewById(R.id.txtNombreDuenoAgregar)
        txtEdad=findViewById(R.id.txtEdadAnimalAgregar)
        txtPeso=findViewById(R.id.txtPesoAnimalAgregar)
        txtInformacionAnimal=findViewById(R.id.txtInformacionAnimalAgregar)
        spnSexo=findViewById(R.id.spnGeneroAgregar)
        spnTipo=findViewById(R.id.spnTipoAgregar)
        btnRegistrar=findViewById(R.id.btnRegistrarAnimalAgregar)

        api = ApiUtils.getApiAnimal()

        btnRegistrar.setOnClickListener { registrarAnimalApi() }

    }

    private fun registrarAnimalApi() {
        // Obtener los valores de los campos de texto
        val nombre = txtNombreAnimal.text.toString()
        val dueno = txtDueno.text.toString()
        val edad = txtEdad.text.toString().toIntOrNull() ?: 0 // Cambiar a 0 si no es un número válido
        val peso = txtPeso.text.toString().toDoubleOrNull() ?: 0.0 // Cambiar a 0.0 si no es un número válido
        val informacion = txtInformacionAnimal.text.toString()
        val sexo = spnSexo.text.toString()
        val tipo = spnTipo.text.toString()

        // Crear un objeto Animales sin el códigoAnimal
        val nuevoAnimal = Animales(
            nombreAnimal = nombre,
            dueno = dueno,
            edad = edad,
            peso = peso,
            informacionAnimal = informacion,
            generoAnim = sexo,
            tipoAnimal = tipo
        )

        // Llamada a la API para registrar el animal
        lifecycleScope.launch {
            try {
                val response = api.insertarAnimal(nuevoAnimal)
                if (response.isSuccessful) {
                    // Mostrar mensaje de éxito
                    Toast.makeText(this@AgregarAnimalesActivity, "Animal registrado con éxito", Toast.LENGTH_SHORT).show()

                    // Redirigir a ListaAnimalesActivity
                    val intent = Intent(this@AgregarAnimalesActivity, ListaAnimalesActivity::class.java)
                    startActivity(intent)
                    finish() // Finaliza la actividad actual
                } else {
                    // Manejar el error de la respuesta
                    Toast.makeText(this@AgregarAnimalesActivity, "Error al registrar el animal: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Manejar excepciones
                Toast.makeText(this@AgregarAnimalesActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}