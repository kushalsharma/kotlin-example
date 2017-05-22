package xyz.kushal.kotlinexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import xyz.kushal.extentions.toast


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById(R.id.main_activity_recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerAdapter(recyclerList = listOf("Google", "Square", "Flipkart")) { recyclerItemText ->
            toast(recyclerItemText)
        }
    }
}

class RecyclerAdapter(val recyclerList: List<String>, val itemClicked: (String) -> Unit) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    override fun onBindViewHolder(viewHolder: RecyclerAdapter.ViewHolder, position: Int) {
        viewHolder.bind(recyclerList[position])
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): RecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.recycler_row, viewGroup, false)
        return ViewHolder(itemView = view, itemClicked = itemClicked)
    }

    override fun getItemCount(): Int {
        return recyclerList.count()
    }

    class ViewHolder(itemView: View, val itemClicked: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val itemTextView = itemView.findViewById(R.id.recycler_row_text_view) as TextView
        fun bind(recyclerItemText: String) {
            itemTextView.text = recyclerItemText
            itemTextView.setOnClickListener { itemClicked(recyclerItemText) }
        }
    }
}