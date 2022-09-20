package com.example.assignmentlistview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentlistview.adapter.ImageListAdapter
import com.example.assignmentlistview.image.InfoData

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myRv = findViewById<RecyclerView>(R.id.myRecyclerView)
        myRv.layoutManager = LinearLayoutManager(this)
        myRv.adapter = ImageListAdapter(this, InfoData.getInfoData())
    }
}