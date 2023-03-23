package com.example.projmacc.Register

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.projmacc.R
import com.example.projmacc.databinding.FragmentRegisterBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.lang.Runnable

class RegisterFragment : Fragment() {

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!


    private lateinit var  mAuth: FirebaseAuth




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        if(mAuth.currentUser !=null){
            //if the current user is auth
            return
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater , container , false)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        /*binding.PlayBtn.setOnClickListener{
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_gameViewFragment)
        }*/
        binding.errorTexts.visibility = View.GONE

        binding.logins.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_registerFragment_to_loginFragment)
        }


        mAuth = Firebase.auth
        val emailEditText = binding.emails
        val nameEditText = binding.names
        val surnameEditText = binding.surnames
        val passwordEditText = binding.passwords
        val password2EditText = binding.password2


        val credential = GoogleSignIn.getLastSignedInAccount(requireActivity())

        if (credential != null){ //google registration page
            binding.emailsLayout.visibility = View.GONE
            binding.nameLayout.visibility = View.GONE
            binding.surnameLayout.visibility = View.GONE
            binding.passwords.visibility = View.GONE
            binding.password2Layout.visibility = View.GONE
            binding.eyer.visibility = View.GONE
            binding.eyep.visibility = View.GONE
            binding.signins.visibility = View.GONE
            binding.logins.visibility = View.GONE
            binding.chooseUserNameLayout.visibility = View.VISIBLE
            binding.play.visibility = View.VISIBLE
            binding.back.visibility = View.GONE
        }
        binding.signins.setOnClickListener {

            val emailText = emailEditText.text.toString()
            val nameText = nameEditText.text.toString()
            val surnameText = surnameEditText.text.toString()
            val passwordText = passwordEditText.text.toString()
            val password2Text = password2EditText.text.toString()

            if(!emailText.contains("@") || emailText == ""){
                showInvalidEmail()
            } else if(nameText == ""){
                showInvalidName()
            } else if(surnameText == ""){
                showInvalidSurname()
            } else if(passwordText.length < 6){
                showInvalidPassword()
            } else if (passwordText != password2Text){
                showPasswordMismatch()
            } else {
                mAuth.fetchSignInMethodsForEmail(binding.emails.text.toString())
                    .addOnCompleteListener { task ->
                        val newUser: Boolean = task.result!!.signInMethods!!.isEmpty()
                        if (newUser) {
                            binding.emailsLayout.visibility = View.GONE
                            binding.nameLayout.visibility = View.GONE
                            binding.surnameLayout.visibility = View.GONE
                            binding.passwords.visibility = View.GONE
                            binding.password2Layout.visibility = View.GONE
                            binding.eyer.visibility = View.GONE
                            binding.eyep.visibility = View.GONE
                            binding.signins.visibility = View.GONE
                            binding.logins.visibility = View.GONE
                            binding.chooseUserNameLayout.visibility = View.VISIBLE
                            binding.play.visibility = View.VISIBLE
                            binding.back.visibility = View.VISIBLE
                        } else {
                            showSigninFailed()
                        }

                    }
            }
        }


        binding.play.setOnClickListener {
            if(credential != null){ //loged with google
                val googleEmail = credential.email
                val googleId = credential.id
                GlobalScope.launch(Dispatchers.IO) {
                    val request: GoogleUpdateRequest = createGoogleUpdateRequest(binding.chooseUserName.text.toString(),
                        googleEmail , googleId)
                    async {updateGoogleUser(request)}
                }
            }else{
                if (binding.chooseUserName.text.toString() == ""){
                    Toast.makeText(context, "choose a name",
                        Toast.LENGTH_SHORT
                    ).show()
                }else {

                    val emailText = emailEditText.text.toString()
                    //val nameText = nameEditText.text.toString()
                    //val surnameText = surnameEditText.text.toString()
                    val passwordText = passwordEditText.text.toString()
                    //val password2Text = password2EditText.text.toString()
                    Log.d("pass: ", passwordText)
                    mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener { task ->
                            val emailId: String = mAuth.uid.toString()
                            Log.d("emailId: ", emailId)
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(
                                    context, "Authentication success. email account created",
                                    Toast.LENGTH_SHORT
                                ).show()
                                //create account on pythonanywere DB
                                GlobalScope.launch(Dispatchers.IO) {
                                    val request: EmailRequest = createEmailRequest(
                                        emailId, binding.chooseUserName.text.toString(),
                                        emailText, passwordText
                                    )
                                    async { createEmailUser(request) }
                                }
                                //val user = mAuth.currentUser
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.exception)
                                Toast.makeText(context, "Authentication failed. email error",
                                    Toast.LENGTH_SHORT
                                ).show()
                                //updateUI(null)
                            }
                        }
                }
            }
        }



        binding.back.setOnClickListener {
            if ( credential != null){
                var gso: GoogleSignInOptions
                gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    //.requestIdToken(R.string.default_web_client_id)
                    .requestEmail()
                    .build()
                var gsc: GoogleSignInClient
                gsc = GoogleSignIn.getClient(requireActivity(),gso)

                gsc.signOut()
                Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment)
            }
            binding.emailsLayout.visibility = View.VISIBLE
            binding.nameLayout.visibility = View.VISIBLE
            binding.surnameLayout.visibility = View.VISIBLE
            binding.passwords.visibility = View.VISIBLE
            binding.password2Layout.visibility = View.VISIBLE
            binding.eyer.visibility = View.VISIBLE
            binding.eyep.visibility = View.VISIBLE
            binding.signins.visibility = View.VISIBLE
            binding.logins.visibility = View.VISIBLE
            binding.chooseUserNameLayout.visibility = View.GONE
            binding.play.visibility = View.GONE
            binding.back.visibility = View.GONE
        }

        var eyevalp = 0
        binding.eyep.setOnClickListener {
            if(eyevalp == 0){
                binding.passwords.transformationMethod = HideReturnsTransformationMethod.getInstance()
                eyevalp = 1
                context?.let { it1 -> ContextCompat.getColor(it1, R.color.primary) }
                    ?.let { it2 -> binding.eyep.setColorFilter(it2, android.graphics.PorterDuff.Mode.SRC_IN) };
            } else{
                binding.passwords.transformationMethod = PasswordTransformationMethod.getInstance()
                eyevalp = 0
                context?.let { it1 -> ContextCompat.getColor(it1, R.color.black) }
                    ?.let { it2 -> binding.eyep.setColorFilter(it2, android.graphics.PorterDuff.Mode.SRC_IN) };
            }
        }



        var eyevalr = 0
        binding.eyer.setOnClickListener {
            if(eyevalr == 0){
                binding.password2.transformationMethod = HideReturnsTransformationMethod.getInstance()
                eyevalr = 1
                context?.let { it1 -> ContextCompat.getColor(it1, R.color.primary) }
                    ?.let { it2 -> binding.eyer.setColorFilter(it2, android.graphics.PorterDuff.Mode.SRC_IN) };
            } else{
                binding.password2.transformationMethod = PasswordTransformationMethod.getInstance()
                eyevalr = 0
                context?.let { it1 -> ContextCompat.getColor(it1, R.color.black) }
                    ?.let { it2 -> binding.eyer.setColorFilter(it2, android.graphics.PorterDuff.Mode.SRC_IN) };
            }
        }

        return binding.root
    }
    fun createEmailRequest(emailId: String? , name: String?, email: String?,  pass: String?): EmailRequest {
        val registRequest = EmailRequest()
        try {
            registRequest.setId(emailId!!)
            registRequest.setName(name!!)
            registRequest.setEmail(email!!)
            registRequest.setPass(pass!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return registRequest
    }

    fun createEmailUser(registRequest: EmailRequest) {
        val userResponseCall = ApiClient.getUserService().createEmail(registRequest)

        userResponseCall.enqueue(object : Callback<RegisterResponse?> {
            override fun onResponse(call: Call<RegisterResponse?>, response: Response<RegisterResponse?>){
                if (response.isSuccessful) {

                    Log.d("create Email Account: ", "success")
                    Toast.makeText( context, "Email Account created",
                        Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_homeFragment)

                } else {

                    Log.d("Email Account creation: ", "error")
                    Toast.makeText( context, "Email Account not created",
                        Toast.LENGTH_SHORT).show()

                }
            }
            override fun onFailure(call: Call<RegisterResponse?>, t: Throwable) {

                Log.d("update", "Request Failed" + t.localizedMessage)
            }

        })


    }

    fun createGoogleUpdateRequest(name: String?, googleEmail: String?, googleId: String?): GoogleUpdateRequest {
        val registRequest = GoogleUpdateRequest()
        try {
            registRequest.setId(googleId!!)
            registRequest.setName(name!!)
            registRequest.setEmail(googleEmail!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return registRequest
    }
    fun updateGoogleUser(registRequest: GoogleUpdateRequest?) {
        val userResponseCall = ApiClient.getUserService().updateGoogleUser(registRequest)

        userResponseCall.enqueue(object : Callback<RegisterResponse?> {
            override fun onResponse(call: Call<RegisterResponse?>, response: Response<RegisterResponse?>){
                if (response.isSuccessful) {

                    Log.d("update: ", "success")
                    Toast.makeText( context, "Account updated",
                        Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_homeFragment)

                } else {

                    Log.d("account update", "error")
                    Toast.makeText( context, "Account not updated",
                        Toast.LENGTH_SHORT).show()

                }
            }
            override fun onFailure(call: Call<RegisterResponse?>, t: Throwable) {

                Log.d("update", "Request Failed" + t.localizedMessage)
            }

        })


    }


    private fun showSigninFailed() {
        displayMsg("Email already exist/ regist error")
    }

    private fun showInvalidEmail(){
        displayMsg("Invalid email")
    }

    private fun showInvalidName(){
        displayMsg("Name can't be empty")
    }

    private fun showInvalidSurname(){
        displayMsg("Surname can't be empty")
    }

    private fun showInvalidPassword(){
        displayMsg("Password must have min 6 char")
    }

    private fun showPasswordMismatch(){
        displayMsg("Passwords don't coincide")
    }
    private fun AccountRegistered(){
        displayMsg("Registered")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayMsg(str: String){
        //val appContext = context?.applicationContext ?: return
        //Toast.makeText(appContext, str, Toast.LENGTH_LONG).show()
        val errtxt = binding.errorTexts
        errtxt.text = str
        errtxt.visibility = View.VISIBLE
        Handler().postDelayed(Runnable { // hide your button here
            errtxt.visibility = View.GONE
        }, 2000)
    }
}



