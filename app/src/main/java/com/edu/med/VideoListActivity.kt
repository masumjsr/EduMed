package com.edu.med

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_video_list.*
import kotlinx.android.synthetic.main.video_layoutx.view.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.Map
import kotlin.collections.MutableMap
import kotlin.collections.set

class VideoListActivity : AppCompatActivity() {
    var recycler: RecyclerView? = null
    var progressDialog: ProgressDialog? = null
    var path: String? = null
    private lateinit var category: ArrayList<VideoList>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("loading")
        progressDialog!!.show()
        category = ArrayList()

        recycler_video?.layoutManager = LinearLayoutManager(this)
        recycler_video?.adapter = VideoCategoryAdapter(category)



        getVideoList()
    }


    private fun getVideoList() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, "http://mrenglish.xyz/pharmacy/" + "video_api.php",
                Response.Listener { response: String? ->
                    try {
                        progressDialog?.dismiss()
                        val jsonObject = JSONObject(response)
                        val array = jsonObject.getJSONArray("data")

                        for (i in 0 until array.length()) {
                            val data: JSONObject = array.getJSONObject(i)
                            category.add(VideoList(data.getString("question"), data.getString("image")))
                            recycler_video?.adapter?.notifyItemChanged(category.size)
                            recycler_video?.adapter?.notifyDataSetChanged()


                        }
                        Log.i("123321","70:"+category.size)
                        /*  root.recycler.adapter=VideoTaskAdapter(category)*/
                    } catch (e: JSONException) {
                        Log.i("123321", "72" + e.message)
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error: VolleyError -> Log.i("123321", "error = $error") }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["access_key"] = "6808"
                params["category"] = intent.getStringExtra("category_id")!!
                params["get_video_by_category"] = "1"
                println("params  $params")
                return params
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(stringRequest)

    }


    class VideoCategoryAdapter(
            private val category: ArrayList<VideoList>
    ) :
            RecyclerView.Adapter<VideoCategoryAdapter.VideoCategoryViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                VideoCategoryViewHolder(
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.video_layoutx, parent, false)
                )

        override fun getItemCount(): Int {
            return category.size
        }

        @SuppressLint("InflateParams")
        override fun onBindViewHolder(holder: VideoCategoryViewHolder, position: Int) {
            if(category[position].image.isNotEmpty())
            Picasso.get().load(category[position].image).into(holder.image)
            holder.button.setOnClickListener {

                Log.i("123321","114:"+category[position].link)
                try {
                    getLink(category[position].link.toInt(),holder.button.context)
                } catch (e: Exception) {
                }

            }


        }

        class VideoCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            val image: ImageView = view.nice_video_player
            val button: ImageView = view.imageButton

        }


        private fun getLink(video_id: Int, context: Context) {
            val url = "https://player.vimeo.com/video/$video_id/config"

            val request: StringRequest = object : StringRequest(Method.GET, url, Response.Listener { response: String ->
                Log.i("123321", "30:$response")
                try {

                    val `object` = JSONObject(response).getJSONObject("request").getJSONObject("files")
                    val array = `object`.getJSONArray("progressive")
                    val intent = Intent(context, Main3Activity_x::class.java)
                    intent.putExtra("link", array.getJSONObject(2).getString("url"))
                    context.startActivity(intent)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.i("123321", "440:" + e.message)

                }
            } as Response.Listener<String>, Response.ErrorListener { error: VolleyError ->
                Log.i("123321", "32:$error")
                Toast.makeText(context, "Fail :( \n$error", Toast.LENGTH_SHORT).show()

            }) {
                override fun getParams(): Map<String, String> {
                    return HashMap()
                }
            }
            val requestQueue = Volley.newRequestQueue(context)
            requestQueue.add(request)
        }

    }
}