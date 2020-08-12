package com.edu.med

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main_category.view.*
import kotlinx.android.synthetic.main.item_video_category.view.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MainCategoryFragment : Fragment() {
    private lateinit var root: View
    var progressDialog: ProgressDialog? = null
    private lateinit var category: ArrayList<VideoCategory>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.activity_main_category, container, false)

        progressDialog = ProgressDialog(activity)
        progressDialog!!.setMessage("loading")
        progressDialog!!.show()
        category= ArrayList()

        root.recycler.layoutManager= GridLayoutManager(activity, 2)
        root.recycler.adapter=VideoCategoryAdapter(category)



       getVideoList()
        return root
    }

    private fun getVideoList() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, Constant.BASE_URL + "video_api.php",
                Response.Listener { response: String? ->
                    try {
                        progressDialog?.dismiss()
                        val jsonObject = JSONObject(response)
                        val array = jsonObject.getJSONArray("data")

                        for (i in 0 until array.length()){
                            val data:JSONObject=array.getJSONObject(i)
                                category.add(VideoCategory(data.getString("id"),data.getString("category_name"),data.getString("image"),data.getString("total_video")))
                            root.recycler.adapter?.notifyItemChanged(category.size)
                         
                            root.recycler.adapter?.notifyDataSetChanged()


                    }
                      /*  root.recycler.adapter=VideoTaskAdapter(category)*/
                    } catch (e: JSONException) {
                        Log.i("123321", "" + e.message)
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error: VolleyError -> Log.i("123321", "error = $error") }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["access_key"] = "6808"
                params["get_categories"] = "1"
                println("params  $params")
                return params
            }
        }
        val queue = Volley.newRequestQueue(activity)
        queue.add(stringRequest)

    }


    class VideoCategoryAdapter(
            private val videoCategoryList: ArrayList<VideoCategory>
    ) :
            RecyclerView.Adapter<VideoCategoryAdapter.VideoCategoryViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                VideoCategoryViewHolder(
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.item_video_category, parent, false)
                )

        override fun getItemCount(): Int {
            return videoCategoryList.size
        }

        @SuppressLint("InflateParams")
        override fun onBindViewHolder(holder: VideoCategoryViewHolder, position: Int) {
                holder.title.text=videoCategoryList[position].title
                holder.totalVideo.text=videoCategoryList[position].total_video+" Videos"
            if(videoCategoryList[position].image.isNotEmpty())
                Picasso.get().load(videoCategoryList[position].image).into(holder.icon)
                holder.main.setOnClickListener {
                    val intent = Intent(holder.main.context, VideoListActivity::class.java)
                    intent.putExtra("category_id", videoCategoryList[position].id)
                    if(videoCategoryList[position].total_video.toInt()> 0)
                        holder.main.context.startActivity(intent)
                    else{
                      val builder = AlertDialog.Builder(holder.main.context);
        builder.setTitle("No Videos in This Category")
        builder.setPositiveButton("ok",null )
        builder.show()
                    }
                }


        }

        class VideoCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val main:LinearLayout=view.main
            val icon:ImageView=view.icon
            val title:TextView=view.title
            val totalVideo:TextView=view.total_video

        }

    }
}