package pakiet.arkadiuszzimny.extralessonapp1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private val dataArrayList: ArrayList<DatabaseRow>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    //private var students: MutableList<String> = dataArrayList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.editablecard_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return dataArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.personName.text = dataArrayList[holder.adapterPosition].imie

    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView
        val personName: TextView

        init {
            itemImage = itemView.findViewById(R.id.itemImage)
            personName = itemView.findViewById(R.id.personName)

            /*itemView.setOnClickListener {
                val position: Int = adapterPosition
                //Toast.makeText(itemView.context, "Nacisnąłeś ${students[position]}", Toast.LENGTH_LONG).show()
            */
        }


    }

}