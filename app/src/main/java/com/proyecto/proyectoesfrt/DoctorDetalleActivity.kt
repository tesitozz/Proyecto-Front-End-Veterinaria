package com.proyecto.proyectoesfrt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appproyecto.utils.ApiUtils
import com.proyecto.proyectoesfrt.entidad.Cliente
import com.proyecto.proyectoesfrt.entidad.Doctor
import com.proyecto.proyectoesfrt.service.ApiServiceCliente
import com.proyecto.proyectoesfrt.service.ApiServiceDoctores
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DoctorDetalleActivity : AppCompatActivity() {

    private lateinit var txtCodigoDoctorActualizar:TextView
    private lateinit var txtNombreDoctoresActualizar:TextView
    private lateinit var txtDoctoresApellidosActualizar:TextView
    private lateinit var txtDNIDoctoresActualizar:TextView
    private lateinit var spnEspecialidadDocActualizar:AutoCompleteTextView
    private lateinit var spnExperienciaDocActualizar: AutoCompleteTextView
    private lateinit var btnActualizarDoctor:Button
    private lateinit var btnBorrarDoctor:Button
    private lateinit var btnRegresarDoctor:Button

    //Api
    private lateinit var api:ApiServiceDoctores

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.doctores_detalles_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtCodigoDoctorActualizar = findViewById(R.id.txtCodigoDoctorActualizar)
        txtNombreDoctoresActualizar = findViewById(R.id.txtNombreDoctoresActualizar)
        txtDoctoresApellidosActualizar = findViewById(R.id.txtDoctoresApellidosActualizar)
        txtDNIDoctoresActualizar = findViewById(R.id.txtDNIDoctoresActualizar)
        spnEspecialidadDocActualizar = findViewById(R.id.spnEspecialidadDocActualizar)
        spnExperienciaDocActualizar = findViewById(R.id.spnExperienciaDocActualizar)
        btnActualizarDoctor=findViewById(R.id.btnActualizarDoctor)
        btnBorrarDoctor = findViewById(R.id.btnBorrarDoctor)
        btnRegresarDoctor = findViewById(R.id.btnRegresarDoctor)

        //Api

        api = ApiUtils.getApiDoctor()

        //Obtener datos del Intent

        obtenerDatos()

        btnRegresarDoctor.setOnClickListener {
            regresarDoctor()
        }


        btnActualizarDoctor.setOnClickListener {
            actualizarDoctor()
        }
        btnBorrarDoctor.setOnClickListener {
            eliminarDoctor()
        }

    }

    private fun regresarDoctor(){
        var intent= Intent(this,ListaDoctoresActivity::class.java)
        startActivity(intent)
    }

    private fun obtenerDatos() {
        val codigoDoctor = intent.getIntExtra("CODIGO_DOCTOR", 0)
        val nombreDoctor = intent.getStringExtra("NOMBRE_DOCTOR") ?: ""
        val apellidosDoctor = intent.getStringExtra("APELLIDO_DOCTOR") ?: ""
        val dniDoctor = intent.getIntExtra("DNI_DOCTOR", 0)
        val especialidadDoctor = intent.getStringExtra("ESPECIALIDAD_DOCTOR") ?: ""
        val experienciaDoctor = intent.getStringExtra("EXPERIENCIA_DOCTOR") ?: ""


        // Asigna los datos a las vistas
        txtCodigoDoctorActualizar.text = codigoDoctor.toString()
        txtNombreDoctoresActualizar.text = nombreDoctor
        txtDoctoresApellidosActualizar.text = apellidosDoctor
        txtDNIDoctoresActualizar.text = dniDoctor.toString()
        spnEspecialidadDocActualizar.setText(especialidadDoctor)
        spnExperienciaDocActualizar.setText(experienciaDoctor)

        Log.d("DoctorDetalleActivity", "Código: $codigoDoctor, Nombre: $nombreDoctor, Apellidos: $apellidosDoctor")

    }

    private fun actualizarDoctor() {
        // Recoge los datos de las vistas
        val codigoDoctor = txtCodigoDoctorActualizar.text.toString().toInt()
        val nombreDoctor = txtNombreDoctoresActualizar.text.toString()
        val apellidosDoctor = txtDoctoresApellidosActualizar.text.toString()
        val dniDoctor = txtDNIDoctoresActualizar.text.toString().toInt()
        val especialidadDoctor = spnEspecialidadDocActualizar.text.toString()
        val experienciaDoctor = spnExperienciaDocActualizar.text.toString()

        // Crea un objeto Doctor con los nuevos datos
        val doctorActualizado = Doctor(codigoDoctor, nombreDoctor, apellidosDoctor, dniDoctor, especialidadDoctor, experienciaDoctor)

        // Llama a tu API para actualizar el doctor
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.actualizarDoctor(codigoDoctor, doctorActualizado) // Aquí pasamos el código del doctor
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(this@DoctorDetalleActivity, "Doctor actualizado con éxito", Toast.LENGTH_SHORT).show()
                    regresarDoctor() // Vuelve a la lista de doctores
                } else {
                    Toast.makeText(this@DoctorDetalleActivity, "Error al actualizar el doctor", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun eliminarDoctor() {
        val codigoDoctor = txtCodigoDoctorActualizar.text.toString().toInt()

        // Llama a tu API para eliminar el doctor
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.eliminarDoctor(codigoDoctor) // Asegúrate de que tienes este método en tu ApiServiceDoctores
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(this@DoctorDetalleActivity, "Doctor eliminado con éxito", Toast.LENGTH_SHORT).show()
                    regresarDoctor() // Vuelve a la lista de doctores
                } else {
                    Toast.makeText(this@DoctorDetalleActivity, "Error al eliminar el doctor", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }







}