package pakiet.arkadiuszzimny.extralessonapp1


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.icu.text.Transliterator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_editable_list.*
import java.util.*
import kotlin.collections.ArrayList

class EditableListActivity : AppCompatActivity() {


    private lateinit var myRef: DatabaseReference
    private lateinit var listOfItems: ArrayList<DatabaseRow>
    private lateinit var listOfStudents: ArrayList<Student>
    private lateinit var displayList: ArrayList<Student>
    private lateinit var deletedStudent: Student
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var deleteIcon: Drawable

    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#fcfcfc"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editable_list)

        val firebase = FirebaseDatabase.getInstance()
        myRef = firebase.getReference("ArrayData")

        recyclerView.layoutManager = LinearLayoutManager(this)
        buttonAdd.setOnClickListener {
            val nazwa = studentName.text.toString()
            val poziom = "Brak"
            val ostatniaLekcja = "Brak"
            val stawka = "Brak"
            val firebaseInput = DatabaseRow(nazwa, poziom, ostatniaLekcja, stawka)
            myRef.child(nazwa).setValue(firebaseInput)
            studentName.text.clear()
        }

    }

}
