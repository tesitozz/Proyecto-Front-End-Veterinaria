import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.proyectoesfrt.DoctorDetalleActivity
import com.proyecto.proyectoesfrt.R
import com.proyecto.proyectoesfrt.entidad.Doctor

class DoctorAdapter(
    private val context: Context,
    private val doctoresList: List<Doctor>
) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    inner class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvApiCodigoDoctor: TextView = itemView.findViewById(R.id.tvApiCodigoDoctor)
        val tvApiNombreDoctor: TextView = itemView.findViewById(R.id.tvApiNombreDoctor)
        val tvApiDNIDoctor: TextView = itemView.findViewById(R.id.tvApiDNIDoctor)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val doctor = doctoresList[position]
                    val intent = Intent(context, DoctorDetalleActivity::class.java).apply {
                        putExtra("CODIGO_DOCTOR", doctor.codigoDoctor)
                        putExtra("NOMBRE_DOCTOR", doctor.nombreDoctor)
                        putExtra("APELLIDO_DOCTOR", doctor.apellidoDoctor)
                        putExtra("DNI_DOCTOR", doctor.dniDoctor)
                        putExtra("ESPECIALIDAD_DOCTOR", doctor.especialidad)
                        putExtra("EXPERIENCIA_DOCTOR", doctor.experiencia)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doctores, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {

        val doctor = doctoresList[position]
        holder.tvApiCodigoDoctor.text = doctor.codigoDoctor.toString()
        holder.tvApiNombreDoctor.text = doctor.nombreDoctor
        holder.tvApiDNIDoctor.text = doctor.dniDoctor.toString()
    }


    override fun getItemCount() = doctoresList.size
}
