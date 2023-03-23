package com.example.projmacc.Rank

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.projmacc.R
import com.example.projmacc.Register.EmailRequest
import com.example.projmacc.databinding.FragmentRankBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import kotlin.reflect.typeOf

class RankFragment:Fragment() {
    private var _binding : FragmentRankBinding? = null
    private val binding get() = _binding!!
    private lateinit var rotationFigure : RotationFigure

    private lateinit var  mAuth: FirebaseAuth
    private var rank1 = 0
    private var rank2 = 0
    private var rank3 = 0
    var name1 = "NONE"
    var name2 = "NONE"
    var name3 = "NONE"
    var score1 = ""
    var score2 = ""
    var score3 = ""

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

        _binding = FragmentRankBinding.inflate(inflater, container, false)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        rotationFigure = RotationFigure(requireContext())
        binding.rotationImage.addView(MyView(context , "cube" , rotationFigure))
        binding.cristalRotImage.addView((MyView(context , "crystal" , rotationFigure)))

        binding.back2.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_rankFragment_to_homeFragment)
        }


        connect()
        Thread.sleep(1000)

        binding.rankName1.text = name1
        binding.score1.text = score1
        binding.rankName2.text = name2
        binding.score2.text = score2
        binding.rankName3.text = name3
        binding.score3.text = score3

        return binding.root
    }
    override fun onPause() {
        Log.d("RankFragmen.kt" , "onPause()")
        super.onPause()
    }




    override fun onResume() {
        Log.d("RankFragmen.kt" , "onResume()")
        super.onResume()
    }
    private fun connect() {

            var client: OkHttpClient = OkHttpClient()

            val credential = GoogleSignIn.getLastSignedInAccount(requireActivity())
            var myArray:List<String>

            if (credential != null) {
                val name =
                    "https://bosilou.pythonanywhere.com/getScoreGoogle/"
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
                            val name = jsonResponse.get("name").toString()
                            val score = jsonResponse.get("score").toString()
                            //Log.d("json", jsonResponse.toString())
                            Log.d("json name", name.toString())
                            Log.d("json  score", score.toString())
                            if(binding.rankName1.text == "NONE" && name1.equals("NONE")){ //1

                                name1 = name
                                score1 = score
                                Log.d("name1", name)
                            }else if (score1.toInt() > score.toInt() && binding.rankName2.text == "NONE"
                                && name2 == "NONE"  ){//2
                                name2 = name
                                score2 = score
                                Log.d("name2", name)
                            }else if(score1.toInt() <= score.toInt() && name2.equals("NONE")
                                && binding.rankName2.text == "NONE"){//2
                                name2 = name1
                                score2 = score1
                                name1 = name
                                score1 = score
                                Log.d("name2", name)
                            }else if (score2.toInt() >= score.toInt() && name3.equals("NONE")
                                && binding.rankName3.text == "NONE"){//3
                                name3 = name
                                score3 = score
                                Log.d("name3", name)
                            }else if (score1.toInt() <= score.toInt() && name3.equals("NONE")
                                && binding.rankName3.text == "NONE"){//3
                                name3 = name2
                                score3 = score2
                                name2 = name1
                                score2 = score1
                                name1 = name
                                score1 = score
                                Log.d("name3", name)
                            }else if (score2.toInt() <= score.toInt() && name3.equals("NONE")
                                && binding.rankName3.text == "NONE"){//3
                                name3 = name2
                                score3 = score2
                                name2 = name
                                score2 = score
                                Log.d("name3", name)
                            }//end fill all empty space
                            else if(score1.toInt() >= score.toInt() && score2.toInt() < score.toInt()
                                && binding.rankName2.text != "NONE"){//2
                                name3 = name2
                                score3 = score2
                                name2 = name
                                score2 = score
                            }else if(score1.toInt() <= score.toInt() && binding.rankName2.text != "NONE"){//1
                                name3 = name2
                                score3 = score2
                                name2 = name1
                                score2 = score1
                                name1 = name
                                score1 = score
                            }else if(score3.toInt() <= score.toInt() && binding.rankName3.text != "NONE") {//3
                                name3 = name
                                score3 = score
                            }

                        }

                        binding.rankName1.text = name1
                        binding.score1.text = score1
                        binding.rankName2.text = name2
                        binding.score2.text = score2
                        binding.rankName3.text = name3
                        binding.score3.text = score3

                        if (body is String) {
                            Log.d("body type", "is String")
                        }

                        Log.d("body", body)
                    }

                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        Log.d("response", "error")
                    }
                })
            } else { //email rank
                val name =
                    "https://bosilou.pythonanywhere.com/getScoreEmail/"
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
                            val name = jsonResponse.get("name").toString()
                            val score = jsonResponse.get("score").toString()
                            //Log.d("json", jsonResponse.toString())
                            Log.d("json name", name.toString())
                            Log.d("json  score", score.toString())
                            if(binding.rankName1.text == "NONE" && name1.equals("NONE")){ //1

                                name1 = name
                                score1 = score
                                Log.d("name1", name)
                            }else if (score1.toInt() > score.toInt() && binding.rankName2.text == "NONE"
                                && name2.equals("NONE")  ){//2
                                name2 = name
                                score2 = score
                                Log.d("name2", name)
                            }else if(score1.toInt() <= score.toInt() && name2.equals("NONE")
                                && binding.rankName2.text == "NONE"){//2
                                name2 = name1
                                score2 = score1
                                name1 = name
                                score1 = score
                                Log.d("name2", name)
                            }else if (score2.toInt() >= score.toInt() && name3.equals("NONE")
                                && binding.rankName3.text == "NONE"){//3
                                name3 = name
                                score3 = score
                                Log.d("name3", name)
                            }else if (score1.toInt() <= score.toInt() && name3.equals("NONE")
                                && binding.rankName3.text == "NONE"){//3
                                name3 = name2
                                score3 = score2
                                name2 = name1
                                score2 = score1
                                name1 = name
                                score1 = score
                                Log.d("name3", name)
                            }else if (score2.toInt() <= score.toInt() && name3.equals("NONE")
                                && binding.rankName3.text == "NONE"){//3
                                name3 = name2
                                score3 = score2
                                name2 = name
                                score2 = score
                                Log.d("name3", name)
                            }//end fill all empty space
                            else if(score1.toInt() >= score.toInt() && score2.toInt() < score.toInt()
                                && binding.rankName2.text != "NONE"){//2
                                name3 = name2
                                score3 = score2
                                name2 = name
                                score2 = score
                            }else if(score1.toInt() <= score.toInt() && binding.rankName2.text != "NONE"){//1
                                name3 = name2
                                score3 = score2
                                name2 = name1
                                score2 = score1
                                name1 = name
                                score1 = score
                            }else if(score3.toInt() <= score.toInt() && binding.rankName3.text != "NONE") {//3
                                name3 = name
                                score3 = score
                            }

                        }


                        if (body is String) {
                            Log.d("body type", "is String")
                        }

                        Log.d("body", body)
                    }

                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        Log.d("response", "error")
                    }
                })

            }

    }

}