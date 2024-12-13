package com.proyecto.proyectoesfrt

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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

    //REGISTRAR ANIMAL --VALIDADO ANIMALE
    private fun registrarAnimalApi() {
        // Obtener los valores de los campos de texto
        val nombre = txtNombreAnimal.text.toString().trim()
        val dueno = txtDueno.text.toString().trim()
        val edadStr = txtEdad.text.toString().trim() // Mantener como String para validaciones
        val pesoStr = txtPeso.text.toString().trim() // Mantener como String para validaciones
        val informacion = txtInformacionAnimal.text.toString().trim()
        val sexo = spnSexo.isSelected?.toString()?.trim() ?: "" // Asegurar valor desde Spinner
        val tipo = spnTipo.isSelected?.toString()?.trim() ?: ""

        // Validar que ningún campo esté vacío
        if (nombre.isEmpty()) {
            showAlert("Por favor, ingresa el nombre del animal.")
            return
        }

        if (dueno.isEmpty()) {
            showAlert("Por favor, ingresa el nombre del dueño.")
            return
        }

        if (edadStr.isEmpty()) {
            showAlert("Por favor, ingresa la edad del animal.")
            return
        }

        if (pesoStr.isEmpty()) {
            showAlert("Por favor, ingresa el peso del animal.")
            return
        }

        if (informacion.isEmpty()) {
            showAlert("Por favor, ingresa información adicional sobre el animal.")
            return
        }

        if (sexo.isEmpty() || sexo == "Seleccionar") {
            showAlert("Por favor, selecciona el sexo del animal.")
            return
        }

        if (tipo.isEmpty() || tipo == "Seleccionar") {
            showAlert("Por favor, selecciona el tipo de animal.")
            return
        }

        // Validar que la edad sea un número válido y no mayor a 90
        val edad = edadStr.toIntOrNull()
        if (edad == null || edad <= 0 || edad > 100) {
            showAlert("La edad del animal debe ser un número válido entre 1 y 90.")
            return
        }

        // Validar que el peso sea un número válido
        val peso = pesoStr.toDoubleOrNull()
        if (peso == null || peso <= 0) {
            showAlert("El peso del animal debe ser un número válido mayor a 0.")
            return
        }

        // Crear un objeto Animales con los datos
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
                    showAlert("Animal registrado con éxito.")
                    // Redirigir a ListaAnimalesActivity
                    val intent = Intent(this@AgregarAnimalesActivity, ListaAnimalesActivity::class.java)
                    startActivity(intent)
                    finish() // Finaliza la actividad actual
                } else {
                    // Manejar el error de la respuesta
                    showAlert("Error al registrar el animal: ${response.message()}")
                }
            } catch (e: Exception) {
                // Manejar excepciones
                showAlert("Error: ${e.message}")
            }
        }
    }

    fun showAlert(mensaje: String) {
        val toast = Toast.makeText(this, mensaje, Toast.LENGTH_LONG) // LONG muestra el mensaje por más tiempo
        toast.show()
    }




}