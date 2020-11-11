package pakiet.arkadiuszzimny.extralessonapp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listCard = findViewById<CardView>(R.id.list_card)
        val formulaCard = findViewById<CardView>(R.id.formula_card)
        val editCard = findViewById<CardView>(R.id.edit_card)
        val moneyCard = findViewById<CardView>(R.id.money_card)

        listCard.setOnClickListener {
            val listIntent = Intent(this, DisplayListActivity::class.java)
            startActivity(listIntent)
        }

    }
}