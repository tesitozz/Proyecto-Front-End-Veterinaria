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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistorialDetalleActivity : AppCompatActivity() {

    private lateinit var historialClinica: HistorialClinica

    // Vistas de la actividad
    private lateinit var txtFechaRegistoHistorialDetalles: TextView
    private lateinit var txtHoraRegistroHistorialDetalles: TextView
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

        // Cargar los datos en los campos
        cargarDatos()

        btnEliminarHistorial.setOnClickListener { eliminarHistorial() }
    }

    private fun cargarDatos() {
        // Asignar los valores de historialClinica a los campos
        txtFechaRegistoHistorialDetalles.text = historialClinica.fechaDeRegistro.toString()
        txtHoraRegistroHistorialDetalles.text = historialClinica.horaDeRegistro.toString()

        // Cargar los datos del cliente
        val cliente = historialClinica.cliente
        txtApellidoDuenioRegistroHistorialDetalles.text = "${cliente.nombres} ${cliente.apellidos}"
        txtCorreoDuenioRegistroHistorialDetalles.text = cliente.correo ?: "No disponible"
        txtDniDuenoRegistroHistorialDetalles.text = cliente.dni
        txtTelefonoDuenioRegistroHistorialDetalles.text = cliente.celular
        txtDireccionDuenoRegistroHistorialDetalles.text = cliente.direccion ?: "No disponible"

        // Cargar los datos del animal
        val animal = historialClinica.animal
        txtRazaAnimalRegistroHistorialDetalles.text = animal.raza ?: "No disponible"
        txtColorAnimalRegistroHistorialDetallesDetalles.text = animal.color ?: "No disponible"
        txtEspecieAnimalRegistroHistorialDetalles.text = animal.tipo
        txtPesoAnimalRegistroHistorialDetalles.text = animal.peso.toString()

        // Cargar los datos del médico
        val medico = historialClinica.medico
        txtApellidosDoctorDetalles.text = "${medico.nombres} ${medico.apellidos}"
    }


    private fun eliminarHistorial() {
        // Confirmar la eliminación
        AlertDialog.Builder(this)
            .setMessage("¿Estás seguro de que deseas eliminar este historial?")
            .setPositiveButton("Sí") { dialog, _ ->
                val apiService = RetrofitClient.apiServiceHistorial // Usar el servicio de historial directamente desde RetrofitClient
                val historialId = historialClinica.id
                if (historialId != null) {
                    val call = apiService.deleteHistoriaClinica(historialId)

                    call.enqueue(object : retrofit2.Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@HistorialDetalleActivity, "Historial eliminado correctamente", Toast.LENGTH_SHORT).show()
                                finish() // Cerrar la actividad después de eliminar
                            } else {
                                Toast.makeText(this@HistorialDetalleActivity, "Error al eliminar el historial", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(this@HistorialDetalleActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(this@HistorialDetalleActivity, "ID de historial no válido", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}
