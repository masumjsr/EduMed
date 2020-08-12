package com.edu.med.mr.fragment

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.edu.med.BcsPlay
import com.edu.med.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dialog.view.*
import kotlinx.android.synthetic.main.layout.view.*
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout, container, false)
        view.contest.setOnClickListener {


            val dialogBuilder =
                    activity?.let { it1 -> AlertDialog.Builder(it1) }
            // ...Irrelevant code for customizing the buttons and title
            // ...Irrelevant code for customizing the buttons and title
            val inflater = layoutInflater
            val dialogView: View = inflater.inflate(R.layout.dialog, null)
            dialogBuilder?.setView(dialogView)
            var checked=false

            val alertDialog = dialogBuilder?.create()
            dialogView.agree.isClickable=false
            dialogView.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->checked=isChecked }
            dialogView.agree.setOnClickListener {
             
                if(checked) {
                    // startActivity(Intent(activity,BcsPlay::class.java))
                    alertDialog?.dismiss()


                    // Required empty public constructor
                    val format = SimpleDateFormat("yyyyMMdd")
                    Log.i("123321", "93:" + format.format(Date()))

                    val preferences: SharedPreferences? = activity?.getSharedPreferences("quiz", Context.MODE_PRIVATE)


                    if (preferences != null) {
                        if (preferences.getBoolean(format.format(Date()), false)) {
                            Toast.makeText(context,"Try Again Tomorrow",Toast.LENGTH_SHORT).show()

                        } else {
                            val editor: SharedPreferences.Editor? = activity?.getSharedPreferences("quiz", Context.MODE_PRIVATE)?.edit()

                            editor?.putBoolean(format.format(Date()), true )
                            editor?.apply()
                            startActivity(Intent(activity, BcsPlay::class.java))
                        }
                    }
                }
            }

            alertDialog?.show()
        }
        view.leaderboard.setOnClickListener {
            startActivity(Intent(activity,ExamActivity::class.java))
        }

        return view
    }

    private fun submit() {
        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("loading")
        progressDialog.show()
        val stringRequest: StringRequest = object : StringRequest(Method.POST, "http://mrenglish.xyz/pharmacy/" + "check_score_api.php",
                Response.Listener { response: String ->
                    Log.i("123321", "74     $response")
                    var `object`: JSONObject? = null
                    try {
                        progressDialog.dismiss()
                        `object` = JSONObject(response)
                       if(`object`.getString("message") == "Data Exits!"){
                           Toast.makeText(context,"Try Again Tomorrow",Toast.LENGTH_SHORT).show()
                       }

                        else{
                         startActivity(Intent(activity, BcsPlay::class.java))
                           Toast.makeText(context,"work",Toast.LENGTH_SHORT).show()
                       }

                    } catch (e: JSONException) {
                        Log.i("123321","82:"+e.message)
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error: VolleyError ->
                    Log.i("123321", "164:$error")
                    // As of f605da3 the following should work
                }) {
            override fun getParams(): Map<String, String> {
                val parameters: MutableMap<String, String> = HashMap()
                Log.i("123321",""+FirebaseAuth.getInstance().currentUser?.uid.toString())
                parameters["access_key"] = "6808"
                parameters["check_point"] = "1"
                parameters["user_id"]=FirebaseAuth.getInstance().currentUser?.uid.toString()
                return parameters
            }
        }

        //adding our stringrequest to queue
        Volley.newRequestQueue(activity).add(stringRequest)
    }

}