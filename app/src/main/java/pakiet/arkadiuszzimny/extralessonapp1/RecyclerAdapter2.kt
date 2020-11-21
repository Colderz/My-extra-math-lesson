package pakiet.arkadiuszzimny.extralessonapp1


import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.studentcard_layout.view.*
import org.w3c.dom.Text

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
        holder.lastDate.text = dataArrayList[holder.adapterPosition].ostatniaLekcja
        holder.standardCost.text = dataArrayList[holder.adapterPosition].stawka
        holder.idText.text = dataArrayList[holder.adapterPosition].id.toString()
        //holder.dialogName.text = dataArrayList[holder.adapterPosition].nazwa
        Log.d("TAG", "To to id: ${dataArrayList[holder.adapterPosition].id}")
    }


    inner class ViewHolder(itemView: View, parent: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        val profileImage: CircleImageView
        val name: TextView
        val level: TextView
        val lastDate: TextView
        val standardCost: TextView
        val idText: TextView
        //val dialogName: TextView


        init {
            profileImage = itemView.findViewById(R.id.profile_image)
            name = itemView.findViewById(R.id.name)
            level = itemView.findViewById(R.id.level)
            lastDate = itemView.findViewById(R.id.lastDate)
            standardCost = itemView.findViewById(R.id.standardCost)
            idText = itemView.findViewById(R.id.idText)
            //dialogName = itemView.findViewById(R.id.dialogName)


           itemView.setOnClickListener {
               //val dialogView = LayoutInflater.from(parent.context).inflate(R.layout.dialog_layout, null)
               //val builder = AlertDialog.Builder(parent.context, R.style.CustomDialog).setView(dialogView)
               val dialog = Dialog(parent.context, R.style.CustomDialog)
               dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
               dialog.setContentView(R.layout.dialog_layout)
               val saveBtn = dialog.findViewById<Button>(R.id.saveButton)
               val levelET = dialog.findViewById<EditText>(R.id.studentLevel1)
               val costET = dialog.findViewById<EditText>(R.id.studentCost1)
               val titleTV = dialog.findViewById<TextView>(R.id.dialogName)
               titleTV.text = name.text
               saveBtn.setOnClickListener {
                   dialog.dismiss()
                   val level = levelET.text.toString()
                   val cost = costET.text.toString()
                   val databaseReferenceDialog = FirebaseDatabase.getInstance().getReference().child("ArrayData").child("${idText.text}")
                   databaseReferenceDialog.child("poziom").setValue(level)
                   databaseReferenceDialog.child("stawka").setValue(cost)
               }
               dialog.show()

               val databaseReference = FirebaseDatabase.getInstance().getReference().child("ArrayData").child("${idText.text}")
               databaseReference.child("poziom").setValue("No coś jest")
           }
        }
    }


}