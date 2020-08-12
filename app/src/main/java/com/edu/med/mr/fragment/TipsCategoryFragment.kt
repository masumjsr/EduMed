package com.edu.med.mr.fragment

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.edu.med.*
import com.edu.med.mr.model.Model
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_tips.view.*
import kotlinx.android.synthetic.main.item_video_category.view.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

/**
 * A simple [Fragment] subclass.
 */
class TipsCategoryFragment : Fragment() {
    var recycler: RecyclerView? = null
    var progressDialog: ProgressDialog? = null
    var text: String? = null
    private lateinit var root: View
    private lateinit var category: ArrayList<VideoCategory>

    var question: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
     root = inflater.inflate(R.layout.fragment_tips, container, false)
        //   mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        category= ArrayList()

        Model.setI(0)

        progressDialog = ProgressDialog(activity)
        progressDialog!!.setMessage("loading")
        progressDialog!!.show()
        root.recycler_tips.layoutManager= LinearLayoutManager(activity)
      root.recycler_tips.adapter=VideoCategoryAdapter(category)



        getVideoList()
        return root
    }


    private fun getVideoList() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, "http://mrenglish.xyz/pharmacy/" + "tips_api.php",
                Response.Listener { response: String? ->
                    try {
                        progressDialog?.dismiss()
                        val jsonObject = JSONObject(response)
                        val array = jsonObject.getJSONArray("data")
                        Log.i("123321", "118:" + response)
                        Log.i("123321", "118:" + array.length())

                        for (i in 0 until array.length()){
                                val data: JSONObject =array.getJSONObject(i)
                                category.add(VideoCategory(data.getString("id"),data.getString("category_name"),data.getString("image"),""))
                                root.recycler_tips?.adapter?.notifyItemChanged(category.size)
                                Log.i("123321","61:"+category.size)
                                root.recycler_tips?.adapter?.notifyDataSetChanged()


                            }
                        /*  root.recycler.adapter=VideoTaskAdapter(category)*/
                    } catch (e: JSONException) {
                        Log.i("123321", "99" + e.message)
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
                                .inflate(R.layout.item_tips_category, parent, false)
                )

        override fun getItemCount(): Int {
            return videoCategoryList.size
        }

        @SuppressLint("InflateParams")
        override fun onBindViewHolder(holder: VideoCategoryViewHolder, position: Int) {

                holder.title.text = videoCategoryList[position].title
                holder.totalVideo.text = videoCategoryList[position].total_video + " Tips"
                if(videoCategoryList[position].image.isNotEmpty())
                Picasso.get().load(videoCategoryList[position].image).into(holder.icon)
            holder.main.setOnClickListener {
                val intent=Intent(holder.main.context,TipListActivity::class.java)
                intent.putExtra("category_id",videoCategoryList[position].id)
                holder.main.context.startActivity(intent)
            }


        }

        class VideoCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            val icon: ImageView = view.icon
            val title: TextView = view.title
            val totalVideo: TextView = view.total_video
            val main:LinearLayout=view.main

        }

    }
}