package com.lab4.reader.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.lab4.reader.NewsItem
import com.lab4.reader.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rv_news_item.view.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URL

class NewsAdapter(
    private var newsItems: ArrayList<NewsItem>,
    private var clickListener: OnItemClickListener,
    private val context: FragmentActivity
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_news_item, parent, false)

        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.itemView.title.text = newsItems[position].title
        holder.itemView.date.text = newsItems[position].date
        var url = extractImageLink(newsItems[position].description)

        if (url == null){
            url = newsItems[position].enclosure
        }

        if (!url.isNullOrEmpty()) {
            try {
                context.let {
                    Picasso.with(it)
                        .load(url)
                        .into(holder.itemView.image)
                }
                holder.itemView.image.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        holder.itemView.setOnClickListener {
            clickListener.onClicked(newsItems[position].link)
        }
    }

    override fun getItemCount(): Int {
        return newsItems.size
    }

    interface OnItemClickListener {
        fun onClicked(link: String?)
    }

    private fun extractImageLink(html: String): String? {

        try {
            val doc: Document = Jsoup.parse(html)

            val img = doc.select("img").first()

            return img.attr("src")

        } catch (e: Exception) {
            Log.e("", e.toString())
        }

        return null
    }
}