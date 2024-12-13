package com.proyecto.proyectoesfrt

import android.content.Intent
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
import com.example.appproyecto.utils.ApiUtils
import com.proyecto.proyectoesfrt.entidad.Doctor
import com.proyecto.proyectoesfrt.service.ApiServiceDoctores
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DoctorDetalleActivity : AppCompatActivity() {

    private lateinit var txtCodigoDoctorActualizar: TextView
    private lateinit var txtNombreDoctoresActualizar: TextView
    private lateinit var txtDoctoresApellidosActualizar: TextView
    private lateinit var txtDNIDoctoresActualizar: TextView
    private lateinit var spnEspecialidadDocActualizar: AutoCompleteTextView
    private lateinit var spnExperienciaDocActualizar: AutoCompleteTextView
    private lateinit var btnActualizarDoctor: Button
    private lateinit var btnBorrarDoctor: Button
    private lateinit var btnRegresarDoctor: Button

    // APIs
    private lateinit var api: ApiServiceDoctores

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.doctores_detalles_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialización de las vistas
        txtCodigoDoctorActualizar = findViewById(R.id.txtCodigoDoctorActualizar)
        txtNombreDoctoresActualizar = findViewById(R.id.txtNombreDoctoresActualizar)
        txtDoctoresApellidosActualizar = findViewById(R.id.txtDoctoresApellidosActualizar)
        txtDNIDoctoresActualizar = findViewById(R.id.txtDNIDoctoresActualizar)
        spnEspecialidadDocActualizar = findViewById(R.id.spnEspecialidadDocActualizar)
        spnExperienciaDocActualizar = findViewById(R.id.spnExperienciaDocActualizar)
        btnActualizarDoctor = findViewById(R.id.btnActualizarDoctor)
        btnBorrarDoctor = findViewById(R.id.btnBorrarDoctor)
        btnRegresarDoctor = findViewById(R.id.btnRegresarDoctor)

        // Configuración del API
        api = ApiUtils.getApiDoctor()

        // Obtener datos del Intent
        obtenerDatos()

        // Configuración de los AutoCompleteTextViews con los arrays de recursos
        configurarAutoCompleteTextViews()

        // Eventos de los botones
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

    private fun configurarAutoCompleteTextViews() {
        // Configurar AutoCompleteTextView para Especialidad
        val opcionesEspecialidad = resources.getStringArray(R.array.item_doctores)
        val adapterEspecialidad = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, opcionesEspecialidad)
        spnEspecialidadDocActualizar.setAdapter(adapterEspecialidad)

        // Configurar AutoCompleteTextView para Experiencia
        val opcionesExperiencia = resources.getStringArray(R.array.item_experiencia)
        val adapterExperiencia = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, opcionesExperiencia)
        spnExperienciaDocActualizar.setAdapter(adapterExperiencia)
    }

    private fun regresarDoctor() {
        val intent = Intent(this, ListaDoctoresActivity::class.java)
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

    //ACTUALIZAR DOCTOR
    private fun actualizarDoctor() {
        // Validaciones de los datos ingresados
        val codigoDoctor = txtCodigoDoctorActualizar.text.toString()
        if (codigoDoctor.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese el código del doctor", Toast.LENGTH_SHORT).show()
            return
        }

        val nombreDoctor = txtNombreDoctoresActualizar.text.toString()
        if (nombreDoctor.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese el nombre del doctor", Toast.LENGTH_SHORT).show()
            return
        }

        val apellidosDoctor = txtDoctoresApellidosActualizar.text.toString()
        if (apellidosDoctor.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese los apellidos del doctor", Toast.LENGTH_SHORT).show()
            return
        }

        val dniDoctor = txtDNIDoctoresActualizar.text.toString()
        if (dniDoctor.isEmpty() || dniDoctor.length != 8 || !dniDoctor.matches(Regex("\\d{8}"))) {
            Toast.makeText(this, "Por favor, ingrese un DNI válido (exactamente 8 dígitos numéricos)", Toast.LENGTH_SHORT).show()
            return
        }

        val especialidadDoctor = spnEspecialidadDocActualizar.text.toString()
        if (especialidadDoctor.isEmpty()) {
            Toast.makeText(this, "Por favor, seleccione una especialidad", Toast.LENGTH_SHORT).show()
            return
        }

        val experienciaDoctor = spnExperienciaDocActualizar.text.toString()
        if (experienciaDoctor.isEmpty()) {
            Toast.makeText(this, "Por favor, seleccione la experiencia del doctor", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear un objeto Doctor con los datos validados
        val doctorActualizado = Doctor(
            codigoDoctor = codigoDoctor.toInt(),
            nombreDoctor = nombreDoctor,
            apellidoDoctor = apellidosDoctor,
            dniDoctor = dniDoctor.toInt(),
            especialidad = especialidadDoctor,
            experiencia = experienciaDoctor
        )

        // Llamar a la API para actualizar el doctor
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.actualizarDoctor(codigoDoctor.toInt(), doctorActualizado)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    // Mostrar un mensaje atractivo al usuario
                    AlertDialog.Builder(this@DoctorDetalleActivity)
                        .setTitle("¡Éxito!")
                        .setMessage("El doctor ha sido actualizado correctamente.")
                        .setPositiveButton("Aceptar") { _, _ ->
                            regresarDoctor() // Vuelve a la lista de doctores
                        }
                        .show()
                } else {
                    // Mostrar un mensaje de error en caso de falla
                    AlertDialog.Builder(this@DoctorDetalleActivity)
                        .setTitle("Error")
                        .setMessage("Hubo un problema al actualizar el doctor. Intenta nuevamente.\n${response.message()}")
                        .setPositiveButton("Aceptar", null)
                        .show()
                }
            }
        }
    }


    private fun eliminarDoctor() {
        val codigoDoctor = txtCodigoDoctorActualizar.text.toString()

        // Validar que el campo no esté vacío
        if (codigoDoctor.isEmpty()) {
            Toast.makeText(this@DoctorDetalleActivity, "Por favor, ingrese el código del doctor", Toast.LENGTH_SHORT).show()
            return
        }

        // Mostrar un cuadro de diálogo de confirmación
        AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este doctor?")
            .setPositiveButton("Sí") { _, _ ->
                // Proceder con la eliminación si el usuario confirma
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // Convertir el código de doctor a un número entero
                        val codigoDoctorInt = codigoDoctor.toInt()

                        // Llamar a tu API para eliminar el doctor
                        val response = api.eliminarDoctor(codigoDoctorInt)
                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@DoctorDetalleActivity, "Doctor eliminado con éxito", Toast.LENGTH_SHORT).show()
                                setResult(RESULT_OK)
                                regresarDoctor() // Vuelve a la lista de doctores
                            } else {
                                Toast.makeText(this@DoctorDetalleActivity, "Error al eliminar el doctor", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: NumberFormatException) {
                        // Captura el error si el código no es un número válido
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@DoctorDetalleActivity, "Código inválido", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        // Captura otros errores
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@DoctorDetalleActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("No", null) // No hacer nada si el usuario cancela
            .show()
    }

}
