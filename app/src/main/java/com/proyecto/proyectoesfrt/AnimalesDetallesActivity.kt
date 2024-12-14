package com.proyecto.proyectoesfrt

 import android.content.Intent
            import android.os.Bundle
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
            import androidx.lifecycle.lifecycleScope
            import com.example.appproyecto.utils.ApiUtils
            import com.proyecto.proyectoesfrt.entidad.Animal
            import com.proyecto.proyectoesfrt.service.ApiServiceAnimal
            import kotlinx.coroutines.launch

            class AnimalesDetallesActivity : AppCompatActivity() {

                private lateinit var txtNombreAnimalDetalles: TextView
                private lateinit var txtEdadAnimalDetalle: TextView
                private lateinit var txtPesoAnimalDetalles: TextView
                private lateinit var spnColorAnimalDetalles: AutoCompleteTextView
                private lateinit var spnTipoAnimalDetalles: AutoCompleteTextView
                private lateinit var spnRazaAnimalDetalles: AutoCompleteTextView
                private lateinit var btnRegistrarAnimalModificar: Button
                private lateinit var btnRegistrarAnimalBorrar: Button

                private lateinit var btnRegresarAnimalAgregar:Button


                //Clientes

                private lateinit var spnListarDniClientesAnimalesDetalles:AutoCompleteTextView
                private lateinit var txtNombreCargarDatosDetalles:TextView

                //Apis
                private lateinit var api: ApiServiceAnimal

                override fun onCreate(savedInstanceState: Bundle?) {
                    super.onCreate(savedInstanceState)
                    enableEdgeToEdge()
                    setContentView(R.layout.animales_detalles_main)

                    // Configurar la vista
                    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                        insets
                    }

                    // Inicialización de vistas
                    txtNombreAnimalDetalles = findViewById(R.id.txtNombreAnimalDetalles)
                    txtEdadAnimalDetalle = findViewById(R.id.txtEdadAnimalDetalle)
                    txtPesoAnimalDetalles = findViewById(R.id.txtPesoAnimalDetalles)
                    spnColorAnimalDetalles = findViewById(R.id.spnColorAnimalDetalles)
                    spnTipoAnimalDetalles = findViewById(R.id.spnTipoAnimalDetalles)
                    spnRazaAnimalDetalles = findViewById(R.id.spnRazaAnimalDetalles)

                    //clientes
                    spnListarDniClientesAnimalesDetalles = findViewById(R.id.spnListarDniClientesAnimalesDetalles)
                    txtNombreCargarDatosDetalles = findViewById(R.id.txtNombreCargarDatosDetalles)

                    btnRegistrarAnimalModificar = findViewById(R.id.btnRegistrarAnimalModificar)
                    btnRegistrarAnimalBorrar = findViewById(R.id.btnRegistrarAnimalBorrar)
                    btnRegresarAnimalAgregar = findViewById(R.id.btnRegresarAnimalAgregar)



                    // Inicializar API
                    api = ApiUtils.getApiAnimal()

                    // Inicializar API para Cliente
                    val apiCliente = ApiUtils.getApiCliente()


                    // Obtener el ID del animal
                    val animalId = intent.getLongExtra("id", -1)

                    if (animalId != -1L) {
                        cargarDatosAnimal(animalId)
                    }

                    // Configuración de botones
                    btnRegistrarAnimalBorrar.setOnClickListener { borrarAnimal(animalId) }
                    btnRegistrarAnimalModificar.setOnClickListener { modificarAnimal(animalId) }
                    btnRegresarAnimalAgregar.setOnClickListener { volverListado() }
                }


                private fun cargarDatosAnimal(animalId: Long) {
                    lifecycleScope.launch {
                        try {
                            val response = api.getAnimalById(animalId) // Método en tu API para obtener el animal por ID
                            if (response.isSuccessful && response.body() != null) {
                                val animal = response.body() // Obtenemos el animal directamente desde la respuesta de la API
                                animal?.let {
                                    // Asigna los datos a las vistas
                                    txtNombreAnimalDetalles.text = it.nombre
                                    txtEdadAnimalDetalle.text = it.edad.toString()
                                    txtPesoAnimalDetalles.text = it.peso.toString()
                                    spnColorAnimalDetalles.setText(it.color ?: "", false)
                                    spnTipoAnimalDetalles.setText(it.tipo, false)
                                    spnRazaAnimalDetalles.setText(it.raza ?: "", false)

                                    spnListarDniClientesAnimalesDetalles.setText(it.cliente.dni, false)
                                    txtNombreCargarDatosDetalles.text = "${it.cliente.nombres} ${it.cliente.apellidos}"
                                }
                            } else {
                                Toast.makeText(this@AnimalesDetallesActivity, "Error al cargar datos del animal", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(this@AnimalesDetallesActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }



                private fun modificarAnimal(animalId: Long) {
                    val nombre = txtNombreAnimalDetalles.text.toString()
                    val tipo = spnTipoAnimalDetalles.text.toString()
                    val color = spnColorAnimalDetalles.text.toString()
                    val raza = spnRazaAnimalDetalles.text.toString()
                    val edadText = txtEdadAnimalDetalle.text.toString()
                    val pesoText = txtPesoAnimalDetalles.text.toString()

                    // Validaciones básicas
                    if (nombre.isEmpty() || tipo.isEmpty() || edadText.isEmpty() || pesoText.isEmpty()) {
                        Toast.makeText(this, "Por favor, completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                        return
                    }

                    val edad = edadText.toIntOrNull()
                    val peso = pesoText.toDoubleOrNull()

                    if (edad == null || peso == null) {
                        Toast.makeText(this, "Edad y peso deben ser valores válidos", Toast.LENGTH_SHORT).show()
                        return
                    }

                    // Obtener el DNI del cliente seleccionado
                    val dniCliente = spnListarDniClientesAnimalesDetalles.text.toString()

                    // Aquí necesitas hacer una llamada a la API para obtener el cliente por DNI
                    lifecycleScope.launch {
                        try {
                            // Obtén el cliente usando su DNI
                            val responseCliente = ApiUtils.getApiCliente().getClienteByDni(dniCliente)

                            if (responseCliente.isSuccessful && responseCliente.body() != null) {
                                val cliente = responseCliente.body()!!

                                // Crear el objeto Animal con el cliente completo
                                val animal = Animal(
                                    id = animalId,
                                    nombre = nombre,
                                    tipo = tipo,
                                    genero = "", // Puedes añadir un campo si es necesario
                                    edad = edad,
                                    peso = peso,
                                    raza = if (raza.isEmpty()) null else raza,
                                    color = if (color.isEmpty()) null else color,
                                    cliente = cliente  // Aquí asignas el cliente completo
                                )

                                // Llamada a la API para actualizar el animal
                                val updateResponse = api.updateAnimal(animal)  // Ya no es necesario pasar el animalId como parámetro, ya que está en el objeto Animal
                                if (updateResponse.isSuccessful) {
                                    Toast.makeText(this@AnimalesDetallesActivity, "Animal actualizado con éxito", Toast.LENGTH_SHORT).show()
                                    volverListado()
                                } else {
                                    Toast.makeText(this@AnimalesDetallesActivity, "Error al actualizar el animal", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@AnimalesDetallesActivity, "Cliente no encontrado", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(this@AnimalesDetallesActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }



                private fun borrarAnimal(animalId: Long) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Confirmar eliminación")
                    builder.setMessage("¿Estás seguro de que deseas eliminar este animal?")

                    builder.setPositiveButton("Sí") { _, _ ->
                        lifecycleScope.launch {
                            try {
                                val response = api.deleteAnimal(animalId) // Método para eliminar el animal
                                if (response.isSuccessful) {
                                    Toast.makeText(this@AnimalesDetallesActivity, "Animal eliminado con éxito", Toast.LENGTH_SHORT).show()
                                    volverListado()
                                } else {
                                    Toast.makeText(this@AnimalesDetallesActivity, "Error al eliminar el animal", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(this@AnimalesDetallesActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                    builder.show()
                }


                private fun volverListado() {
                    val intent = Intent(this, ListaAnimalesActivity::class.java)
                    startActivity(intent)
                }

                private fun cargarOpcionesSpinners() {
                    // Cargar los valores desde strings.xml
                    val colores = resources.getStringArray(R.array.label_color)
                    val tipos = resources.getStringArray(R.array.tipoAnimal)
                    val razas = resources.getStringArray(R.array.tipoRazaAnimal)

                    // Crear un adaptador para cada AutoCompleteTextView
                    val colorAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, colores)
                    spnColorAnimalDetalles.setAdapter(colorAdapter)

                    val tipoAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, tipos)
                    spnTipoAnimalDetalles.setAdapter(tipoAdapter)

                    val razaAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, razas)
                    spnRazaAnimalDetalles.setAdapter(razaAdapter)
                }

            }
