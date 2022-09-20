package com.example.assignmentlistview.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.example.assignmentlistview.R
import com.example.assignmentlistview.model.ItemModel
import okhttp3.*
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ImageListAdapter(private val context: Context, var imageList: List<ItemModel>) :
    RecyclerView.Adapter<ImageListAdapter.ImageHolder>() {

    private val glide = Glide.with(context)
    private val cache = DiskCacheStrategy.AUTOMATIC

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.assignment_list_item, parent, false)
        return ImageHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        var loadTime: Long = 0
        val requestTime = System.currentTimeMillis()

        val iv = imageList[position]

        glide.load(iv.myImage)
            .listener(object : com.bumptech.glide.request.RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    val displayTime = System.currentTimeMillis()
                    if (dataSource == DataSource.REMOTE || dataSource == DataSource.DATA_DISK_CACHE) {
                        loadTime = displayTime - requestTime
                        Log.d("loading_time", "Loading time of ${iv.myImage} is $loadTime ms")
                        trackImageLoadingTime(time = loadTime.toString(), url = iv.myImage)
                    }
                    return false
                }
            })
            .diskCacheStrategy(cache)
            .skipMemoryCache(false)
            .placeholder(R.drawable.ic_baseline_error)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.imageItem)
    }

    private fun trackImageLoadingTime(time: String, url: String) {
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()

        val formBody = FormBody
            .Builder()

        formBody.add(url, time)

        val requestBody: RequestBody = formBody
            .build()

        val request: Request = Request.Builder()
            .url("https://httpbin.org/")
            .post(requestBody)
            .build()

        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        executor.execute {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    //no operation
                }

                override fun onResponse(call: Call, response: Response) {
                    //no operation
                }
            })
        }
    }
}