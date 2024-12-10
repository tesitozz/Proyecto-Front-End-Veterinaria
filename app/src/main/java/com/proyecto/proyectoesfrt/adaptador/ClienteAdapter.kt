package com.proyecto.proyectoesfrt.adaptador

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.proyectoesfrt.ClienteDetalleActivity
import com.proyecto.proyectoesfrt.R
import com.proyecto.proyectoesfrt.entidad.Cliente

class ClienteAdapter(private  val context:Context, private val lista: List<Cliente>) :
RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>(){

    inner class ClienteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val tvApiCodigoCliente : TextView = itemView.findViewById(R.id.tvApiCodigoCliente)
        val tvApiNombreCliente : TextView = itemView.findViewById(R.id.tvApiNombreCliente)
        val tvApiDniCliente : TextView = itemView.findViewById(R.id.tvApiDniCliente )

        init {
            itemView.setOnClickListener{
                val cliente = lista[adapterPosition]
                val intent = Intent(context, ClienteDetalleActivity::class.java).apply {
                    putExtra("CODIGO_CLIENTE", cliente.codigoCliente)
                    putExtra("NOMBRE_CLIENTE", cliente.nombreCliente)
                    putExtra("APELLIDOS_CLIENTE", cliente.apellidosCliente)
                    putExtra("DNI_CLIENTE", cliente.dniCliente)
                    putExtra("GENERO_CLIE", cliente.generoClie)
                    putExtra("INFORMACION_CLIENTE", cliente.informacionCliente)
                }
                context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = lista[position]
        holder.tvApiCodigoCliente.text = cliente.codigoCliente.toString()
        holder.tvApiNombreCliente.text = cliente.nombreCliente
        holder.tvApiDniCliente.text = cliente.dniCliente.toString()
    }

    override fun getItemCount(): Int = lista.size

}