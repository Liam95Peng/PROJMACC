package com.example.projmacc


import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.projmacc.Comments.CommentsModel
import com.example.projmacc.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient

    private lateinit var auth: FirebaseAuth
    private lateinit var googleId:String

    var list = mutableListOf<EmailIDModel>()
    private companion object{
        private const val  RC_SIGN_IN = 100
        private const val TAGG = "GOOGLE_SIGN_IN_TAG"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR

        //login with email and pass
        val emailEditText = binding.email
        val passwordEditText = binding.password
        val credential = GoogleSignIn.getLastSignedInAccount(requireActivity())

        googleId = credential?.id.toString()
        binding.login.setOnClickListener{
            val emailText = emailEditText.text.toString()
            val passwordText = passwordEditText.text.toString()

            if(!emailText.contains("@") || emailText == "") {
                Toast.makeText( context, "Email error",
                    Toast.LENGTH_SHORT).show()
            }else if(passwordText.length < 6){
                Toast.makeText( context, "password error",
                    Toast.LENGTH_SHORT).show()
            }else {
                signIn(binding.email.text.toString(), binding.password.text.toString())
            }
        }
        binding.register.setOnClickListener{
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registerFragment)
        }

        //log in with google account
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestIdToken(R.string.default_web_client_id)
            .requestEmail()
            .build()
        gsc =GoogleSignIn.getClient(requireActivity(),gso)

        //init firebase auth
        auth = FirebaseAuth.getInstance()
        //checkUser()
        getGoogleCredential()
        binding.googleLogIn.setOnClickListener {

                GlobalScope.launch(Dispatchers.IO) {
                    async { googleSignIn() }
                }
        }
        return binding.root
    }


    private fun googleSignIn() {
        gsc.signOut()
        val intent: Intent = gsc.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN) {
            val task= GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                Log.d(TAGG, "try task account")
                val account= task.getResult(ApiException::class.java)
                Log.d("acoount", account.id.toString())
                var exists = false
                for (item in list){
                    val currentId = item.id
                    if (currentId == account.id){
                        exists = true
                        Log.d("Item id: ",currentId)
                        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
                    }

                }
                if (!exists) {
                    firebaseAuthWithGoogleAccount(account)
                }
                Toast.makeText( context, "google sigIn",
                    Toast.LENGTH_SHORT).show()
            } catch (e : ApiException) {
                Toast.makeText( context, "google sigIn error",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun googleLoginRequest( name: String?, googleEmail: String?, googleId: String?): LoginRequest {
        val loginRequest = LoginRequest()
        try {
            loginRequest.setId(googleId!!)
            loginRequest.setName(name!!)
            loginRequest.setEmail(googleEmail!!)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return loginRequest
    }

    fun googleExists(loginRequest: LoginRequest?) {

        val userResponseCall = GoogleApiClient.getUserService().userLogin(loginRequest)

        userResponseCall.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(call: Call<LoginResponse?>, response: retrofit2.Response<LoginResponse?>){
                if (response.isSuccessful) {

                    Log.d("account creation", "success")
                    Toast.makeText( context, "Account created",
                        Toast.LENGTH_SHORT).show()

                    Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registerFragment)
                } else {
                    Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
                    Log.d("account exists", "exist!!!")

                }
            }
            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {

                Log.d("saveUser", "Request Failed" + t.localizedMessage)
            }

        })


    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAGG, "firebase with google account")
        try{
        val credential = GoogleSignIn.getLastSignedInAccount(requireActivity())

        Log.d(TAGG,  "credential : " + credential.toString())
        if (credential !=null){
            googleId = credential.id.toString()
            val googleEmail: String? = credential.email
            Log.d("googleId",googleId)

            //if googlePlayer already exists then go to home
            GlobalScope.launch(Dispatchers.IO) {
                val request: LoginRequest = googleLoginRequest( "null", googleEmail, googleId)
                async {googleExists(request)}
            }
        }
        }catch ( e: ApiException){
            Log.d(TAG , "firebaseAuthGoogleAccount: Login failed due to  ${e.message}")
            e.printStackTrace()
        }
    }

    private fun getGoogleCredential() {

        var client: OkHttpClient = OkHttpClient()
        var myArray:List<String>
        val name =
            "https://bosilou.pythonanywhere.com/getGoogleCredential/"
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



//                for (item in myArray) {
//
//                    val newItem = item + "}"
//                    val jsonResponse = JSONObject(newItem)
//                    val id = jsonResponse.get("id").toString()
//                    Log.d("json id", id)
//                    list.add(EmailIDModel(id))
//                    Log.d("list", list.toString())
//                    Log.d("json", jsonResponse.toString())
//                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d("response", "error")
            }
        })
    }


    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    GlobalScope.launch(Dispatchers.IO) {
                        val request: EmailLoginRequest = loginUpdateRequest(
                            email,password
                        )
                        async { loginUpdateUser(request) }
                    }
                }
            }
    }
    fun loginUpdateRequest(email: String?, pass: String?): EmailLoginRequest {
        val registRequest = EmailLoginRequest()
        try {
            registRequest.setEmail(email!!)
            registRequest.setPass(pass!!)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return registRequest
    }
    fun loginUpdateUser(registRequest: EmailLoginRequest?) {
        val userResponseCall = GoogleApiClient.getUserService().emailLoginCall(registRequest)

        userResponseCall.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(call: Call<LoginResponse?>, response: Response<LoginResponse?>){
                if (response.isSuccessful) {
                    Log.d("login: ", "success")
                    Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    Log.d("login", "error")
                }
            }
            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                Log.d("login", "Request Failed" + t.localizedMessage)
            }

        })

    }


}



