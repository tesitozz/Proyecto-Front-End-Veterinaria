package com.proyecto.proyectoesfrt

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.lifecycle.lifecycleScope
import com.example.appproyecto.utils.ApiUtils
import com.google.android.material.textfield.TextInputEditText
import com.proyecto.proyectoesfrt.entidad.Animal
import com.proyecto.proyectoesfrt.entidad.Cliente
import com.proyecto.proyectoesfrt.entidad.HistorialClinica
import com.proyecto.proyectoesfrt.entidad.Medico
import com.proyecto.proyectoesfrt.service.ApiServiceMedicos
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class AgregarHistorialActivity : AppCompatActivity() {

    private lateinit var txtFechaRegistoHistorial: TextInputEditText
    private lateinit var txtHoraRegistroHistorial: TextInputEditText

    // Duenio
    private lateinit var spnDuenioListarHistorial: AutoCompleteTextView
    private lateinit var txtApellidoDuenioRegistroHistorial: TextView
    private lateinit var txtDniDuenoRegistroHistorial: TextView
    private lateinit var txtCorreoDuenioRegistroHistorial: TextView
    private lateinit var txtTelefonoDuenioRegistroHistorial: TextView
    private lateinit var txtDireccionDuenoRegistroHistorial: TextView

    // Animal
    private lateinit var spnAnimalRegistrar: AutoCompleteTextView
    private lateinit var txtTipoAnimalRegistrar: TextView
    private lateinit var txtGeneroAnimalHistorialRegistrar: TextView
    private lateinit var txtEdadAnimalRegistrarHistorial: TextView
    private lateinit var txtPesoAnimalHistorial: TextView
    private lateinit var txtRazaAnimalRegistrarHistorial: TextView
    private lateinit var txtColorAnimalRegistrar: TextView

    // Doctor
    private lateinit var spnMedicoHistorialRegistrar: AutoCompleteTextView
    private lateinit var txtApellidoMedicoHistorial: TextView

    // Botones
    private lateinit var btnRegistrarHistorialMedico: Button
    private lateinit var btnVolverRegistrarHistorial: Button

    // Variables para almacenar los datos obtenidos
    private var dueniosList = mutableListOf<Cliente>()
    private var animalesList = mutableListOf<Animal>()
    private var medicosList = mutableListOf<Medico>()

    // API
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.historial_agregar_main)

        // Inicialización de vistas
        txtFechaRegistoHistorial = findViewById(R.id.txtFechaRegistoHistorial)
        txtHoraRegistroHistorial = findViewById(R.id.txtHoraRegistroHistorial)

        spnDuenioListarHistorial = findViewById(R.id.spnDuenioListarHistorial)
        txtApellidoDuenioRegistroHistorial = findViewById(R.id.txtApellidoDuenioRegistroHistorial)
        txtDniDuenoRegistroHistorial = findViewById(R.id.txtDniDuenoRegistroHistorial)
        txtCorreoDuenioRegistroHistorial = findViewById(R.id.txtCorreoDuenioRegistroHistorial)
        txtTelefonoDuenioRegistroHistorial = findViewById(R.id.txtTelefonoDuenioRegistroHistorial)
        txtDireccionDuenoRegistroHistorial = findViewById(R.id.txtDireccionDuenoRegistroHistorial)

        spnAnimalRegistrar = findViewById(R.id.spnAnimalRegistrar)
        txtTipoAnimalRegistrar = findViewById(R.id.txtTipoAnimalRegistrar)
        txtGeneroAnimalHistorialRegistrar = findViewById(R.id.txtGeneroAnimalHistorialRegistrar)
        txtEdadAnimalRegistrarHistorial = findViewById(R.id.txtEdadAnimalRegistrarHistorial)
        txtPesoAnimalHistorial = findViewById(R.id.txtPesoAnimalHistorial)
        txtRazaAnimalRegistrarHistorial = findViewById(R.id.txtRazaAnimalRegistrarHistorial)
        txtColorAnimalRegistrar = findViewById(R.id.txtColorAnimalRegistrar)

        spnMedicoHistorialRegistrar = findViewById(R.id.spnMedicoHistorialRegistrar)
        txtApellidoMedicoHistorial = findViewById(R.id.txtApellidoMedicoHistorial)

        btnRegistrarHistorialMedico = findViewById(R.id.btnRegistrarHistorialMedico)
        btnVolverRegistrarHistorial = findViewById(R.id.btnVolverRegistrarHistorial)

        btnRegistrarHistorialMedico.setOnClickListener { registrarHistorialMedico() }
        btnVolverRegistrarHistorial.setOnClickListener { volverHistorial() }

        // Llamadas a las APIs para obtener los datos
        obtenerDatos()

        // Configurar el AutoCompleteTextView
        spnDuenioListarHistorial.setOnItemClickListener { parent, view, position, id ->
            val duenioSeleccionado = dueniosList[position]
            txtApellidoDuenioRegistroHistorial.text = duenioSeleccionado.apellidos
            txtDniDuenoRegistroHistorial.text = duenioSeleccionado.dni
            txtCorreoDuenioRegistroHistorial.text = duenioSeleccionado.correo
            txtTelefonoDuenioRegistroHistorial.text = duenioSeleccionado.celular
            txtDireccionDuenoRegistroHistorial.text = duenioSeleccionado.direccion
        }

        spnAnimalRegistrar.setOnItemClickListener { parent, view, position, id ->
            val animalSeleccionado = animalesList[position]
            txtTipoAnimalRegistrar.text = animalSeleccionado.tipo
            txtGeneroAnimalHistorialRegistrar.text = animalSeleccionado.genero
            txtEdadAnimalRegistrarHistorial.text = animalSeleccionado.edad.toString()
            txtRazaAnimalRegistrarHistorial.text = animalSeleccionado.raza
            txtPesoAnimalHistorial.text = animalSeleccionado.peso.toString()
            txtColorAnimalRegistrar.text = animalSeleccionado.color
        }

        spnMedicoHistorialRegistrar.setOnItemClickListener { parent, view, position, id ->
            val medicoSeleccionado = medicosList[position]
            txtApellidoMedicoHistorial.text = medicoSeleccionado.apellidos
        }

        txtFechaRegistoHistorial.setOnClickListener {
            fechaRegistroHistorial()
        }

        txtHoraRegistroHistorial.setOnClickListener {
            horaRegistroHistorial()
        }

    }

    // La función que maneja la selección de la fecha
    fun fechaRegistroHistorial() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { view, selectedYear, selectedMonth, selectedDay ->
                // Formato de fecha: YYYY-MM-DD
                val fechaSeleccionada = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                txtFechaRegistoHistorial.setText(fechaSeleccionada) // Mostrar la fecha seleccionada
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    // La función que maneja la selección de la hora
    fun horaRegistroHistorial() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { view, selectedHour, selectedMinute ->
                // Formato de hora: HH:MM
                val horaSeleccionada = String.format("%02d:%02d", selectedHour, selectedMinute)
                txtHoraRegistroHistorial.setText(horaSeleccionada) // Mostrar la hora seleccionada
            },
            hour, minute, true // true para formato de 24 horas
        )
        timePickerDialog.show()
    }


    private fun obtenerDatos() {
        lifecycleScope.launch {
            try {
                // Llamadas a las APIs para obtener las listas de datos
                val responseDuenios = ApiUtils.getApiCliente().getAllClientes()
                val responseAnimales = ApiUtils.getApiAnimal().getAllAnimales()
                val responseMedicos = ApiUtils.getApiDoctor().getAllMedicos()

                // Verificar si las respuestas fueron exitosas
                if (responseDuenios.isSuccessful && responseAnimales.isSuccessful && responseMedicos.isSuccessful) {
                    val dueniosResponse = responseDuenios.body()
                    val animalesResponse = responseAnimales.body()
                    val medicosResponse = responseMedicos.body()

                    // Verifica que las respuestas no sean nulas
                    if (dueniosResponse != null && animalesResponse != null && medicosResponse != null) {
                        dueniosList = dueniosResponse.toMutableList()
                        animalesList = animalesResponse.toMutableList()
                        medicosList = medicosResponse.toMutableList()

                        // Configurar los adaptadores para cada spinner
                        val duenioAdapter = ArrayAdapter(
                            this@AgregarHistorialActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            dueniosList.map { it.nombres } // Ajusta el campo que deseas mostrar
                        )
                        spnDuenioListarHistorial.setAdapter(duenioAdapter)

                        val animalAdapter = ArrayAdapter(
                            this@AgregarHistorialActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            animalesList.map { it.nombre } // Ajusta el campo que deseas mostrar
                        )
                        spnAnimalRegistrar.setAdapter(animalAdapter)

                        val medicoAdapter = ArrayAdapter(
                            this@AgregarHistorialActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            medicosList.map { it.nombres } // Ajusta el campo que deseas mostrar
                        )
                        spnMedicoHistorialRegistrar.setAdapter(medicoAdapter)

                    } else {
                        Toast.makeText(this@AgregarHistorialActivity, "Error: Datos incompletos", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@AgregarHistorialActivity, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AgregarHistorialActivity, "Excepción al obtener datos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun registrarHistorialMedico() {
        // Obtener los datos del formulario
        val fecha = txtFechaRegistoHistorial.text.toString()
        val hora = txtHoraRegistroHistorial.text.toString()

        // Validar que los campos no estén vacíos
        if (fecha.isEmpty() || hora.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Convertir la fecha a LocalDate
        val localDate = try {
            LocalDate.parse(fecha) // Si la fecha tiene el formato adecuado: "YYYY-MM-DD"
        } catch (e: Exception) {
            Toast.makeText(this, "Formato de fecha no válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Convertir la hora a LocalTime
        val localTime = try {
            // Si la hora tiene el formato adecuado: "HH:mm", se le agregan los segundos automáticamente.
            if (hora.length == 5) {
                LocalTime.parse(hora + ":00") // Añadimos los segundos automáticamente
            } else {
                LocalTime.parse(hora) // Si ya tiene el formato "HH:mm:ss", la parseamos directamente
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Formato de hora no válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Convertir LocalDate a String (si se requiere un String en lugar de LocalDate)
        val fechaString = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        // Obtener el cliente, animal y médico seleccionado
        val duenioSeleccionado = spnDuenioListarHistorial.text.toString()
        val animalSeleccionado = spnAnimalRegistrar.text.toString()
        val medicoSeleccionado = spnMedicoHistorialRegistrar.text.toString()

        val duenio = dueniosList.find { it.nombres == duenioSeleccionado }
        val animal = animalesList.find { it.nombre == animalSeleccionado }
        val medico = medicosList.find { it.nombres == medicoSeleccionado }

        if (duenio == null || animal == null || medico == null) {
            Toast.makeText(this, "Selecciona un dueño, un animal y un médico válidos", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear el objeto HistorialClinica con fechaString
        val historialClinica = HistorialClinica(
            fechaDeRegistro = localDate,
            horaDeRegistro = localTime,
            animal = animal,
            cliente = duenio,
            medico = medico
        )

        // Llamar a la API para registrar el historial clínico
        ApiUtils.getApiHistorial().createHistoriaClinica(historialClinica).enqueue(object : retrofit2.Callback<HistorialClinica> {
            override fun onResponse(call: retrofit2.Call<HistorialClinica>, response: retrofit2.Response<HistorialClinica>) {
                if (response.isSuccessful) {
                    // Si la respuesta es exitosa
                    Toast.makeText(this@AgregarHistorialActivity, "Historial clínico registrado correctamente", Toast.LENGTH_SHORT).show()
                    finish() // O navegar a otra actividad
                } else {
                    // Si la respuesta no fue exitosa
                    Toast.makeText(this@AgregarHistorialActivity, "Error al registrar historial clínico", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<HistorialClinica>, t: Throwable) {
                // En caso de error en la llamada
                Toast.makeText(this@AgregarHistorialActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun volverHistorial() {
        finish()
    }
}
