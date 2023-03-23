package com.example.projmacc.Home

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.projmacc.LoginResponse
import com.example.projmacc.R
import com.example.projmacc.databinding.FragmentHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.RequestBody.Companion.create
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("HomeFragmen.kt" , "onCreateView()")
        _binding = FragmentHomeBinding.inflate(inflater , container , false)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        lateinit var  mAuth: FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        if(mAuth.uid !=null){
            //if the current user is auth
            Log.d("email","logged : "+mAuth.uid)

        }else{
            Log.d("email or google","google logged")
        }
        val googleCredential = GoogleSignIn.getLastSignedInAccount(requireActivity())
        val googleId: String? = googleCredential?.id


        binding.challengeBtn.setOnClickListener{
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_gameViewFragment)
        }

        binding.rankBtn.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_rankFragment)
        }

        binding.commentsBtn.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_commentsFragment)
        }
        binding.logOutBtn.setOnClickListener{
            val googleCredential = GoogleSignIn.getLastSignedInAccount(requireActivity())

                var gso: GoogleSignInOptions
                gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(R.string.default_web_client_id)
                    .requestEmail()
                    .build()
                var gsc: GoogleSignInClient
                gsc = GoogleSignIn.getClient(requireActivity(),gso)

            if(googleCredential != null) {
                gsc.signOut()
                Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_loginFragment)
            }else{
                GlobalScope.launch(Dispatchers.IO) {
                    val request: EmailLogOutRequest = logOutUpdateRequest(
                        mAuth.uid.toString()
                    )
                    async { logOutUpdateUser(request) }
                }
            }

        }

        return binding.root
    }

    private fun googleScore() {
        val userResponseCall =ScoreApi.getUserService().getScore()

        userResponseCall.enqueue(object : Callback<GoogleScoreResponse?> {
            override fun onResponse(call: Call<GoogleScoreResponse?>, response: Response<GoogleScoreResponse?>){
                if (response.isSuccessful) {
                    Log.d("Score: ", response.body()!!.getScore())
                    Log.d("Score: ", "success")
                    Toast.makeText( context, "score success",
                        Toast.LENGTH_SHORT).show()


                } else {

                    Log.d("get score: ", "error")
                    Toast.makeText( context, "not get score ",
                        Toast.LENGTH_SHORT).show()

                }
            }
            override fun onFailure(call: Call<GoogleScoreResponse?>, t: Throwable) {

                Log.d("update", "Request Failed" + t.localizedMessage)
            }

        })
    }

    fun logOutUpdateRequest(emailId: String?): EmailLogOutRequest {
        val registRequest = EmailLogOutRequest()
        try {
            registRequest.setId(emailId!!)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return registRequest
    }
    fun logOutUpdateUser(registRequest: EmailLogOutRequest?) {
        val userResponseCall = EmailLogOutClient.getUserService().logOutCall(registRequest)

        userResponseCall.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(call: Call<LoginResponse?>, response: Response<LoginResponse?>){
                if (response.isSuccessful) {

                    Log.d("logout: ", "success")
                    Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_loginFragment)

                } else {

                    Log.d("logout", "error")


                }
            }
            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {

                Log.d("logout", "Request Failed" + t.localizedMessage)
            }

        })

    }

    override fun onPause() {
        Log.d("HomeFragmen.kt" , "onPause()")
        super.onPause()
    }

    override fun onStart() {
        Log.d("HomeFragmen.kt" , "onStart()")
        super.onStart()
    }


    override fun onResume() {
        Log.d("HomeFragmen.kt" , "onResume()")
        super.onResume()
    }
}

