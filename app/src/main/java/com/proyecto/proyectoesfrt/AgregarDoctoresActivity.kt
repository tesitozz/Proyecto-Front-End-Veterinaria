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
import com.proyecto.proyectoesfrt.entidad.Cliente
import com.proyecto.proyectoesfrt.entidad.Doctor
import com.proyecto.proyectoesfrt.service.ApiServiceCliente
import com.proyecto.proyectoesfrt.service.ApiServiceDoctores
import kotlinx.coroutines.launch

class AgregarDoctoresActivity : AppCompatActivity() {

    private lateinit var txtNombreDoctoresRegistrar : TextView
    private lateinit var txtDoctoresApellidosRegistrar : TextView
    private lateinit var txtDNIDoctoresRegistrar : TextView
    private lateinit var spnEspecialidadDocRegistrar : AutoCompleteTextView
    private lateinit var spnExperienciaDocRegistrar : AutoCompleteTextView
    private lateinit var btnGrabarDoctorRegistrar : Button
    private lateinit var btnRegresarDoctorRegistrar  : Button


    //Api
    private lateinit var api: ApiServiceDoctores


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
        spnExperienciaDocRegistrar=findViewById(R.id.spnExperienciaDocRegistrar)
        btnGrabarDoctorRegistrar = findViewById(R.id.btnGrabarDoctorRegistrar)
        btnRegresarDoctorRegistrar = findViewById(R.id.btnRegresarDoctorRegistrar)

        api = ApiUtils.getApiDoctor()

        btnGrabarDoctorRegistrar.setOnClickListener { registrarDoctorApi() }
        btnRegresarDoctorRegistrar.setOnClickListener { regresarDoctor() }

    }

    private fun registrarDoctorApi() {
        // Recoge los datos de las vistas
        val nombreDoctor = txtNombreDoctoresRegistrar.text.toString()
        val apellidosDoctor = txtDoctoresApellidosRegistrar.text.toString()
        val dniDoctor = txtDNIDoctoresRegistrar.text.toString().toIntOrNull() ?: 0 // Cambiado para manejar excepciones
        val especialidadDoctor = spnEspecialidadDocRegistrar.text.toString()
        val experienciaDoctor = spnExperienciaDocRegistrar.text.toString()

        // Crea un objeto Doctor con los datos
        val nuevoDoctor = Doctor(0, nombreDoctor, apellidosDoctor, dniDoctor, especialidadDoctor, experienciaDoctor)

        // Llama a tu API para registrar el doctor
        lifecycleScope.launch {
            try {
                val response = api.insertarDoctor(nuevoDoctor)
                if (response.isSuccessful) {
                    Toast.makeText(this@AgregarDoctoresActivity, "Doctor registrado con éxito", Toast.LENGTH_SHORT).show()
                    regresarDoctor() // Vuelve a la lista de doctores
                } else {
                    Toast.makeText(this@AgregarDoctoresActivity, "Error al registrar el doctor: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AgregarDoctoresActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun  regresarDoctor(){
        var intent= Intent(this,ListaDoctoresActivity::class.java)
        startActivity(intent)
    }





}