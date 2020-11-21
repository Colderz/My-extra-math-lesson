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

        myRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                listOfItems = ArrayList()
                listOfStudents = ArrayList()
                displayList = ArrayList()
                for (i in snapshot.children) {
                    val newId = i.key?.toString()
                    Log.d("infoid", "O to id chodzi: ${newId}")
                    val newRow = i.getValue(DatabaseRow::class.java)
                    listOfItems.add(newRow!!)
                    listOfStudents.add(Student(newId!!, newRow.poziom, newRow.ostatniaLekcja, newRow.stawka))
                }
                displayList.addAll(listOfStudents)
                displayList.sortBy { it.nazwa }
                setupAdapter(displayList)
            }
        })

        deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_delete_24)!!
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
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
                .child(Dstudent.nazwa.toString())
                .removeValue()
        }

        fun undoDeleted(position: Int) {
            Snackbar.make(
                recyclerView,
                "Usunięto ucznia: ${deletedStudent.nazwa}",
                Snackbar.LENGTH_LONG
            ).setAction("Cofnij", View.OnClickListener {
                val imie = deletedStudent.nazwa
                val poziom = "Brak"
                val ostatniaLekcja = "Brak"
                val stawka = "Brak"
                val firebaseInput = DatabaseRow(imie, poziom, ostatniaLekcja, stawka)
                myRef.child(imie).setValue(firebaseInput)
            }).show()
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var position = viewHolder.adapterPosition
            when (direction) {
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
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val itemView = viewHolder.itemView
            val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2
            if (dX > 0) {
                swipeBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                deleteIcon.setBounds(
                    itemView.left + iconMargin,
                    itemView.top + iconMargin,
                    itemView.left + iconMargin + deleteIcon.intrinsicWidth,
                    itemView.bottom - iconMargin
                )
            } else {
                swipeBackground.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                deleteIcon.setBounds(
                    itemView.right - iconMargin - deleteIcon.intrinsicWidth,
                    itemView.top + iconMargin,
                    itemView.right - iconMargin,
                    itemView.bottom - iconMargin
                )
            }

            swipeBackground.draw(c)
            deleteIcon.draw(c)

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
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
                            recyclerView.adapter!!.notifyDataSetChanged()
                        }
                    }else {
                        displayList.clear()
                        displayList.addAll(listOfStudents)
                        displayList.sortBy { it.nazwa }
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                    return true
                }

            })
        }

        return super.onCreateOptionsMenu(menu)
    }


    private fun setupAdapter(arrayData: ArrayList<Student>) {
        recyclerAdapter = RecyclerAdapter(arrayData)
        recyclerView.adapter = RecyclerAdapter(arrayData)
    }

}
