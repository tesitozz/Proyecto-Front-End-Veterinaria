package com.proyecto.proyectoesfrt

import android.content.Intent
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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

    //REGISTRAR DOCTOR --FALTAV VALIDACIÓN
    private fun registrarDoctorApi() {
        // Recoge los datos de las vistas
        val nombreDoctor = txtNombreDoctoresRegistrar.text.toString().trim()
        val apellidosDoctor = txtDoctoresApellidosRegistrar.text.toString().trim()
        val dniDoctor = txtDNIDoctoresRegistrar.text.toString().trim()
        val especialidadDoctor = spnEspecialidadDocRegistrar.text.toString().trim()
        val experienciaDoctor = spnExperienciaDocRegistrar.text.toString().trim()

        // Validación de los campos
        if (nombreDoctor.isEmpty()) {
            showAlert("El nombre no puede estar vacío", AlertType.WARNING)
            return
        }

        if (apellidosDoctor.isEmpty()) {
            showAlert("Los apellidos no pueden estar vacíos", AlertType.WARNING)
            return
        }

        if (dniDoctor.isEmpty()) {
            showAlert("El DNI no puede estar vacío", AlertType.WARNING)
            return
        }

        if (dniDoctor.length != 8 || !dniDoctor.all { it.isDigit() }) {
            showAlert("El DNI debe contener exactamente 8 dígitos numéricos", AlertType.WARNING)
            return
        }

        if (especialidadDoctor.isEmpty()) {
            showAlert("La especialidad no puede estar vacía", AlertType.WARNING)
            return
        }

        if (experienciaDoctor.isEmpty()) {
            showAlert("La experiencia no puede estar vacía", AlertType.WARNING)
            return
        }

        // Crea un objeto Doctor con los datos
        val nuevoDoctor = Doctor(
            codigoDoctor = 0,
            nombreDoctor = nombreDoctor,
            apellidoDoctor = apellidosDoctor,
            dniDoctor = dniDoctor.toInt(),
            especialidad = especialidadDoctor,
            experiencia = experienciaDoctor
        )

        // Llama a tu API para registrar el doctor
        lifecycleScope.launch {
            try {
                val response = api.insertarDoctor(nuevoDoctor)
                if (response.isSuccessful) {
                    showAlert("Doctor registrado con éxito", AlertType.SUCCESS)
                    regresarDoctor() // Vuelve a la lista de doctores
                } else {
                    showAlert("Error al registrar el doctor: ${response.message()}", AlertType.ERROR)
                }
            } catch (e: Exception) {
                showAlert("Error: ${e.message}", AlertType.ERROR)
            }
        }
    }

    // Función para mostrar alertas con estilos personalizados
    enum class AlertType {
        SUCCESS, WARNING, ERROR
    }

    fun showAlert(mensaje: String, type: AlertType) {
        val builder = AlertDialog.Builder(this)

        when (type) {
            AlertType.SUCCESS -> {
                builder.setTitle("\u2705 ÉXITO")
            }
            AlertType.WARNING -> {
                builder.setTitle("\u26A0 ADVERTENCIA")
            }
            AlertType.ERROR -> {
                builder.setTitle("\u274C ERROR")
            }
        }

        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }



    private fun  regresarDoctor(){
        var intent= Intent(this,ListaDoctoresActivity::class.java)
        startActivity(intent)
    }





}