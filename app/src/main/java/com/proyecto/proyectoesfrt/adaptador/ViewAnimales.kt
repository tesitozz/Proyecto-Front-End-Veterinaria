package com.proyecto.proyectoesfrt.adaptador

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.proyectoesfrt.R

class ViewAnimales(vista:View):RecyclerView.ViewHolder(vista) {

    //Atributos que irian en el Item_Animales
    var tvApiCodigoAnimal:TextView
    var tvApiNombreAnimal:TextView
    var tvApiNombreDueno:TextView

    //Bloque Init

    init {
        tvApiCodigoAnimal=vista.findViewById(R.id.tvApiCodigoAnimal)
        tvApiNombreAnimal=vista.findViewById(R.id.tvApiNombreAnimal)
        tvApiNombreDueno=vista.findViewById(R.id.tvApiNombreDueno)
    }
}