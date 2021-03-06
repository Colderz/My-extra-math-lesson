package pakiet.arkadiuszzimny.extralessonapp1.viewModel

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_display_list.*
import pakiet.arkadiuszzimny.extralessonapp1.model.DatabaseRow
import pakiet.arkadiuszzimny.extralessonapp1.R
import pakiet.arkadiuszzimny.extralessonapp1.model.Student
import pakiet.arkadiuszzimny.extralessonapp1.adapters.RecyclerAdapter2
import java.util.*
import kotlin.collections.ArrayList

class DisplayListActivity : AppCompatActivity() {

    private lateinit var myRef2: DatabaseReference
    private lateinit var listOfItems: ArrayList<DatabaseRow>
    private lateinit var listOfStudents: ArrayList<Student>
    private lateinit var displayList: ArrayList<Student>
    private lateinit var recyclerAdapter: RecyclerAdapter2
    private lateinit var deletedStudent: Student
    private lateinit var deleteIcon: Drawable

    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#fcfcfc"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_list)

        val firebase = FirebaseDatabase.getInstance()
        myRef2 = firebase.getReference("ArrayData")

        recyclerView2.layoutManager = LinearLayoutManager(this)
        addButton.setOnClickListener {
            val intent = Intent(this, EditableListActivity::class.java)
            startActivity(intent)
        }

        myRef2.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                listOfItems = ArrayList()
                listOfStudents = ArrayList()
                displayList = ArrayList()
                for (i in snapshot.children) {
                    val newId = i.key!!.toLong()
                    val newRow = i.getValue(DatabaseRow::class.java)
                    listOfItems.add(newRow!!)
                    listOfStudents.add(
                        Student(
                            newId!!,
                            newRow.nazwa,
                            newRow.poziom,
                            newRow.stawka
                        )
                    )
                }
                displayList.addAll(listOfStudents)
                displayList.sortBy { it.nazwa }
                setupAdapter(displayList)
            }
        })
        deleteIcon = ContextCompat.getDrawable(this,
            R.drawable.ic_baseline_delete_24
        )!!
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView2)
    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(
        ItemTouchHelper.RIGHT)) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        fun firebaseDelete(Dstudent: Student) {
            FirebaseDatabase.getInstance().getReference()
                .child("ArrayData")
                .child(Dstudent.id.toString())
                .removeValue()
        }

        fun undoDeleted(position: Int) {
            Snackbar.make(recyclerView2, "Usunięto ucznia: ${deletedStudent.nazwa}", Snackbar.LENGTH_LONG).setAction("Cofnij", View.OnClickListener {
                val nazwa = deletedStudent.nazwa
                val poziom = deletedStudent.poziom
                val stawka = deletedStudent.stawka
                val firebaseInput =
                    DatabaseRow(
                        nazwa,
                        poziom,
                        stawka
                    )
                myRef2.child("${Date().time}").setValue(firebaseInput)
            }).show()
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var position = viewHolder.adapterPosition
            when(direction) {
                ItemTouchHelper.LEFT -> {
                    deletedStudent = displayList.get(position)
                    displayList.removeAt(position)
                    firebaseDelete(deletedStudent)
                    undoDeleted(position)
                }
                ItemTouchHelper.RIGHT -> {
                    deletedStudent = displayList.get(position)
                    displayList.removeAt(position)
                    firebaseDelete(deletedStudent)
                    undoDeleted(position)
                }
            }
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView2: RecyclerView,
            viewHolder2: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val itemView = viewHolder2.itemView
            val iconMargin = (itemView.height - deleteIcon.intrinsicHeight)/2
            if(dX > 0) {
                swipeBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                deleteIcon.setBounds(itemView.left + iconMargin, itemView.top + iconMargin, itemView.left + iconMargin + deleteIcon.intrinsicWidth, itemView.bottom - iconMargin)
            } else {
                swipeBackground.setBounds(itemView.right+dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                deleteIcon.setBounds(itemView.right - iconMargin - deleteIcon.intrinsicWidth, itemView.top + iconMargin, itemView.right - iconMargin, itemView.bottom - iconMargin)
            }

            swipeBackground.draw(c)
            deleteIcon.draw(c)

            super.onChildDraw(c, recyclerView2, viewHolder2, dX, dY, actionState, isCurrentlyActive)
        }
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
                            if(student.nazwa.toLowerCase(Locale.getDefault()).contains(search)) {
                                displayList.add(student)
                                displayList.sortBy { it.nazwa }
                            }
                            recyclerView2.adapter!!.notifyDataSetChanged()
                        }
                    }else {
                        displayList.clear()
                        displayList.addAll(listOfStudents)
                        displayList.sortBy { it.nazwa }
                        recyclerView2.adapter!!.notifyDataSetChanged()
                    }
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupAdapter(arrayData: ArrayList<Student>) {
        recyclerAdapter =
            RecyclerAdapter2(
                arrayData
            )
        recyclerView2.adapter =
            RecyclerAdapter2(
                arrayData
            )
    }
}