package pakiet.arkadiuszzimny.extralessonapp1.adapters


import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView
import pakiet.arkadiuszzimny.extralessonapp1.R
import pakiet.arkadiuszzimny.extralessonapp1.model.Student
import kotlin.collections.ArrayList

class RecyclerAdapter2(private val dataArrayList: ArrayList<Student>) : RecyclerView.Adapter<RecyclerAdapter2.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.studentcard_layout, parent, false)
        return ViewHolder(v, parent)

    }

    override fun getItemCount(): Int {
        return dataArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = dataArrayList[holder.adapterPosition].nazwa
        holder.level.text = dataArrayList[holder.adapterPosition].poziom
        holder.standardCost.text = dataArrayList[holder.adapterPosition].stawka
        holder.idText.text = dataArrayList[holder.adapterPosition].id.toString()
        Log.d("TAG", "To to id: ${dataArrayList[holder.adapterPosition].id}")
    }


    inner class ViewHolder(itemView: View, parent: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        val profileImage: CircleImageView
        val name: TextView
        val level: TextView
        val standardCost: TextView
        val idText: TextView


        init {
            profileImage = itemView.findViewById(R.id.profile_image)
            name = itemView.findViewById(R.id.name)
            level = itemView.findViewById(R.id.level)
            standardCost = itemView.findViewById(R.id.standardCost)
            idText = itemView.findViewById(R.id.idText)


           itemView.setOnClickListener {
               val dialog = Dialog(parent.context,
                   R.style.CustomDialog
               )
               dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
               dialog.setContentView(R.layout.dialog_layout)
               val saveBtn = dialog.findViewById<Button>(R.id.saveButton)
               val levelET = dialog.findViewById<EditText>(R.id.studentLevel1)
               val costET = dialog.findViewById<EditText>(R.id.studentCost1)
               val titleTV = dialog.findViewById<TextView>(R.id.dialogName)
               titleTV.text = name.text
               saveBtn.setOnClickListener {
                   dialog.dismiss()
                   val databaseReferenceDialog = FirebaseDatabase.getInstance().getReference().child("ArrayData").child("${idText.text}")
                   if(levelET.text.toString() != "") {
                       val level = levelET.text.toString()
                       databaseReferenceDialog.child("poziom").setValue(level)
                   }
                   if(costET.text.toString() != "") {
                       val cost = costET.text.toString()
                       databaseReferenceDialog.child("stawka").setValue(cost)
                   }
               }
               dialog.show()
           }
        }
    }


}