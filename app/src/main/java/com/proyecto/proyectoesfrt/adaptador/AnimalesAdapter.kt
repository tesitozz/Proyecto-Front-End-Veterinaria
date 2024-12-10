package com.proyecto.proyectoesfrt.adaptador

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.proyectoesfrt.AnimalesDetallesActivity
import com.proyecto.proyectoesfrt.R
import com.proyecto.proyectoesfrt.entidad.Animales

class AnimalesAdapter(private val context: Context, private val lista: List<Animales>) :
    RecyclerView.Adapter<AnimalesAdapter.AnimalViewHolder>() {

    inner class AnimalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvApiCodigoAnimal: TextView = itemView.findViewById(R.id.tvApiCodigoAnimal)
        val tvApiNombreAnimal: TextView = itemView.findViewById(R.id.tvApiNombreAnimal)
        val tvApiNombreDueno: TextView = itemView.findViewById(R.id.tvApiNombreDueno)

        init {
            itemView.setOnClickListener {
                val animal = lista[adapterPosition]
                val intent = Intent(context, AnimalesDetallesActivity::class.java).apply {
                    putExtra("CODIGO_ANIMAL", animal.codigoAnimal)
                    putExtra("NOMBRE_ANIMAL", animal.nombreAnimal)
                    putExtra("DUENO", animal.dueno)
                    putExtra("EDAD", animal.edad)
                    putExtra("PESO", animal.peso)
                    putExtra("INFORMACION_ANIMAL", animal.informacionAnimal)
                    putExtra("SEXO", animal.generoAnim)
                    putExtra("TIPO", animal.tipoAnimal)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_animales, parent, false)
        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = lista[position]
        holder.tvApiCodigoAnimal.text = animal.codigoAnimal.toString() // Muestra el código
        holder.tvApiNombreAnimal.text = animal.nombreAnimal // Muestra el nombre
        holder.tvApiNombreDueno.text = animal.dueno // Muestra el dueño
    }

    override fun getItemCount(): Int = lista.size
}

