    package com.proyecto.proyectoesfrt

    import android.content.Intent
    import android.os.Bundle
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
    import androidx.lifecycle.lifecycleScope
    import com.example.appproyecto.utils.ApiUtils
    import com.google.android.material.textfield.TextInputEditText
    import com.proyecto.proyectoesfrt.entidad.Animal
    import com.proyecto.proyectoesfrt.service.ApiServiceAnimal
    import kotlinx.coroutines.launch

    class AgregarAnimalesActivity : AppCompatActivity() {

       private lateinit var  txtNombreAnimalAgregar:TextView

       private lateinit var txtEdadAnimalAgregar:TextView

       private lateinit var txtPesoAnimalAgregar:TextView

        private lateinit var spnColorAnimalAgregar: AutoCompleteTextView

        private lateinit var spnTipoAnimalRegistrar: AutoCompleteTextView

        private lateinit var spnRazaAnimalAgregar: AutoCompleteTextView

        //Estos 2 - CLIENTE
        private lateinit var spnListarDniClientesAnimales:AutoCompleteTextView

        private lateinit var txtNombreCargarDatos:TextView



        private lateinit var btnRegistrarAnimalAgregar : Button

        private lateinit var btnRegresarAnimalAgregar:Button


        //Api
        private lateinit var api: ApiServiceAnimal


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.animales_agregar_main)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            txtNombreAnimalAgregar = findViewById(R.id.txtNombreAnimalAgregar)
            txtEdadAnimalAgregar = findViewById(R.id.txtEdadAnimalAgregar)
            txtPesoAnimalAgregar = findViewById(R.id.txtPesoAnimalAgregar)
            spnColorAnimalAgregar = findViewById(R.id.spnColorAnimalAgregar)
            spnTipoAnimalRegistrar = findViewById(R.id.spnTipoAnimalRegistrar)
            spnListarDniClientesAnimales = findViewById(R.id.spnListarDniClientesAnimales)
            txtNombreCargarDatos = findViewById(R.id.txtNombreCargarDatos)
            spnRazaAnimalAgregar = findViewById(R.id.spnRazaAnimalAgregar)
            btnRegresarAnimalAgregar = findViewById(R.id.btnRegresarAnimalAgregar)
            btnRegistrarAnimalAgregar = findViewById(R.id.btnRegistrarAnimalAgregar)


            btnRegistrarAnimalAgregar.setOnClickListener { registrarAnimalApi() }
            btnRegresarAnimalAgregar.setOnClickListener { regresarAnimal() }


            api = ApiUtils.getApiAnimal()

            cargarDniClientes()


        }

        private fun registrarAnimalApi() {
            val nombre = txtNombreAnimalAgregar.text.toString()
            val tipo = spnTipoAnimalRegistrar.text.toString()
            val genero = spnColorAnimalAgregar.text.toString() // Puedes agregar un spinner para género si corresponde
            val edadText = txtEdadAnimalAgregar.text.toString()
            val pesoText = txtPesoAnimalAgregar.text.toString()
            val raza = spnRazaAnimalAgregar.text.toString()
            val color = spnColorAnimalAgregar.text.toString()
            val dniCliente = spnListarDniClientesAnimales.text.toString()

            // Validaciones
            if (nombre.isEmpty() || tipo.isEmpty() || genero.isEmpty() ||
                edadText.isEmpty() || pesoText.isEmpty() || dniCliente.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                return
            }

            val edad = edadText.toIntOrNull()
            val peso = pesoText.toDoubleOrNull()

            if (edad == null || peso == null) {
                Toast.makeText(this, "Edad y peso deben ser valores válidos", Toast.LENGTH_SHORT).show()
                return
            }

            lifecycleScope.launch {
                try {
                    // Obtener el cliente por DNI
                    val clienteResponse = ApiUtils.getApiCliente().getClienteByDni(dniCliente)
                    if (clienteResponse.isSuccessful && clienteResponse.body() != null) {
                        val cliente = clienteResponse.body()!!

                        // Crear el objeto Animal
                        val animal = Animal(
                            id = null, // El backend asignará el ID
                            nombre = nombre,
                            tipo = tipo,
                            genero = genero,
                            edad = edad,
                            peso = peso,
                            raza = if (raza.isEmpty()) null else raza,
                            color = if (color.isEmpty()) null else color,
                            cliente = cliente
                        )

                        // Registrar el animal
                        registrarAnimalEnApi(animal)
                    } else {
                        Toast.makeText(this@AgregarAnimalesActivity, "Cliente no encontrado", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@AgregarAnimalesActivity, "Error al obtener cliente: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }


        fun registrarAnimalEnApi(animal: Animal) {
            lifecycleScope.launch {
                try {
                    // Hacer la llamada a la API para registrar el animal
                    val response = api.createAnimal(animal)

                    // Verificar si la respuesta fue exitosa
                    if (response.isSuccessful) {
                        // Mostrar un mensaje de éxito
                        Toast.makeText(this@AgregarAnimalesActivity, "Animal registrado con éxito", Toast.LENGTH_SHORT).show()
                        // Puedes redirigir a otra actividad si es necesario
                        val intent = Intent(this@AgregarAnimalesActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Mostrar un mensaje de error si la respuesta no es exitosa
                        Toast.makeText(this@AgregarAnimalesActivity, "Error al registrar el animal", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Manejar cualquier error de red o excepción
                    Toast.makeText(this@AgregarAnimalesActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun cargarDniClientes() {
            val apiCliente = ApiUtils.getApiCliente() // Asegúrate de tener este método
            lifecycleScope.launch {
                try {
                    val response = apiCliente.getAllClientes() // Asegúrate de que este endpoint te devuelva todos los clientes
                    if (response.isSuccessful && response.body() != null) {
                        val clientes = response.body()!!
                        val dniList = clientes.map { it.dni } // Extraer la lista de DNIs
                        val adapter = ArrayAdapter(this@AgregarAnimalesActivity, android.R.layout.simple_dropdown_item_1line, dniList)
                        spnListarDniClientesAnimales.setAdapter(adapter)

                        // Manejar la selección del DNI
                        spnListarDniClientesAnimales.setOnItemClickListener { _, _, position, _ ->
                            val dniSeleccionado = adapter.getItem(position) ?: ""
                            cargarNombreCliente(dniSeleccionado)
                        }
                    } else {
                        Toast.makeText(this@AgregarAnimalesActivity, "Error al cargar clientes", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@AgregarAnimalesActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun cargarNombreCliente(dni: String) {
            val apiCliente = ApiUtils.getApiCliente()
            lifecycleScope.launch {
                try {
                    val response = apiCliente.getClienteByDni(dni)
                    if (response.isSuccessful && response.body() != null) {
                        val cliente = response.body()!!
                        txtNombreCargarDatos.text = "${cliente.nombres} ${cliente.apellidos}" // Mostrar nombres y apellidos
                    } else {
                        txtNombreCargarDatos.text = "Cliente no encontrado"
                    }
                } catch (e: Exception) {
                    txtNombreCargarDatos.text = "Error al cargar datos"
                }
            }
        }



        fun regresarAnimal(){
            var intent= Intent(this,ListaAnimalesActivity::class.java)
            startActivity(intent)
        }




    }