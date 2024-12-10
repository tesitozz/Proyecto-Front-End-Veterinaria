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
import com.proyecto.proyectoesfrt.service.ApiServiceCliente
import kotlinx.coroutines.launch

class AgregarClienteActivity : AppCompatActivity() {

    private lateinit var txtNombreClienteAgregar : TextView
    private lateinit var txtClienteApellidosAgregar : TextView
    private lateinit var txtDniClienteAgregar : TextView
    private lateinit var txtInformacionClienteAgregar : TextView
    private lateinit var spnGeneroClienteAgregar : AutoCompleteTextView
    private lateinit var btnRegistrarClienteAgregar : Button
    private lateinit var btnRegresarClienteAgregar  : Button


    //Api
    private lateinit var api: ApiServiceCliente


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.clientes_agregar_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtNombreClienteAgregar=findViewById(R.id.txtNombreClienteAgregar)
        txtClienteApellidosAgregar=findViewById(R.id.txtClienteApellidosAgregar)
        txtDniClienteAgregar=findViewById(R.id.txtDniClienteAgregar)
        txtInformacionClienteAgregar=findViewById(R.id.txtInformacionClienteAgregar)
        spnGeneroClienteAgregar=findViewById(R.id.spnGeneroClienteAgregar)
        btnRegistrarClienteAgregar = findViewById(R.id.btnRegistrarClienteAgregar)
        btnRegresarClienteAgregar = findViewById(R.id.btnRegresarClienteAgregar)

        api = ApiUtils.getApiCliente()

        btnRegistrarClienteAgregar.setOnClickListener { registrarClienteApi() }
        btnRegresarClienteAgregar.setOnClickListener { regresarCliente() }

        if (savedInstanceState != null) {
            txtNombreClienteAgregar.setText(savedInstanceState.getString("nombreCliente"))
            txtClienteApellidosAgregar.setText(savedInstanceState.getString("apellidosCliente"))
            txtDniClienteAgregar.setText(savedInstanceState.getString("dniCliente"))
            txtInformacionClienteAgregar.setText(savedInstanceState.getString("informacionCliente"))
            spnGeneroClienteAgregar.setText(savedInstanceState.getString("generoCliente"))
        } else {
            // Verificar si hay datos pasados por Intent desde ListaClientesActivity
            intent.extras?.let { extras ->
                txtNombreClienteAgregar.setText(extras.getString("nombreCliente"))
                txtClienteApellidosAgregar.setText(extras.getString("apellidosCliente"))
                txtDniClienteAgregar.setText(extras.getString("dniCliente"))
                txtInformacionClienteAgregar.setText(extras.getString("informacionCliente"))
                spnGeneroClienteAgregar.setText(extras.getString("generoCliente"))
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("nombreCliente", txtNombreClienteAgregar.text.toString().trim())
        outState.putString("apellidosCliente", txtClienteApellidosAgregar.text.toString().trim())
        outState.putString("dniCliente", txtDniClienteAgregar.text.toString().trim())
        outState.putString("informacionCliente", txtInformacionClienteAgregar.text.toString().trim())
        outState.putString("generoCliente", spnGeneroClienteAgregar.text.toString().trim())
    }



    private fun registrarClienteApi() {
        // Obtener los datos del cliente desde los campos de texto
        val nombreCliente = txtNombreClienteAgregar.text.toString().trim()
        val apellidosCliente = txtClienteApellidosAgregar.text.toString().trim()
        val dniClienteStr = txtDniClienteAgregar.text.toString().trim() // Obtener como String primero
        val informacionCliente = txtInformacionClienteAgregar.text.toString().trim()
        val generoCliente = spnGeneroClienteAgregar.text.toString().trim()

        // Validar que el DNI no esté vacío y sea un número válido antes de convertirlo
        val dniCliente = if (dniClienteStr.isNotEmpty() && dniClienteStr.toIntOrNull() != null) {
            dniClienteStr.toInt() // Convertir a entero si es válido
        } else {
            0 // O un valor predeterminado si no es válido
        }

        // Crear un objeto Cliente con los datos
        val nuevoCliente = Cliente(
                codigoCliente = 0, // Asignar un valor predeterminado o dejarlo como 0 si el backend lo genera
                nombreCliente = nombreCliente,
                apellidosCliente = apellidosCliente,
                dniCliente = dniCliente,
                informacionCliente = informacionCliente,
                generoClie = generoCliente
        )

        // Llamar a la API para registrar el cliente
        lifecycleScope.launch {
            val response = api.insertarCliente(nuevoCliente)
            if (response.isSuccessful) {
                // Registro exitoso
                Toast.makeText(this@AgregarClienteActivity, "Cliente registrado con éxito", Toast.LENGTH_SHORT).show()
                // Pasar los datos a ListaClientesActivity
                val intent = Intent(this@AgregarClienteActivity, ListaClientesActivity::class.java).apply {
                    putExtra("nombreCliente", nuevoCliente.nombreCliente)
                    putExtra("apellidosCliente", nuevoCliente.apellidosCliente)
                    putExtra("dniCliente", nuevoCliente.dniCliente)
                    putExtra("informacionCliente", nuevoCliente.informacionCliente)
                    putExtra("generoCliente", nuevoCliente.generoClie)
                }
                startActivity(intent)
                finish() // Cierra la actividad actual
            } else {
                // Manejar el error
                Toast.makeText(this@AgregarClienteActivity, "Error al registrar el cliente: ${response.message()}", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun  regresarCliente(){
        val intent = Intent(this, ListaClientesActivity::class.java)
        startActivity(intent)
    }





}