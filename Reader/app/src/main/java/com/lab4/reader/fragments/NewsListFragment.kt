package com.lab4.reader.fragments

import android.content.Context
import android.content.Intent
import android.nfc.Tag
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.loader.content.AsyncTaskLoader
import androidx.recyclerview.widget.LinearLayoutManager
import com.lab4.reader.NewsItem
import com.lab4.reader.R
import com.lab4.reader.WebActivity
import com.lab4.reader.adapter.NewsAdapter
import kotlinx.android.synthetic.main.fragment_news_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class NewsListFragment : Fragment() {

    private lateinit var urlPath: String
    private lateinit var adapter: NewsAdapter
    private var newsItems = ArrayList<NewsItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news_list, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(url: String) =
            NewsListFragment().apply {
                this.urlPath = url
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NewsAdapter(newsItems, onClicklistener, requireActivity())

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch {
            getXml(urlPath)
        }
    }

    private val onClicklistener = object : NewsAdapter.OnItemClickListener {

        override fun onClicked(link: String?) {

            if (link != null) {
                val intent = Intent(context, WebActivity::class.java)
                intent.putExtra("url", link)
                startActivity(intent)
            }
        }
    }


    private suspend fun getXml(urlPath: String) {

        withContext(Dispatchers.IO) {
            var inputStream: InputStream? = null
            try {

                val url = URL(urlPath)

                val connection: HttpsURLConnection = url.openConnection() as HttpsURLConnection

                if (connection.responseCode == 200) {
                    inputStream = connection.inputStream
                    cashXml(inputStream)
                }
            } catch (e: Exception) {
                Log.e(tag, e.toString())
            }

            inputStream = getCashedXml()

            if (inputStream == null) {
                return@withContext
            }
            try {
                val xmlFactory = XmlPullParserFactory.newInstance()
                xmlFactory.isNamespaceAware = false

                val xmlParser = xmlFactory.newPullParser()
                xmlParser.setInput(inputStream, "UTF_8")

                var insideItem = false
                var eventType = xmlParser.eventType
                var newsItem: NewsItem? = null

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {
                        when {
                            xmlParser.name.equals("item", true) -> {
                                insideItem = true
                                newsItem = NewsItem()
                            }
                            xmlParser.name.equals("title", true) and insideItem -> {
                                newsItem?.title = xmlParser.nextText()
                            }
                            xmlParser.name.equals("link", true) and insideItem -> {
                                newsItem?.link = xmlParser.nextText()
                            }
                            xmlParser.name.equals("pubDate", true) and insideItem -> {
                                newsItem?.date = xmlParser.nextText()
                            }
                            xmlParser.name.equals("description", true) and insideItem -> {
                                newsItem?.description = xmlParser.nextText()
                            }
                            xmlParser.name.equals("enclosure", true) and insideItem -> {
                                newsItem?.enclosure = xmlParser.getAttributeValue(null, "url")
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xmlParser.name.equals(
                            "item",
                            true
                        )
                    ) {
                        if (newsItem != null) {
                            newsItems.add(newsItem)
                        }
                        insideItem = false
                    }

                    eventType = xmlParser.next()
                }
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                Log.e(tag, e.toString())
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun cashXml(stream: InputStream) {
        val file = File(context?.filesDir, "news.txt")

        if (!file.exists())
            file.createNewFile()

        file.writeBytes(stream.readBytes())
    }

    private fun getCashedXml(): InputStream? {
        val file = File(context?.filesDir, "news.txt")

        return if (file.exists())
            file.inputStream()
        else null
    }

}