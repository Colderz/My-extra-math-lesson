package pakiet.arkadiuszzimny.extralessonapp1

import android.content.Context
import android.icu.text.Transliterator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                listOfItems = ArrayList()
                for (i in snapshot.children) {
                    val newRow = i.getValue(DatabaseRow::class.java)
                    listOfItems.add(newRow!!)
                }
                setupAdapter(listOfItems)
            }

        })

    }



    private fun setupAdapter(arrayData: ArrayList<DatabaseRow>) {
        recyclerView.adapter = RecyclerAdapter(arrayData)
    }
}
