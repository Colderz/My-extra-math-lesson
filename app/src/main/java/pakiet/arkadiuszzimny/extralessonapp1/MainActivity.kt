package pakiet.arkadiuszzimny.extralessonapp1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import pakiet.arkadiuszzimny.extralessonapp1.viewModel.DisplayListActivity
import pakiet.arkadiuszzimny.extralessonapp1.viewModel.EditableListActivity

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val listCard = findViewById<CardView>(R.id.list_card)
        val editCard = findViewById<CardView>(R.id.edit_card)

        listCard.setOnClickListener {
            val listIntent = Intent(this, DisplayListActivity::class.java)
            startActivity(listIntent)
        }

        editCard.setOnClickListener {
            val editIntent = Intent(this, EditableListActivity::class.java)
            startActivity(editIntent)
        }

    }

}