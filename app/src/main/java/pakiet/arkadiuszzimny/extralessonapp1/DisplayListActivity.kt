package pakiet.arkadiuszzimny.extralessonapp1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_display_list.*
import kotlinx.android.synthetic.main.activity_editable_list.*
import kotlinx.android.synthetic.main.dialog_layout.*
import java.util.*
import kotlin.collections.ArrayList

class DisplayListActivity : AppCompatActivity() {

    private lateinit var myRef2: DatabaseReference
    private lateinit var listOfItems: ArrayList<DatabaseRow>
    private lateinit var listOfStudents: ArrayList<Student>
    private lateinit var displayList: ArrayList<Student>
    private lateinit var recyclerAdapter: RecyclerAdapter2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_list)

        val firebase = FirebaseDatabase.getInstance()
        myRef2 = firebase.getReference("ArrayData")

        recyclerView2.layoutManager = LinearLayoutManager(this)
        //saveInfo.setOnClickListener {
            //Toast.makeText(this, studentId.text, Toast.LENGTH_LONG).show()
        //}

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
                    listOfStudents.add(Student(newId!!, newRow.imie, newRow.poziom, newRow.ostatniaLekcja, newRow.stawka))
                }
                displayList.addAll(listOfStudents)
                displayList.sortBy { it.imie }
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
                                displayList.sortBy { it.imie }
                            }
                            recyclerView2.adapter!!.notifyDataSetChanged()
                        }
                    }else {
                        displayList.clear()
                        displayList.addAll(listOfStudents)
                        displayList.sortBy { it.imie }
                        recyclerView2.adapter!!.notifyDataSetChanged()
                    }
                    return true
                }

            })
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun setupAdapter(arrayData: ArrayList<Student>) {
        recyclerAdapter = RecyclerAdapter2(arrayData)
        recyclerView2.adapter = RecyclerAdapter2(arrayData)
    }
}