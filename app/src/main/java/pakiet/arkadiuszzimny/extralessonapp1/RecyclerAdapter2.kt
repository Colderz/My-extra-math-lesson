package pakiet.arkadiuszzimny.extralessonapp1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class RecyclerAdapter2(private val dataArrayList: ArrayList<Student>) : RecyclerView.Adapter<RecyclerAdapter2.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.studentcard_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return dataArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = dataArrayList[holder.adapterPosition].imie
        holder.level.text = "II LICEUM"
        holder.lastDate.text = "02-11-2020"
        holder.standardCost.text = "30 zł"
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: CircleImageView
        val name: TextView
        val level: TextView
        val lastDate: TextView
        val standardCost: TextView

        init {
            profileImage = itemView.findViewById(R.id.profile_image)
            name = itemView.findViewById(R.id.name)
            level = itemView.findViewById(R.id.level)
            lastDate = itemView.findViewById(R.id.lastDate)
            standardCost = itemView.findViewById(R.id.standardCost)

            itemView.setOnClickListener {
                val position = adapterPosition
                Toast.makeText(itemView.context, "Tutaj pojawi się okno z edycją i ustawianiem, to dla ucznia: ${dataArrayList[position].imie}", Toast.LENGTH_LONG).show()
            }

        }
    }


}