package com.proyecto.proyectoesfrt

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.proyecto.proyectoesfrt.adaptador.HistorialAdapter
import com.proyecto.proyectoesfrt.api.RetrofitClient
import com.proyecto.proyectoesfrt.entidad.HistorialClinica
import com.proyecto.proyectoesfrt.service.ApiServiceHistorial
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalTime

class HistorialDetalleActivity : AppCompatActivity() {

    private lateinit var historialClinica: HistorialClinica

    // Vistas de la actividad
    private lateinit var txtFechaRegistoHistorialDetalles: TextView
    private lateinit var txtHoraRegistroHistorialDetalles: TextView
    private lateinit var txtDiagnosticoHistorialDetalle : TextView
    private lateinit var txtTratamientoHistorialDetalle: TextView
    private lateinit var txtVacunasAplicadasHistorialDetalleDetalle: TextView
    private lateinit var txtObservacionesHistorialDetalle: TextView
    private lateinit var spnDuenioListarHistorialModificar: AutoCompleteTextView
    private lateinit var txtApellidoDuenioRegistroHistorialDetalles: TextView
    private lateinit var txtCorreoDuenioRegistroHistorialDetalles: TextView
    private lateinit var txtDniDuenoRegistroHistorialDetalles: TextView
    private lateinit var txtTelefonoDuenioRegistroHistorialDetalles: TextView
    private lateinit var txtDireccionDuenoRegistroHistorialDetalles: TextView
    private lateinit var spnAnimalRegistrarModificar: AutoCompleteTextView
    private lateinit var txtRazaAnimalRegistroHistorialDetalles: TextView
    private lateinit var txtColorAnimalRegistroHistorialDetallesDetalles: TextView
    private lateinit var txtEspecieAnimalRegistroHistorialDetalles: TextView
    private lateinit var txtPesoAnimalRegistroHistorialDetalles: TextView
    private lateinit var spnDoctorDetalleModificar: AutoCompleteTextView
    private lateinit var txtApellidosDoctorDetalles: TextView

    private lateinit var btnModificarRegistroHistorial: Button
    private lateinit var btnEliminarHistorial: Button
    private lateinit var btnRegresar: Button

    private lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiServiceHistorial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.historial_detalles_main)

        // Recuperar el objeto "historial" pasado a través del Intent
        historialClinica = intent.getSerializableExtra("historial") as HistorialClinica

        // Configurar la vista
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialización de las vistas
        txtFechaRegistoHistorialDetalles = findViewById(R.id.txtFechaRegistoHistorialDetalles)
        txtHoraRegistroHistorialDetalles = findViewById(R.id.txtHoraRegistroHistorialDetalles)
        txtDiagnosticoHistorialDetalle = findViewById(R.id.txtDiagnosticoHistorialDetalle)
        txtTratamientoHistorialDetalle = findViewById(R.id.txtTratamientoHistorialDetalle)
        txtVacunasAplicadasHistorialDetalleDetalle = findViewById(R.id.txtVacunasAplicadasHistorialDetalleDetalle)
        txtObservacionesHistorialDetalle = findViewById(R.id.txtObservacionesHistorialDetalle)
        spnDuenioListarHistorialModificar = findViewById(R.id.spnDuenioListarHistorialModificar)
        txtApellidoDuenioRegistroHistorialDetalles = findViewById(R.id.txtApellidoDuenioRegistroHistorialDetalles)
        txtCorreoDuenioRegistroHistorialDetalles = findViewById(R.id.txtCorreoDuenioRegistroHistorialDetalles)
        txtDniDuenoRegistroHistorialDetalles = findViewById(R.id.txtDniDuenoRegistroHistorialDetalles)
        txtTelefonoDuenioRegistroHistorialDetalles = findViewById(R.id.txtTelefonoDuenioRegistroHistorialDetalles)
        txtDireccionDuenoRegistroHistorialDetalles = findViewById(R.id.txtDireccionDuenoRegistroHistorialDetalles)
        spnAnimalRegistrarModificar = findViewById(R.id.spnAnimalRegistrarModificarDetalles)
        txtRazaAnimalRegistroHistorialDetalles = findViewById(R.id.txtRazaAnimalRegistroHistorialDetalles)
        txtColorAnimalRegistroHistorialDetallesDetalles = findViewById(R.id.txtColorAnimalRegistroHistorialDetallesDetalles)
        txtEspecieAnimalRegistroHistorialDetalles = findViewById(R.id.txtEspecieAnimalRegistroHistorialDetalles)
        txtPesoAnimalRegistroHistorialDetalles = findViewById(R.id.txtPesoAnimalRegistroHistorialDetalles)
        spnDoctorDetalleModificar = findViewById(R.id.spnDoctorDetalleModificar)
        txtApellidosDoctorDetalles = findViewById(R.id.txtApellidosDoctorDetalles)

        btnModificarRegistroHistorial = findViewById(R.id.btnModificarRegistroHistorial)
        btnEliminarHistorial = findViewById(R.id.btnEliminarHistorial)
        btnRegresar = findViewById(R.id.btnRegresar)

        // Llenar los campos con los datos del historial

        apiService = retrofit.create(ApiServiceHistorial::class.java)


    }

    // Función para llenar los campos con los datos de historial




}