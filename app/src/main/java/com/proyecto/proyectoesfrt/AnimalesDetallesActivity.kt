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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class AnimalesDetallesActivity : AppCompatActivity() {

    private lateinit var txtCodigoAni: TextView
    private lateinit var txtNombreAnimal: TextInputEditText
    private lateinit var txtDueno: TextInputEditText
    private lateinit var txtEdad: TextInputEditText
    private lateinit var txtPeso: TextInputEditText
    private lateinit var txtInformacionAnimal: TextInputEditText
    private lateinit var spnSexo: AutoCompleteTextView
    private lateinit var spnTipo: AutoCompleteTextView
    private lateinit var btnActualizar: Button
    private lateinit var btnRegresar: Button
    private lateinit var btnEliminar: Button

    //Api
    private lateinit var api: ApiServiceAnimal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.animales_detalles_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtCodigoAni = findViewById(R.id.txtCodigoAnimal)
        txtNombreAnimal = findViewById(R.id.txtNombreAnimalActualizar)
        txtDueno = findViewById(R.id.txtNombreDuenoActualizar)
        txtEdad = findViewById(R.id.txtEdadAnimalActualizar)
        txtPeso = findViewById(R.id.txtPesoAnimalActualizar)
        txtInformacionAnimal = findViewById(R.id.txtInformacionAnimalActualizar)
        spnSexo = findViewById(R.id.spnGeneroActualizar)
        spnTipo = findViewById(R.id.spnTipoActualizar)
        btnActualizar = findViewById(R.id.btnGrabarAnimalActualizar)
        btnRegresar = findViewById(R.id.btnRegresarAnimalActualizar)
        btnEliminar = findViewById(R.id.btnEliminarAnimal)

        // Api
        api = ApiUtils.getApiAnimal()

        // Obtener datos del Intent
        obtenerDatos()

        btnActualizar.setOnClickListener {
            actualizarAnimal()
        }

        btnEliminar.setOnClickListener {
            eliminarAnimal()
        }

        btnRegresar.setOnClickListener {
            volverListado()
        }
    }

    private fun obtenerDatos() {
        // Suponiendo que los datos del animal se pasan a través del Intent
        val codigoAnimal = intent.getIntExtra("CODIGO_ANIMAL", 0) // Cambiar a getIntExtra
        val nombreAnimal = intent.getStringExtra("NOMBRE_ANIMAL")
        val dueno = intent.getStringExtra("DUENO")
        val edad = intent.getIntExtra("EDAD", 0)
        val peso = intent.getDoubleExtra("PESO", 0.0)
        val informacionAnimal = intent.getStringExtra("INFORMACION_ANIMAL")
        val sexo = intent.getStringExtra("SEXO")
        val tipo = intent.getStringExtra("TIPO")

        // Rellenar los campos de texto
        txtCodigoAni.text = codigoAnimal.toString()
        txtNombreAnimal.setText(nombreAnimal)
        txtDueno.setText(dueno)
        txtEdad.setText(edad.toString())
        txtPeso.setText(peso.toString())
        txtInformacionAnimal.setText(informacionAnimal)
        spnSexo.setText(sexo)
        spnTipo.setText(tipo)
    }



    private fun eliminarAnimal() {
        val codigoAnimal = txtCodigoAni.text.toString()

        // Verificar si el campo está vacío
        if (codigoAnimal.isEmpty()) {
            Toast.makeText(this@AnimalesDetallesActivity, "Por favor, ingrese el código del animal", Toast.LENGTH_SHORT).show()
            return // Salir de la función si el campo está vacío
        }

        // Llamar a la API para eliminar el animal
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response<Void> = api.eliminarAnimal(codigoAnimal.toInt())
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AnimalesDetallesActivity, "Animal eliminado con éxito", Toast.LENGTH_SHORT).show()
                        // Configurar resultado y cerrar la actividad
                        setResult(RESULT_OK)
                        volverListado() // Cierra la actividad
                    } else {
                        Toast.makeText(this@AnimalesDetallesActivity, "Error al eliminar el animal", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: NumberFormatException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AnimalesDetallesActivity, "Código inválido", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun actualizarAnimal() {
        // Obtener el código del animal (debe estar en algún campo de texto, como txtCodigoAni)
        val codigoAnimal = txtCodigoAni.text.toString()

        // Verificar si el código está vacío
        if (codigoAnimal.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese el código del animal", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener los datos del animal de los campos de texto
        val nombreAnimal = txtNombreAnimal.text.toString()
        val dueno = txtDueno.text.toString()
        val edad = txtEdad.text.toString().toIntOrNull() ?: 0 // Manejar la conversión
        val peso = txtPeso.text.toString().toDoubleOrNull() ?: 0.0 // Manejar la conversión
        val informacionAnimal = txtInformacionAnimal.text.toString()
        val sexo = spnSexo.text.toString()
        val tipo = spnTipo.text.toString()

        // Crear el objeto Animales con los datos actualizados
        val animalActualizado = Animales(
            codigoAnimal = codigoAnimal.toInt(),
            nombreAnimal = nombreAnimal,
            dueno = dueno,
            edad = edad,
            peso = peso,
            informacionAnimal = informacionAnimal,
            generoAnim = sexo,
            tipoAnimal = tipo
        )

        // Llamar a la API para actualizar el animal
        lifecycleScope.launch {
            val response = api.actualizarAnimal(codigoAnimal.toInt(), animalActualizado)
            if (response.isSuccessful) {
                Toast.makeText(this@AnimalesDetallesActivity, "Animal actualizado con éxito", Toast.LENGTH_SHORT).show()
                // Redirigir a la Lista de Animales
                finish() // Cerrar la actividad actual
            } else {
                Toast.makeText(this@AnimalesDetallesActivity, "Error al actualizar el animal", Toast.LENGTH_SHORT).show()
            }
        }
    }




    private  fun volverListado(){
        var intent= Intent(this,ListaAnimalesActivity::class.java)
        startActivity(intent)
    }
}