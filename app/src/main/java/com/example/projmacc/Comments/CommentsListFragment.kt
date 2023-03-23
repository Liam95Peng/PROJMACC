package com.example.projmacc.Comments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.projmacc.R
import com.example.projmacc.databinding.FragmentCommentListBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class CommentsListFragment: Fragment() {
    private var _binding : FragmentCommentListBinding? = null
    private val binding get() = _binding!!
    var email:String ?=null
    var comment:String ?=null
    val bundle_m = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentListBinding.inflate(inflater, container, false)
        var listview= binding.listView
        var list = mutableListOf<CommentsModel>()
        val context: Context? = activity



        binding.load.setOnClickListener {
            var count:Int = 0
            val size = list.size
            while (count < size!! ){   //size not null

                //let the message visible
                listview.adapter = context?.let { CommentAdapter(it,R.layout.comment_list_item,list) }
                count++
            }
        }
        binding.back1.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_commentsFragment_to_homeFragment)
        }

        binding.post.setOnClickListener {
            val size = list.size
            Log.d("numbId",size.toString())
            bundle_m.putString("numbId", size.toString())
            bundle_m.putString("whereComment", "commentList")
            Navigation.findNavController(requireView()).navigate(R.id.action_commentsFragment_to_commentsPostFragment, bundle_m)
        }


            var client: OkHttpClient = OkHttpClient()
            var myArray:List<String>
            val name =
                "https://bosilou.pythonanywhere.com/getCommentList/"
            val request: Request = Request.Builder()
                .url(name)
                .build()

            client.newCall(request).enqueue(object : okhttp3.Callback {

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val body = response?.body?.string().toString()
                    Log.d("body", body)

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
                        list.add(CommentsModel(email, comment))
                        Log.d("list", list.toString())

                    }


                }

                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.d("response", "error")
                }
            })
        Thread.sleep(1000)

        var count:Int = 0
        val size = list.size
        while (count < size!! ){   //size not null

            //let the message visible
            listview.adapter = context?.let { CommentAdapter(it,R.layout.comment_list_item,list) }
            count++
        }

        listview.setOnItemClickListener{
                _: AdapterView<*>?, view:View, position:Int, _:Long ->
            email = list[position].email
            comment = list[position].comment
            Log.d("email: ", email.toString())
            Log.d("comment: ", comment.toString())
            bundle_m.putString("emailCommentList", email)
            bundle_m.putString("commentList", comment)
            bundle_m.putString("commentNumbId", position.toString())
            Navigation.findNavController(view).navigate(R.id.action_commentsFragment_to_commentsFragment2 , bundle_m)

        }

        return binding.root
    }
    override fun onPause() {
        Log.d("CommentsListPostFragmen.kt" , "onPause()")
        super.onPause()
    }




    override fun onResume() {
        Log.d("CommentsListPostFragmen.kt" , "onResume()")
        super.onResume()
    }


}
