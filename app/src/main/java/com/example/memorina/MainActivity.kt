package com.example.memorina


import android.app.ActionBar
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        val layout = LinearLayout(applicationContext)
        layout.orientation = LinearLayout.VERTICAL

        val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.weight = 1.toFloat() // единичный вес


        val faces = listOf(
            R.drawable.cat_blue,
            R.drawable.cat_gold,
            R.drawable.cat_green,
            R.drawable.cat_orange,
            R.drawable.cat_pink,
            R.drawable.cat_purple,
            R.drawable.cat_slate,
            R.drawable.cat_teal
        )

        val shuffledCards = (faces + faces).shuffled()
        val catViews = ArrayList<ImageView>()

        for (face in shuffledCards) {
            catViews.add(
                ImageView(this).apply {
                    layoutParams = params
                    adjustViewBounds = true
                    setImageResource(R.drawable.card_back)
                    tag = face
                    setOnClickListener(colorListener)
                }
            )
        }

        val rows = Array(4, { LinearLayout(applicationContext)})

        var count = 0
        for (view in catViews) {
            val row: Int = count / 4
            rows[row].addView(view)
            count ++
        }
        for (row in rows) {
            layout.addView(row)
        }
        setContentView(layout)
    }

    suspend fun setBackgroundWithDelay(v: View) {
        delay(1000)
        v.setBackgroundColor(Color.YELLOW)
        delay(1000)
        v.visibility = View.INVISIBLE
        v.isClickable = false
    }

    suspend fun openCards() {

    }

    // обработчик нажатия на кнопку
    val colorListener = View.OnClickListener { view ->
        val card = view as ImageView
        card.setImageResource(card.tag as Int)
        card.isClickable = false

        card.postDelayed({
            card.setImageResource(R.drawable.card_back)
            card.isClickable = true
        }, 1000L)
    }
}