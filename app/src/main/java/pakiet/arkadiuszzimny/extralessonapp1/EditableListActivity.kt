package pakiet.arkadiuszzimny.extralessonapp1

import android.content.Context
import android.icu.text.Transliterator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.SearchEvent
import android.view.View
import android.widget.Adapter
import androidx.appcompat.widget.SearchView
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
    private lateinit var rv: RecyclerView
    private lateinit var listOfStudents: ArrayList<Student>
    private lateinit var displayList: ArrayList<Student>
    private lateinit var deletedStudent: Student

    private lateinit var adapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editable_list)

        val firebase = FirebaseDatabase.getInstance()
        myRef = firebase.getReference("ArrayData")

        recyclerView.layoutManager = LinearLayoutManager(this)
        buttonAdd.setOnClickListener {
            val imie = studentName.text.toString()
            val firebaseInput = DatabaseRow(imie)
            myRef.child("${Date().time}").setValue(firebaseInput)
        }

        myRef.addValueEventListener(object: ValueEventListener{
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
                setupAdapter(displayList)
            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        var item: MenuItem = menu!!.findItem(R.id.action_search)
        if(item != null) {
            var searchView = item.actionView as SearchView
            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText!!.isNotEmpty()) {
                        displayList.clear()
                        var search = newText.toLowerCase(Locale.getDefault())
                        for(student in listOfStudents) {
                            if(student.imie.toLowerCase(Locale.getDefault()).contains(search)) {
                                displayList.add(student)
                            }
                            recyclerView.adapter!!.notifyDataSetChanged()
                        }
                    }else {
                        displayList.clear()
                        displayList.addAll(listOfStudents)
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                    return true
                }

            })
        }

        return super.onCreateOptionsMenu(menu)
    }
    

    private fun setupAdapter(arrayData: ArrayList<Student>) {
        recyclerView.adapter = RecyclerAdapter(arrayData)
    }



}
