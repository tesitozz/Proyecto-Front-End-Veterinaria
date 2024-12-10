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
import com.proyecto.proyectoesfrt.service.ApiServiceCliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClienteDetalleActivity : AppCompatActivity() {

    private lateinit var txtCodigoCliente:TextView
    private lateinit var txtNombreCliente:TextView
    private lateinit var txtClienteApellidos:TextView
    private lateinit var txtDNICliente:TextView
    private lateinit var txtDireccionCliente:TextView
    private lateinit var spnGeneroCliente: AutoCompleteTextView
    private lateinit var btnRegresarCliente:Button
    private lateinit var btnActualizarCliente:Button
    private lateinit var btnEliminarCliente:Button

    //Api
    private lateinit var api:ApiServiceCliente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.clientes_detalles_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtCodigoCliente = findViewById(R.id.txtCodigoClienteActualizar)
        txtNombreCliente = findViewById(R.id.txtNombreClienteActualizar)
        txtClienteApellidos = findViewById(R.id.txtClienteApellidosActualizar)
        txtDNICliente = findViewById(R.id.txtDNIClienteActualizar)
        txtDireccionCliente = findViewById(R.id.txtDireccionClienteActualizar)
        spnGeneroCliente = findViewById(R.id.spnGeneroClienteActualizar)
        btnRegresarCliente=findViewById(R.id.btnRegresarCliente)
        btnActualizarCliente = findViewById(R.id.btnActualizarCliente)
        btnEliminarCliente = findViewById(R.id.btnEliminarCliente)

        //Api

        api = ApiUtils.getApiCliente()

        //Obtener datos del Intent

        obtenerDatos()

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

    private fun regresarCliente(){
        var intent= Intent(this,ListaClientesActivity::class.java)
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
        txtDNICliente.text = dniCliente.toString()
        txtDireccionCliente.text = informacionCliente
        spnGeneroCliente.setText(generoCliente)

        // Registro para depuración
        Log.d("ClienteDetalleActivity", "Código: $codigoCliente, Nombre: $nombreCliente, Apellidos: $apellidosCliente")
    }

    private fun actualizarCliente() {
        // Obtener los datos del cliente desde los campos de texto
        val codigoCliente = txtCodigoCliente.text.toString().toInt()
        val nombreCliente = txtNombreCliente.text.toString()
        val apellidosCliente = txtClienteApellidos.text.toString()
        val dniCliente = txtDNICliente.text.toString().toInt()
        val generoCliente = spnGeneroCliente.text.toString()
        val direccionCliente = txtDireccionCliente.text.toString()

        // Crear un objeto Cliente con los datos actualizados
        val clienteActualizado = Cliente(
            codigoCliente = codigoCliente,
            nombreCliente = nombreCliente,
            apellidosCliente = apellidosCliente,
            dniCliente = dniCliente,
            generoClie = generoCliente,
            informacionCliente = direccionCliente
        )

        // Llamar a la API para actualizar el cliente
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.actualizarCliente(codigoCliente, clienteActualizado)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    // Actualización exitosa
                    Toast.makeText(this@ClienteDetalleActivity, "Cliente actualizado con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    // Manejar el error
                    Toast.makeText(this@ClienteDetalleActivity, "Error al actualizar el cliente: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun eliminarCliente() {
        // Obtener el código del cliente que se va a eliminar
        val codigoCliente = txtCodigoCliente.text.toString().toInt()

        // Llamar a la API para eliminar el cliente
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.eliminarCliente(codigoCliente)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    // Eliminación exitosa
                    Toast.makeText(this@ClienteDetalleActivity, "Cliente eliminado con éxito", Toast.LENGTH_SHORT).show()
                    regresarCliente() // Cerrar la actividad después de eliminar
                } else {
                    // Manejar el error
                    Toast.makeText(this@ClienteDetalleActivity, "Error al eliminar el cliente: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }





}