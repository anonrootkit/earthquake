package com.example.earthquake

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.earthquake.databinding.ActivityMainBinding
import com.example.earthquake.databinding.ListItemBinding
import com.example.earthquake.databinding.PhotoListItemBinding
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private lateinit var activityBinding : ActivityMainBinding

    private val ourAdapter : OurAdapter by lazy {
        OurAdapter(this, ArrayList())
    }

    private val photoAdapter : PhotosAdapter by lazy {
        PhotosAdapter(this, ArrayList())
    }

    // Instantiate the RequestQueue.
    private val volleyQueue : RequestQueue by lazy {
        Volley.newRequestQueue(this)
    }

    private val newAdapter : NewAdapter by lazy {
        NewAdapter()
    }

    private val url = "https://jsonplaceholder.typicode.com/photos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        activityBinding.listView.adapter = newAdapter

        activityBinding.retryButton.setOnClickListener{
//            fetchDataFromNetwork()
            fetchPhotosFromNetwork()
        }

//        fetchDataFromNetwork()
        fetchPhotosFromNetwork()
    }

    private fun fetchDataFromNetwork() {

        activityBinding.progressBar.visibility = View.VISIBLE
        activityBinding.listView.visibility = View.INVISIBLE
        activityBinding.errorLabel.visibility = View.INVISIBLE
        activityBinding.retryButton.visibility = View.INVISIBLE

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
        }, {
                activityBinding.progressBar.visibility = View.INVISIBLE
                activityBinding.listView.visibility = View.INVISIBLE
                activityBinding.errorLabel.visibility = View.VISIBLE
                activityBinding.retryButton.visibility = View.VISIBLE
                Toast.makeText(this, "Some error occurred.", Toast.LENGTH_SHORT).show()
            })

        // Add the request to the RequestQueue.
        volleyQueue.add(stringRequest)
    }

    private fun fetchPhotosFromNetwork() {

        activityBinding.progressBar.visibility = View.VISIBLE
        activityBinding.listView.visibility = View.INVISIBLE
        activityBinding.errorLabel.visibility = View.INVISIBLE
        activityBinding.retryButton.visibility = View.INVISIBLE

        // Request a string response from the provided URL.
        val stringRequest = StringRequest( Request.Method.GET, url, { response ->

            val listType = object : TypeToken<List<Photo>>() {}.type
            val list : List<Photo> = Gson().fromJson(response, listType)

            activityBinding.progressBar.visibility = View.INVISIBLE
            activityBinding.listView.visibility = View.VISIBLE
            activityBinding.errorLabel.visibility = View.INVISIBLE
            activityBinding.retryButton.visibility = View.INVISIBLE

            newAdapter.submitList(list)
        }, {
                activityBinding.progressBar.visibility = View.INVISIBLE
                activityBinding.listView.visibility = View.INVISIBLE
                activityBinding.errorLabel.visibility = View.VISIBLE
                activityBinding.retryButton.visibility = View.VISIBLE
                Toast.makeText(this, "Some error occurred.", Toast.LENGTH_SHORT).show()
            })

        // Add the request to the RequestQueue.
        volleyQueue.add(stringRequest)
    }



    class PhotosAdapter(context: Context, values : ArrayList<Photo>) : ArrayAdapter<Photo>(context, 0, values){
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view : View? = convertView
            if (view == null){
                view = PhotoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
            }

            val data = getItem(position)!!
            val thumbnail = view.findViewById<ImageView>(R.id.thumbnail)
            view.findViewById<TextView>(R.id.post_title).text = data.title
            view.findViewById<TextView>(R.id.post_body).text = data.thumbnail

            Glide.with(thumbnail.context).load(data.thumbnail).placeholder(R.mipmap.ic_launcher).error(
                "data:image/jpeg;base64,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"
            ).into(thumbnail)

            return view
        }
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

data class Photo(
    val albumId : Int,
    val title : String,
    @SerializedName("thumbnailUrl")
    val thumbnail : String
)

data class Data(
    val id : Int,
    val userId : Int,
    val title : String,
    val body : String
)