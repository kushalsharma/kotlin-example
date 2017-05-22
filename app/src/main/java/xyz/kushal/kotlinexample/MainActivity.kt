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
import xyz.kushal.extentions.updateDataList

class MainActivity : AppCompatActivity() {
    val dataList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById(R.id.main_activity_recycler_view) as RecyclerView
        val layoutManager = LinearLayoutManager(this)
        val adapter = RecyclerAdapter(recyclerList = updateDataList(dataList)) { recyclerItemText ->
            toast(recyclerItemText.toString())
        }
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(OnScrollListener(layoutManager, adapter, dataList))
    }
}

class RecyclerAdapter(val recyclerList: List<Int>, val itemClicked: (Int) -> Unit) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
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

    class ViewHolder(itemView: View, val itemClicked: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val itemTextView = itemView.findViewById(R.id.recycler_row_text_view) as TextView

        fun bind(recyclerItemText: Int) {
            itemTextView.text = recyclerItemText.toString()
            itemTextView.setOnClickListener { itemClicked(recyclerItemText) }
        }
    }
}

class OnScrollListener(val layoutManager: LinearLayoutManager, val adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>, val dataList: MutableList<Int>) : RecyclerView.OnScrollListener() {
    var previousTotal = 0
    var loading = true
    val visibleThreshold = 10
    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount
        totalItemCount = layoutManager.itemCount
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            val initialSize = dataList.size
            updateDataList(dataList)
            val updatedSize = dataList.size
            recyclerView.post { adapter.notifyItemRangeInserted(initialSize, updatedSize) }
            loading = true
        }
    }
}