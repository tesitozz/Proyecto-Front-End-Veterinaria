package com.proyecto.proyectoesfrt

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appproyecto.utils.ApiUtils
import com.proyecto.proyectoesfrt.entidad.Medico
import com.proyecto.proyectoesfrt.service.ApiServiceMedicos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.hibernate.query.sqm.sql.FromClauseIndex
import retrofit2.Response

class DoctorDetalleActivity : AppCompatActivity() {

    private lateinit var txtCodigoDoctorActualizar: TextView
    private lateinit var txtNombreDoctoresActualizar: TextView
    private lateinit var txtDoctoresApellidosActualizar: TextView
    private lateinit var txtDNIDoctoresActualizar: TextView
    private lateinit var spnEspecialidadDocActualizar: AutoCompleteTextView
    private lateinit var btnActualizarDoctor: Button
    private lateinit var btnBorrarDoctor: Button
    private lateinit var btnRegresarDoctor: Button

    private lateinit var api: ApiServiceMedicos
    private var doctorId: Long = 0L

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
        btnActualizarDoctor = findViewById(R.id.btnActualizarDoctor)
        btnBorrarDoctor = findViewById(R.id.btnBorrarDoctor)
        btnRegresarDoctor = findViewById(R.id.btnRegresarDoctor)

        api = ApiUtils.getApiDoctor()

        // Configuración del spinner
        val especialidades = resources.getStringArray(R.array.item_especialidadMedico)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, especialidades)
        spnEspecialidadDocActualizar.setAdapter(adapter)

        // Cargar datos recibidos en el Intent
        cargarDatosDesdeIntent()

        btnActualizarDoctor.setOnClickListener { modificarDoctor() }
        btnBorrarDoctor.setOnClickListener { borrarDoctor() }
        btnRegresarDoctor.setOnClickListener { regresarDoctor() }
    }

    private fun cargarDatosDesdeIntent() {
        intent?.let {
            doctorId = it.getLongExtra("id", 0L)
            txtCodigoDoctorActualizar.text = doctorId.toString()
            txtNombreDoctoresActualizar.text = it.getStringExtra("nombres") ?: ""
            txtDoctoresApellidosActualizar.text = it.getStringExtra("apellidos") ?: ""
            txtDNIDoctoresActualizar.text = it.getStringExtra("dni") ?: ""
            spnEspecialidadDocActualizar.setText(it.getStringExtra("especialidad") ?: "", false)
        }
    }

    private fun modificarDoctor() {
        val nombres = txtNombreDoctoresActualizar.text.toString().trim()
        val apellidos = txtDoctoresApellidosActualizar.text.toString().trim()
        val dni = txtDNIDoctoresActualizar.text.toString().trim()
        val especialidad = spnEspecialidadDocActualizar.text.toString().trim()

        if (nombres.isEmpty() || apellidos.isEmpty() || dni.isEmpty() || especialidad.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val medico = Medico(doctorId, nombres, apellidos, dni, especialidad)
                val response: Response<Medico> = withContext(Dispatchers.IO) {
                    api.updateMedico(medico)
                }

                if (response.isSuccessful) {
                    Toast.makeText(this@DoctorDetalleActivity, "Doctor actualizado con éxito.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@DoctorDetalleActivity, "Error al actualizar el doctor.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DoctorDetalleActivity, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun borrarDoctor() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response: Response<Void> = withContext(Dispatchers.IO) {
                    api.deleteMedico(doctorId)
                }

                if (response.isSuccessful) {
                    Toast.makeText(this@DoctorDetalleActivity, "Doctor eliminado con éxito.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@DoctorDetalleActivity, "Error al eliminar el doctor.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DoctorDetalleActivity, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun regresarDoctor() {
        val intent = Intent(this, ListaDoctoresActivity::class.java)
        startActivity(intent)
        finish()
    }
}
