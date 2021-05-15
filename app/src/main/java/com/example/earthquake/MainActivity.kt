package com.example.earthquake

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.earthquake.databinding.ActivityMainBinding
import com.example.earthquake.databinding.ListItemBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private lateinit var activityBinding : ActivityMainBinding

    private val ourAdapter : OurAdapter by lazy {
        OurAdapter(this, ArrayList())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        activityBinding.listView.adapter = ourAdapter

        activityBinding.retryButton.setOnClickListener{
            fetchDataFromNetwork()
        }

        fetchDataFromNetwork()
    }


    @SuppressLint("SetTextI18n")
    fun fetchDataFromNetwork() {

        activityBinding.progressBar.visibility = View.VISIBLE
        activityBinding.listView.visibility = View.INVISIBLE
        activityBinding.errorLabel.visibility = View.INVISIBLE
        activityBinding.retryButton.visibility = View.INVISIBLE

        val queue = Volley.newRequestQueue(this)
        val url = "https://jsonplaceholder.typicode.com/posts"

        // Request a string response from the provided URL.
        val stringRequest = StringRequest( Request.Method.GET, url, { response ->

            val listType = object : TypeToken<List<Data>>() {}.type
            val list : List<Data> = Gson().fromJson(response, listType)

            activityBinding.progressBar.visibility = View.INVISIBLE
            activityBinding.listView.visibility = View.VISIBLE
            activityBinding.errorLabel.visibility = View.INVISIBLE
            activityBinding.retryButton.visibility = View.INVISIBLE

            ourAdapter.clear()
            ourAdapter.addAll(list)
            ourAdapter.notifyDataSetChanged()
        },
            {
                activityBinding.progressBar.visibility = View.INVISIBLE
                activityBinding.listView.visibility = View.INVISIBLE
                activityBinding.errorLabel.visibility = View.VISIBLE
                activityBinding.retryButton.visibility = View.VISIBLE
                Toast.makeText(this, "Some error occurred.", Toast.LENGTH_SHORT).show()
            }
        )

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    class OurAdapter(context: Context, values : ArrayList<Data>) : ArrayAdapter<Data>(context, 0, values){
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view : View? = convertView
            if (view == null){
                view = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
            }

            val data = getItem(position)!!
            view.findViewById<TextView>(R.id.post_title).text = data.title
            view.findViewById<TextView>(R.id.post_body).text = data.body

            return view
        }
    }



}

data class Data(
    val id : Int,
    val userId : Int,
    val title : String,
    val body : String
)