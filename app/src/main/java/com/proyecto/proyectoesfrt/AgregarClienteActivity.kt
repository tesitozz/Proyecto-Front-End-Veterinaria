package com.proyecto.proyectoesfrt

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
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

    //REGISTRAR CLIENTES --VALIDADO CLIENTES
    private fun registrarClienteApi() {
        // Obtener los datos del cliente desde los campos de texto
        val nombreCliente = txtNombreClienteAgregar.text.toString().trim()
        val apellidosCliente = txtClienteApellidosAgregar.text.toString().trim()
        val dniClienteStr = txtDniClienteAgregar.text.toString().trim()
        val informacionCliente = txtInformacionClienteAgregar.text.toString().trim()
        val generoCliente = spnGeneroClienteAgregar.isSelected?.toString()?.trim().orEmpty()

        // Validación de los campos
        if (nombreCliente.isEmpty()) {
            showAlert("El nombre no puede estar vacío", AlertType.WARNING)
            return
        }

        if (apellidosCliente.isEmpty()) {
            showAlert("Los apellidos no pueden estar vacíos", AlertType.WARNING)
            return
        }

        if (dniClienteStr.isEmpty()) {
            showAlert("El DNI no puede estar vacío", AlertType.WARNING)
            return
        }

        if (dniClienteStr.length != 8 || !dniClienteStr.all { it.isDigit() }) {
            showAlert("El DNI debe contener exactamente 8 dígitos numéricos", AlertType.WARNING)
            return
        }

        if (informacionCliente.isEmpty()) {
            showAlert("La información adicional no puede estar vacía", AlertType.WARNING)
            return
        }

        if (generoCliente.isEmpty()) {
            showAlert("El género no puede estar vacío", AlertType.WARNING)
            return
        }

        if (!nombreCliente.all { it.isLetter() || it.isWhitespace() }) {
            showAlert("El nombre solo puede contener letras", AlertType.WARNING)
            return
        }

        if (!apellidosCliente.all { it.isLetter() || it.isWhitespace() }) {
            showAlert("Los apellidos solo pueden contener letras", AlertType.WARNING)
            return
        }

        // Convertir el DNI a entero
        val dniCliente = dniClienteStr.toInt()

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
            try {
                val response = api.insertarCliente(nuevoCliente)
                if (response.isSuccessful) {
                    showAlert("Cliente registrado con éxito", AlertType.SUCCESS)
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
                    showAlert("Error al registrar el cliente: ${response.message()}", AlertType.ERROR)
                }
            } catch (e: Exception) {
                showAlert("Error: ${e.message}", AlertType.ERROR)
            }
        }
    }


    enum class AlertType {
        SUCCESS, WARNING, ERROR
    }

    fun showAlert(mensaje: String, type: AlertType) {
        when (type) {
            AlertType.SUCCESS -> {
                val toast = Toast.makeText(this, "\u2705 $mensaje", Toast.LENGTH_LONG)
                val view = toast.view
                view?.setBackgroundColor(Color.parseColor("#4CAF50")) // Fondo verde para éxito
                val text = view?.findViewById<TextView>(android.R.id.message)
                text?.setTextColor(Color.WHITE) // Texto en blanco
                toast.show()
            }
            AlertType.WARNING -> {
                val toast = Toast.makeText(this, "\u26A0 $mensaje", Toast.LENGTH_LONG)
                val view = toast.view
                view?.setBackgroundColor(Color.parseColor("#FFC107")) // Fondo amarillo para advertencia
                val text = view?.findViewById<TextView>(android.R.id.message)
                text?.setTextColor(Color.BLACK) // Texto en negro
                toast.show()
            }
            AlertType.ERROR -> {
                val toast = Toast.makeText(this, "\u274C $mensaje", Toast.LENGTH_LONG)
                val view = toast.view
                view?.setBackgroundColor(Color.parseColor("#F44336")) // Fondo rojo para error
                val text = view?.findViewById<TextView>(android.R.id.message)
                text?.setTextColor(Color.WHITE) // Texto en blanco
                toast.show()
            }
        }
    }



    //REGRESAR CLIENTE
    private fun  regresarCliente(){
        val intent = Intent(this, ListaClientesActivity::class.java)
        startActivity(intent)
    }

}