package com.example.projmacc.Comments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.projmacc.Game.GoogleScoreRequest
import com.example.projmacc.R
import com.example.projmacc.databinding.FragmentCommentsBinding
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.wait
import org.json.JSONObject
import java.io.IOException


/**
 * A fragment representing a list of Items.
 */
class CommentsFragment : Fragment() {
    private var _binding : FragmentCommentsBinding? = null
    private val binding get() = _binding!!
    private var email:String ?=null
    private var comment:String ?=null
    private var numbId:String = ""
    val bundle_m = Bundle()
    private var columnCount = 1
    var listview: ListView? = null
    var list: MutableList<CommentsModel>? = null
    var loaded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentsBinding.inflate(inflater, container, false)

        listview= binding.list
        list = mutableListOf<CommentsModel>()
        val context: Context? = activity

        binding.back4.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_commentsFragment2_to_commentsFragment)
        }
        binding.load1.setOnClickListener {
            if (loaded){
                var count:Int = 0
                val size = list!!.size
                while (count < size!! ){   //size not null

                //let the message visible
                    listview!!.adapter = context?.let { RecyclerViewAdapter(it,R.layout.comments_item,list!!) }
                    count++
                }
            }else {
                Toast.makeText(
                    context, "There isn't comments",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        val bundle = this.arguments


        email= bundle?.getString("emailCommentList")
        comment= bundle?.getString("commentList")
        numbId = bundle?.getString("commentNumbId").toString()

        binding.personEmail2.text = email  //not list
        binding.comment2.text = comment

        GlobalScope.launch(Dispatchers.IO) {

             val asyncronous = async { var client: OkHttpClient = OkHttpClient()
                 var myArray:List<String>
                 var name: String =""
                 Log.d("nimbId" , numbId)
                 if (numbId == "0") {
                     Log.d("case" , "0")
                     name = "https://bosilou.pythonanywhere.com/getComment0/"
                 }else if(numbId == "1"){
                     Log.d("case" , "1")
                     name = "https://bosilou.pythonanywhere.com/getComment1/"
                 }else if(numbId == "2"){
                     Log.d("case" , "2")
                     name = "https://bosilou.pythonanywhere.com/getComment2/"
                 }else if (numbId == "3"){
                     Log.d("case" , "3")
                     name = "https://bosilou.pythonanywhere.com/getComment3/"
                 }else if(numbId == "4"){
                     Log.d("case" , "4")
                     name = "https://bosilou.pythonanywhere.com/getComment4/"
                 }else if(numbId == "5"){
                     Log.d("case" , "5")
                     name = "https://bosilou.pythonanywhere.com/getComment5/"
                 }
                 val request: Request = Request.Builder()
                     .url(name)
                     .build()

                 client.newCall(request).enqueue(object : okhttp3.Callback {

                     override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                         val body = response?.body?.string().toString()
                         Log.d("body", body)
                         if (body != "[]") {
                             var subString = body.substring(1, body.length - 1)
                             Log.d("subStr", subString)
                             myArray = subString.split("}, ");



                             for (item in myArray) {

                                 val newItem = item + "}"
                                 val jsonResponse = JSONObject(newItem)
                                 val email = jsonResponse.get("email").toString()
                                 val comment = jsonResponse.get("comment").toString()
                                 //Log.d("json", jsonResponse.toString())
                                 Log.d("json email", email)
                                 Log.d("json  comment", comment)
                                 list!!.add(CommentsModel(email, comment))
                                 Log.d("Array", list.toString())
                             }
                             loaded = true
                         }else{
                             Log.d("Comments", "There isn't comments")
                         }

                     }

                     override fun onFailure(call: okhttp3.Call, e: IOException) {
                         Log.d("response", "error")
                     }
                 })

             }


        }
        Thread.sleep(1000)
        if (loaded){
            var count:Int = 0
            val size = list!!.size
            while (count < size!! ){   //size not null

                //let the message visible
                listview!!.adapter = context?.let { RecyclerViewAdapter(it,R.layout.comments_item,list!!) }
                count++
            }
        }else {
            Toast.makeText(
                context, "There isn't comments",
                Toast.LENGTH_SHORT
            ).show()
        }


        // Set the adapter
        binding.commentThis.setOnClickListener {
            bundle_m.putString("numbCommentId", numbId )
            Log.d("numb", numbId.toString())
            Navigation.findNavController(requireView()).navigate(R.id.action_commentsFragment2_to_commentsPostFragment,bundle_m)
        }
        return binding.root
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            CommentsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
    override fun onPause() {
        Log.d("CommentsFragmen.kt" , "onPause()")
        super.onPause()
    }




    override fun onResume() {
        Log.d("CommentsFragmen.kt" , "onResume()")
        super.onResume()
    }




}