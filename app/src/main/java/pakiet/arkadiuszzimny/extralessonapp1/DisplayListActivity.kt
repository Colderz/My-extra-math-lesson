package pakiet.arkadiuszzimny.extralessonapp1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_display_list.*
import kotlinx.android.synthetic.main.activity_editable_list.*

class DisplayListActivity : AppCompatActivity() {

    private lateinit var myRef2: DatabaseReference
    private lateinit var listOfItems: ArrayList<DatabaseRow>
    private lateinit var listOfStudents: ArrayList<Student>
    private lateinit var displayList: ArrayList<Student>
    private lateinit var recyclerAdapter: RecyclerAdapter2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_list)
        supportActionBar?.hide()

        val firebase = FirebaseDatabase.getInstance()
        myRef2 = firebase.getReference("ArrayData")

        recyclerView2.layoutManager = LinearLayoutManager(this)

        myRef2.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                listOfItems = ArrayList()
                listOfStudents = ArrayList()
                displayList = ArrayList()
                for (i in snapshot.children) {
                    val newId = i.key?.toLong()
                    Log.d("infoid", "O to id chodzi: ${newId}")
                    val newRow = i.getValue(DatabaseRow::class.java)
                    listOfItems.add(newRow!!)
                    listOfStudents.add(Student(newId!!, newRow.imie))
                }
                displayList.addAll(listOfStudents)
                displayList.sortBy { it.imie }
                setupAdapter(displayList)
            }
        })
    }
    private fun setupAdapter(arrayData: ArrayList<Student>) {
        recyclerAdapter = RecyclerAdapter2(arrayData)
        recyclerView2.adapter = RecyclerAdapter2(arrayData)
    }
}