package com.example.memorina


import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private var firstOpenCard: ImageView? = null
    private var isCheckingPair = false

    private var pairsRemaining = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(applicationContext)
        layout.orientation = LinearLayout.VERTICAL

        val cardMargin = (2 * resources.displayMetrics.density).roundToInt()
        val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
            setMargins(cardMargin, cardMargin, cardMargin, cardMargin)
        }

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

        pairsRemaining = faces.size

        val shuffledCards = (faces + faces).shuffled()
        val catViews = ArrayList<ImageView>()

        for (face in shuffledCards) {
            catViews.add(
                ImageView(this).apply {
                    layoutParams = params
                    adjustViewBounds = true
                    foreground = context.getDrawable(R.drawable.card_outline)
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

    private val colorListener = View.OnClickListener { view ->
        if (isCheckingPair || !view.isClickable) return@OnClickListener

        val card = view as ImageView
        val face = card.tag as Int
        card.setImageResource(face)
        card.isClickable = false

        val firstCard = firstOpenCard
        if (firstCard == null) {
            firstOpenCard = card
            return@OnClickListener
        }

        firstOpenCard = null
        if (firstCard.tag == face) {
            pairsRemaining--

            if (pairsRemaining == 0) {
                AlertDialog.Builder(this)
                    .setMessage("Победа")
                    .setPositiveButton("Все", null)
                    .show()
            }

            return@OnClickListener
        }

        isCheckingPair = true
        lifecycleScope.launch {
            delay(1000L)
            firstCard.setImageResource(R.drawable.card_back)
            card.setImageResource(R.drawable.card_back)
            firstCard.isClickable = true
            card.isClickable = true
            isCheckingPair = false
        }
    }
}
