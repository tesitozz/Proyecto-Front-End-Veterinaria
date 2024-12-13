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
import com.proyecto.proyectoesfrt.entidad.Cliente
import com.proyecto.proyectoesfrt.service.ApiServiceCliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ClienteDetalleActivity : AppCompatActivity() {

    private lateinit var txtCodigoCliente: TextView
    private lateinit var txtNombreCliente: TextView
    private lateinit var txtClienteApellidos: TextView
    private lateinit var txtDNICliente: TextView
    private lateinit var txtDireccionCliente: TextView
    private lateinit var spnGeneroCliente: AutoCompleteTextView // Cambiar a AutoCompleteTextView
    private lateinit var btnRegresarCliente: Button
    private lateinit var btnActualizarCliente: Button
    private lateinit var btnEliminarCliente: Button

    //Apis
    private lateinit var api: ApiServiceCliente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.clientes_detalles_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar vistas
        txtCodigoCliente = findViewById(R.id.txtCodigoClienteActualizar)
        txtNombreCliente = findViewById(R.id.txtNombreClienteActualizar)
        txtClienteApellidos = findViewById(R.id.txtClienteApellidosActualizar)
        txtDNICliente = findViewById(R.id.txtDNIClienteActualizar)
        txtDireccionCliente = findViewById(R.id.txtDireccionClienteActualizar)
        spnGeneroCliente = findViewById(R.id.spnGeneroClienteActualizar) // Asegúrate de que el ID sea correcto
        btnRegresarCliente = findViewById(R.id.btnRegresarCliente)
        btnActualizarCliente = findViewById(R.id.btnActualizarCliente)
        btnEliminarCliente = findViewById(R.id.btnEliminarCliente)

        // Inicializar API
        api = ApiUtils.getApiCliente()

        // Obtener datos del Intent
        obtenerDatos()

        // Setear listeners de botones
        btnRegresarCliente.setOnClickListener {
            regresarCliente()
        }

        btnActualizarCliente.setOnClickListener {
            actualizarCliente()
        }

        btnEliminarCliente.setOnClickListener {
            eliminarCliente()
        }
    }

    private fun regresarCliente() {
        val intent = Intent(this, ListaClientesActivity::class.java)
        startActivity(intent)
    }

    private fun obtenerDatos() {
        // Obtener datos del Intent
        val codigoCliente = intent.getIntExtra("CODIGO_CLIENTE", 0)
        val nombreCliente = intent.getStringExtra("NOMBRE_CLIENTE") ?: "N/A"
        val apellidosCliente = intent.getStringExtra("APELLIDOS_CLIENTE") ?: "N/A"
        val dniCliente = intent.getIntExtra("DNI_CLIENTE", 0).toString()
        val generoCliente = intent.getStringExtra("GENERO_CLIE") ?: "N/A"
        val informacionCliente = intent.getStringExtra("INFORMACION_CLIENTE") ?: "N/A"

        // Rellenar los campos de texto
        txtCodigoCliente.text = codigoCliente.toString()
        txtNombreCliente.text = nombreCliente
        txtClienteApellidos.text = apellidosCliente
        txtDNICliente.text = dniCliente
        txtDireccionCliente.text = informacionCliente

        // Configurar el AutoCompleteTextView (en lugar de Spinner)
        val opcionesGenero = resources.getStringArray(R.array.clientes_items)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, opcionesGenero)
        spnGeneroCliente.setAdapter(adapter) // Configuramos el adaptador del AutoCompleteTextView

        // Seleccionar el valor actual del género
        val posicionGenero = opcionesGenero.indexOf(generoCliente)
        if (posicionGenero >= 0) {
            spnGeneroCliente.setText(generoCliente, false) // Establecer el valor seleccionado
        } else {
            spnGeneroCliente.setText(opcionesGenero[0], false) // Default a la primera opción si no coincide
        }

        // Registro para depuración
        Log.d("ClienteDetalleActivity", "Código: $codigoCliente, Nombre: $nombreCliente, Apellidos: $apellidosCliente, Género: $generoCliente")
    }

    //ACTUALIZAR CLIENTE
    private fun actualizarCliente() {
        // Validaciones en los datos del cliente
        val codigoCliente = txtCodigoCliente.text.toString()
        if (codigoCliente.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese el código del cliente", Toast.LENGTH_SHORT).show()
            return
        }

        val nombreCliente = txtNombreCliente.text.toString()
        if (nombreCliente.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese el nombre del cliente", Toast.LENGTH_SHORT).show()
            return
        }

        val apellidosCliente = txtClienteApellidos.text.toString()
        if (apellidosCliente.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese los apellidos del cliente", Toast.LENGTH_SHORT).show()
            return
        }

        val dniCliente = txtDNICliente.text.toString()
        if (dniCliente.isEmpty() || dniCliente.length != 8 || !dniCliente.matches(Regex("\\d{8}"))) {
            Toast.makeText(this, "Por favor, ingrese un DNI válido (exactamente 8 dígitos numéricos)", Toast.LENGTH_SHORT).show()
            return
        }

        val generoCliente = spnGeneroCliente.text.toString()
        if (generoCliente.isEmpty()) {
            Toast.makeText(this, "Por favor, seleccione un género", Toast.LENGTH_SHORT).show()
            return
        }

        val direccionCliente = txtDireccionCliente.text.toString()
        if (direccionCliente.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese la dirección del cliente", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear un objeto Cliente con los datos actualizados
        val clienteActualizado = Cliente(
            codigoCliente = codigoCliente.toInt(),
            nombreCliente = nombreCliente,
            apellidosCliente = apellidosCliente,
            dniCliente = dniCliente.toInt(),
            generoClie = generoCliente,
            informacionCliente = direccionCliente
        )

        // Llamar a la API para actualizar el cliente
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.actualizarCliente(codigoCliente.toInt(), clienteActualizado)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    // Mostrar un cuadro de diálogo atractivo
                    AlertDialog.Builder(this@ClienteDetalleActivity)
                        .setTitle("¡Éxito!")
                        .setMessage("El cliente ha sido actualizado correctamente.")
                        .setPositiveButton("Aceptar") { _, _ ->
                            finish()
                            regresarCliente()
                        }
                        .show()
                } else {
                    // Mostrar un mensaje de error si la actualización falla
                    AlertDialog.Builder(this@ClienteDetalleActivity)
                        .setTitle("Error")
                        .setMessage("Hubo un problema al actualizar el cliente. Intenta nuevamente.\n${response.message()}")
                        .setPositiveButton("Aceptar", null)
                        .show()
                }
            }
        }
    }



    //ELIMINAR CLIENTES
    private fun eliminarCliente() {
        val codigoCliente = txtCodigoCliente.text.toString()

        if (codigoCliente.isEmpty()) {
            Toast.makeText(this@ClienteDetalleActivity, "Por favor, ingrese el código del cliente", Toast.LENGTH_SHORT).show()
            return
        }

        // Mostrar un cuadro de diálogo de confirmación
        AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este cliente?")
            .setPositiveButton("Sí") { _, _ ->
                // Proceder con la eliminación si el usuario confirma
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response: Response<Void> = api.eliminarCliente(codigoCliente.toInt())
                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@ClienteDetalleActivity, "Cliente eliminado con éxito", Toast.LENGTH_SHORT).show()
                                setResult(RESULT_OK)
                                regresarCliente()
                            } else {
                                Toast.makeText(this@ClienteDetalleActivity, "Error al eliminar el cliente", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: NumberFormatException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@ClienteDetalleActivity, "Código inválido", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@ClienteDetalleActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("No", null) // No hacer nada si el usuario cancela
            .show()
    }

}
