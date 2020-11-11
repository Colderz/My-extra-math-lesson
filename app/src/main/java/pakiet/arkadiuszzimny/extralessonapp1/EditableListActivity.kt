package pakiet.arkadiuszzimny.extralessonapp1

import android.icu.text.Transliterator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.SearchEvent
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.util.*

class EditableListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapter

    private var studentsList = mutableListOf<String>()
    private var displayList = mutableListOf<String>()

    private lateinit var deletedStudent: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editable_list)

        studentsList.add("Karolina")
        studentsList.add("Eliza")
        studentsList.add("Roksana")
        studentsList.add("Oliwia")
        studentsList.add("Jasiek")
        studentsList.add("Bliźniaczki")
        studentsList.add("Magda")
        studentsList.add("Katarzyna")

        displayList.addAll(studentsList)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerAdapter = RecyclerAdapter(displayList)

        recyclerView.adapter = recyclerAdapter
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN), ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            var startPosition = viewHolder.adapterPosition
            var endPosition = target.adapterPosition

            Collections.swap(displayList, startPosition, endPosition)
            recyclerView.adapter?.notifyItemMoved(startPosition, endPosition)
            return true
        }

        fun undoDeleted(position: Int) {
            Snackbar.make(recyclerView, "Usunięto ucznia: $deletedStudent", Snackbar.LENGTH_LONG).setAction("Cofnij", View.OnClickListener {
                displayList.add(position, deletedStudent)
                recyclerAdapter.notifyItemInserted(position)
            }).show()
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var position = viewHolder.adapterPosition

            when(direction)  {
                ItemTouchHelper.LEFT -> {
                    deletedStudent = displayList.get(position)
                    displayList.removeAt(position)
                    recyclerAdapter.notifyItemRemoved(position)
                    undoDeleted(position)
                }
                ItemTouchHelper.RIGHT -> {
                    deletedStudent = displayList.get(position)
                    displayList.removeAt(position)
                    recyclerAdapter.notifyItemRemoved(position)
                    undoDeleted(position)
                }
            }
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

                        for(student in studentsList) {
                            if(student.toLowerCase(Locale.getDefault()).contains(search)) {
                                displayList.add(student)
                            }
                            recyclerView.adapter!!.notifyDataSetChanged()
                        }
                    }else {
                        displayList.clear()
                        displayList.addAll(studentsList)
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                    return true
                }

            })
        }

        return super.onCreateOptionsMenu(menu)
    }

}