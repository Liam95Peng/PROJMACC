package com.example.projmacc.Comments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.projmacc.R
import com.example.projmacc.databinding.FragmentCommentsPostBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CommentsPostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommentsPostFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentCommentsPostBinding? = null
    private var whereComment:String = ""
    private val binding get() = _binding!!

    private lateinit var sendBnt: Button

    private var email: String= ""
    private var comment:String? = ""
    private var numbId:String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentsPostBinding.inflate(inflater, container, false)
        sendBnt = binding.sendBtn

        val bundle = this.arguments
        whereComment = bundle?.getString("whereComment").toString()
        // Inflate the layout for this fragment
        val user = FirebaseAuth.getInstance().currentUser
        val googleCredential = GoogleSignIn.getLastSignedInAccount(requireActivity())

        if( googleCredential != null){
            email = googleCredential.email.toString()
            Log.d("email:", email)
        }else{
            email = user!!.email.toString()
            Log.d("email:", email)
        }
        binding.back3.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_commentsPostFragment_to_commentsFragment)
        }
        sendBnt.setOnClickListener {
            comment = binding.inputMessage.text.toString()

            if (whereComment == "commentList") {
                Log.d("List case","here")
                numbId = bundle?.getString("numbId")
                Log.d("numb", numbId.toString())
                GlobalScope.launch(Dispatchers.IO) {
                    val request: CommentRequest = CommentPostRequest(
                        numbId.toString(),
                        email, comment.toString()
                    )
                    async {
                        createCommentList(request)

                    }
                }

            }else{// comment the post
                numbId = bundle?.getString("numbCommentId")
                Log.d("commentNumbId", numbId.toString())
                GlobalScope.launch(Dispatchers.IO) {
                    val commentsRequest: CommentsRequest = CommentssRequest(
                        numbId.toString(),
                        email, comment.toString()
                    )
                    async {
                        createComment(commentsRequest)

                    }
                }


            }
            binding.inputMessage.setText("")
        }
        return binding.root

    }

    private fun createComment(requests: CommentsRequest) {
        val userResponseCall = CommentApi.getUserService().createComment(requests)

        userResponseCall.enqueue(object : Callback<CommentsResponse?> {
            override fun onResponse(call: Call<CommentsResponse?>, response: Response<CommentsResponse?>){
                if (response.isSuccessful) {

                    Log.d("create post: ", "success")
                    Toast.makeText( context, "post created",
                        Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(requireView()).navigate(R.id.action_commentsPostFragment_to_commentsFragment)

                } else {

                    Log.d("create post", "error")
                    Toast.makeText( context, "post not created",
                        Toast.LENGTH_SHORT).show()

                }
            }
            override fun onFailure(call: Call<CommentsResponse?>, t: Throwable) {

                Log.d("post request", "Request Failed" + t.localizedMessage)
            }

        })
    }

    private fun createCommentList(request: CommentRequest) {
        val userResponseCall = CommentApi.getUserService().createCommentList(request)

        userResponseCall.enqueue(object : Callback<CommentResponse?> {
            override fun onResponse(call: Call<CommentResponse?>, response: Response<CommentResponse?>){
                if (response.isSuccessful) {

                    Log.d("create post: ", "success")
                    Toast.makeText( context, "post created",
                        Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(requireView()).navigate(R.id.action_commentsPostFragment_to_commentsFragment)

                } else {

                    Log.d("create post", "error")
                    Toast.makeText( context, "post not created",
                        Toast.LENGTH_SHORT).show()

                }
            }
            override fun onFailure(call: Call<CommentResponse?>, t: Throwable) {

                Log.d("post request", "Request Failed" + t.localizedMessage)
            }

        })
    }

    private fun CommentssRequest(idNumb: String ,email: String, comment: String): CommentsRequest {
        val commentRequest = CommentsRequest()
        try {
            commentRequest.setNumbId(idNumb)
            commentRequest.setEmail(email)
            commentRequest.setComment(comment)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return commentRequest
    }

    private fun CommentPostRequest(idNumb: String ,email: String, comment: String): CommentRequest {
        val registRequest = CommentRequest()
        try {
            registRequest.setNumbId(idNumb)
            registRequest.setEmail(email)
            registRequest.setComment(comment)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return registRequest
    }

    /*override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/
    override fun onPause() {
        Log.d("CommentsPostFragmen.kt" , "onPause()")
        super.onPause()
    }




    override fun onResume() {
        Log.d("CommentsPostFragmen.kt" , "onResume()")
        super.onResume()
    }
    /*
    override fun onDestroy() {
        Log.d("CommentsPostFragmen.kt" , "onDestroy()")
        super.onDestroy()
    }
    override fun onStop() {
        Log.d("CommentsPostFragmen.kt" , "onStop()")
        super.onStop()
    }*/


}
