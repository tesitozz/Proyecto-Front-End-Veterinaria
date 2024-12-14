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
import com.proyecto.proyectoesfrt.entidad.Medico
import com.proyecto.proyectoesfrt.service.ApiServiceMedicos
import kotlinx.coroutines.launch

class AgregarDoctoresActivity : AppCompatActivity() {

    private lateinit var txtNombreDoctoresRegistrar : TextView
    private lateinit var txtDoctoresApellidosRegistrar : TextView
    private lateinit var txtDNIDoctoresRegistrar : TextView
    private lateinit var spnEspecialidadDocRegistrar : AutoCompleteTextView
    private lateinit var btnGrabarDoctorRegistrar : Button
    private lateinit var btnRegresarDoctorRegistrar  : Button


    //Api
    private lateinit var api: ApiServiceMedicos


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.doctores_agregar_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtNombreDoctoresRegistrar=findViewById(R.id.txtNombreDoctoresRegistrar)
        txtDoctoresApellidosRegistrar=findViewById(R.id.txtDoctoresApellidosRegistrar)
        txtDNIDoctoresRegistrar=findViewById(R.id.txtDNIDoctoresRegistrar)
        spnEspecialidadDocRegistrar=findViewById(R.id.spnEspecialidadDocRegistrar)
        btnGrabarDoctorRegistrar = findViewById(R.id.btnGrabarDoctorRegistrar)
        btnRegresarDoctorRegistrar = findViewById(R.id.btnRegresarDoctorRegistrar)

        api = ApiUtils.getApiDoctor()

        btnGrabarDoctorRegistrar.setOnClickListener { registrarMedico() }
        btnRegresarDoctorRegistrar.setOnClickListener { regresarMedico() }

    }

    fun registrarMedico() {
        // Obtener los valores desde los campos de entrada
        val nombre = txtNombreDoctoresRegistrar.text.toString().trim()
        val apellidos = txtDoctoresApellidosRegistrar.text.toString().trim()
        val dni = txtDNIDoctoresRegistrar.text.toString().trim()
        val especialidad = spnEspecialidadDocRegistrar.text.toString().trim()

        // Validar los campos
        if (nombre.isEmpty() || apellidos.isEmpty() || dni.isEmpty() || especialidad.isEmpty()) {
            // Mostrar un mensaje de error si algún campo está vacío
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear un objeto Medico con los datos obtenidos
        val medico = Medico(
            nombres = nombre,
            apellidos = apellidos,
            dni = dni,
            especialidad = especialidad
        )

        // Realizar la solicitud para registrar al médico
        lifecycleScope.launch {
            try {
                val response = api.createMedico(medico)
                if (response.isSuccessful) {
                    Toast.makeText(this@AgregarDoctoresActivity, "Médico registrado con éxito", Toast.LENGTH_SHORT).show()
                    // Regresar a la pantalla anterior o realizar alguna acción
                    finish()
                regresarMedico()// Cierra la actividad actual y vuelve a la anterior
                } else {
                    Toast.makeText(this@AgregarDoctoresActivity, "Error al registrar el médico", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AgregarDoctoresActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun regresarMedico(){
        val intent = Intent(this, ListaDoctoresActivity::class.java)
        startActivity(intent)

    }
}