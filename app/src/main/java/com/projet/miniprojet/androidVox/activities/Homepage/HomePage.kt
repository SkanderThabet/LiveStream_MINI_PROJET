package com.projet.miniprojet.androidVox.activities.Homepage

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide.with
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.activities.BroadcastStreaming.BroadcastMain
import com.projet.miniprojet.androidVox.activities.BroadcastStreaming.defaultexample.ExampleRtmpActivity
import com.projet.miniprojet.androidVox.activities.Chat.ChatMain
import com.projet.miniprojet.androidVox.activities.LiveStreamChatInteraction.AppConfig
import com.projet.miniprojet.androidVox.activities.LiveStreamChatInteraction.MainActivity
import com.projet.miniprojet.androidVox.activities.PodcastV2.ui.PodcastActivity
import com.projet.miniprojet.androidVox.activities.SignInUp.EXTRA_CREDENTIAL
import com.projet.miniprojet.androidVox.activities.SignInUp.oAuths
import com.projet.miniprojet.androidVox.models.ChatUser
import com.projet.miniprojet.androidVox.other.SharedPref
import com.squareup.picasso.Picasso
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.drawer_header.*
import org.json.JSONException
import org.json.JSONObject



class HomePage : AppCompatActivity() {
    private lateinit var oneTapClient: SignInClient
    private val client = ChatClient.instance()
    private lateinit var user: User
    lateinit var sharedPref: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        sharedPref = SharedPref(this)


        /**
         * Anonymous logging test
         */
//        val app = App(AppConfiguration.Builder(APP_ID).build())
//        val credentials=Credentials.anonymous()
//        app.loginAsync(credentials){
//            if(it.isSuccess){
//                Log.v("User","Logged in anonymously")
//            }
//            else{
//                Log.e("EXAMPLE", "Failed to log in: ${it.error.errorMessage}")
//            }
//        }

        topAppBar.setNavigationOnClickListener {
            drawerLayout.open()
        }
        signoutBtnHome.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setPositiveButton("Yes") { _, _ ->
                sharedPref.clear()
                client.disconnect()
                startActivity(Intent(this, oAuths::class.java))
                finish()

            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.setTitle("Logout?")
            builder.setMessage("Are you sure you want to logout?")
            builder.create().show()
        }
        navigationViewHome.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            if (menuItem.itemId == R.id.messages_menu) {
                startChatAct()
            }
            if (menuItem.itemId == R.id.channels_menu) {
                startStreamingView()
            }
            if (menuItem.itemId == R.id.podcasts_menu) {
                startPodcast()
            }


            menuItem.isChecked = true

            drawerLayout.close()
            true
        }
        StreamBtn.setOnClickListener {
            startStreamingActivity()
        }

        getUserProfile()



    }




    private fun startPodcast() {
        startActivity(Intent(this, PodcastActivity::class.java))
    }


    private fun startStreamingView() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun startStreamingActivity() {
        startActivity(Intent(this, ExampleRtmpActivity::class.java))
        finish()
    }


    private fun startChatAct() {
        startActivity(Intent(this, ChatMain::class.java))
        finish()
    }

    private fun getUserProfile() {
//        val credential = intent.extras?.get(EXTRA_CREDENTIAL) as SignInCredential
        val url = "https://voxappli.herokuapp.com/api/vox/auth"
        val token: String = sharedPref.getValue_string("token")!!
        val jsonObjectRequest: JsonObjectRequest = object :
            JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener<JSONObject?> { response ->
                    try {
                        if (response.getBoolean("success")) {

                            val userObj = response.getJSONObject("user")
                            val fullname =
                                "${userObj.getString("firstname")} ${userObj.getString("lastname")}"
                            name_textView.setText(fullname)
                            id_textView.setText("@"+userObj.getString("username"))
                            Picasso.with(applicationContext)
                                .load(userObj.getString("avatar"))
                                .placeholder(R.drawable.defaultavatar)
                                .error(R.drawable.defaultavatar)
                                .into(avatarView)
                            setupUser(userObj.getString("username"),fullname,userObj.getString("avatar"),token)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error ->
                    error.printStackTrace()
                    Toast.makeText(this@HomePage, "Error $error", Toast.LENGTH_SHORT).show()
//                    name_textView.text=credential.displayName
//                    id_textView.text="@${credential.givenName}_${credential.familyName}".lowercase()
//                    Picasso.with(this)
//                        .load(credential.profilePictureUri)
//                        .error(R.drawable.defaultavatar)
//                        .into(avatarView)

                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = token
                return params
            }
        }
        val socketTimeout = 30000
        val policy: RetryPolicy = DefaultRetryPolicy(
            socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonObjectRequest)
    }
    private fun connectUser(id:String,name:String,image:String,token:String) {

        ChatClient.instance().connectUser(
            user = User(
                id = id,
                extraData = mutableMapOf(
                    "name" to name,
                    "image" to image
                )
            ),
            token = token
        ).enqueue()
    }
    private fun setupUser(id:String,name:String,image:String,token:String) {

            user =
                User(
                    id = id,
                    extraData = mutableMapOf(
                        "name" to name,
                        "image" to image
                    )
                )
            val token = client.devToken(user.id)
            client.connectUser(
                user = user,
                token = token
            ).enqueue { result ->
                if (result.isSuccess) {
                    Log.d("ChannelFragment", "Success Connecting the User")
                } else {
                    Log.d("ChannelFragment", result.error().message.toString())
                }
            }
        }




    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, HomePage::class.java)
        }
    }
}