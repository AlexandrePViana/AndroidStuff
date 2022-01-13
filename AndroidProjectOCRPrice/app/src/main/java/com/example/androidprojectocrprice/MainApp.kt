package com.example.androidprojectocrprice

import android.R.attr
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import android.accounts.Account
import android.app.Activity
import android.util.Base64

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*
import android.R.attr.bitmap
import android.app.PendingIntent.getActivity
import android.net.Uri
import android.os.AsyncTask
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.bumptech.glide.load.HttpException
import com.google.android.datatransport.cct.internal.LogResponse.fromJson
import com.google.common.collect.ObjectArrays
import com.google.gson.JsonParser
import com.google.gson.Gson
import com.google.gson.JsonSerializer
import com.google.mlkit.vision.text.Text

import org.json.JSONObject
import org.json.JSONArray
import java.lang.Exception
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.common.reflect.TypeToken
import okhttp3.*
import java.io.*
import java.lang.reflect.Type
import android.os.Looper
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.serialization.json.Json
import androidx.coordinatorlayout.widget.CoordinatorLayout





class AsyncTaskExample(private var activity: MainApp?, base : String,tip : String,key : String) : AsyncTask<String, String, String>() {
    var baz = base
    var tipz = tip
    var keyz = key
    override fun onPreExecute() {
        super.onPreExecute()
    }


    override fun doInBackground(vararg p0: String?): String? {
        if (tipz == "IMAGE") {
            activity?.Show()
            activity?.TextBox?.setText("Processando...")
            var url =
                "https://content-vision.googleapis.com/v1/images:annotate?alt=json&key=AIzaSyAa8yy0GdcGPHdtD083HiGGx_S0vMPScDM"
            val client = OkHttpClient()
            val ola = baz
            lateinit var response: Response
            lateinit var OCR: String
            lateinit var LOGO: String
            lateinit var LABEL: String
            lateinit var keywords: String
            lateinit var obj: JSONObject
            lateinit var unA: JSONArray
            lateinit var title: String
            lateinit var c: JSONArray
            lateinit var temp: JSONObject
            val mediaType = "application/json; charset=utf-8".toMediaType()
            var cont =
                "{\"requests\": [{\"features\": [{\"type\": \"LOGO_DETECTION\"}],\"image\": {\"content\":\"$ola\"}}]}"
            var requestBody = cont.toString().toRequestBody(mediaType)
            var request = Request.Builder()
                .addHeader("x-referer", "https://explorer.apis.google.com")
                .url(url)
                .post(requestBody)
                .build()
            try {
                response = client.newCall(request).execute()
                obj = JSONObject(response.body?.string())
                unA = obj.getJSONArray("responses")
                c = unA.getJSONObject(0).getJSONArray("logoAnnotations")
                temp = c.getJSONObject(0)
                title = temp.getString("description")
                LOGO = title

            } catch (e: Exception) {
                LOGO = "NULL"

            }

            cont =
                "{\"requests\": [{\"features\": [{\"type\": \"OBJECT_LOCALIZATION\"}],\"image\": {\"content\":\"$ola\"}}]}"
            requestBody = cont.toString().toRequestBody(mediaType)
            request = Request.Builder()
                .addHeader("x-referer", "https://explorer.apis.google.com")
                .url(url)
                .post(requestBody)
                .build()
            response = client.newCall(request).execute()
            try {
                obj = JSONObject(response.body?.string())
                unA = obj.getJSONArray("responses")
                c = unA.getJSONObject(0).getJSONArray("localizedObjectAnnotations")
                temp = c.getJSONObject(0)
                title = temp.getString("name")
                LABEL = title

            } catch (e: Exception) {
                LABEL = "NULL"
            }
            cont =
                "{\"requests\": [{\"features\": [{\"type\": \"TEXT_DETECTION\"}],\"image\": {\"content\":\"$ola\"}}]}"
            requestBody = cont.toString().toRequestBody(mediaType)
            request = Request.Builder()
                .addHeader("x-referer", "https://explorer.apis.google.com")
                .url(url)
                .post(requestBody)
                .build()
            response = client.newCall(request).execute()
            try {
                obj = JSONObject(response.body?.string())
                unA = obj.getJSONArray("responses")
                c = unA.getJSONObject(0).getJSONArray("textAnnotations")
                temp = c.getJSONObject(0)
                title = temp.getString("description")
                OCR = title
            } catch (e: Exception) {
                OCR = "NULL"
            }
            if (LOGO == "NULL" || LOGO == "") {
                if(OCR != "NULL" || OCR != "")
                {
                    activity?.TextBox?.setText(OCR)
                    keywords = OCR.replace(" ", "%20")
                }
                else
                {
                    activity?.TextBox?.setText("Não foi encontrado nada na foto!")
                }
            } else if (LABEL == "NULL" || LABEL == "") {
                if(LOGO != "NULL" || LOGO != ""){
                    activity?.TextBox?.setText("$LOGO")
                    keywords = LOGO.replace(" ", "%20")
                }
            }else {
                activity?.TextBox?.setText("$LOGO $LABEL")
                keywords = LOGO.replace(" ", "%20") + "%20" + LABEL.replace(" ", "%20")
            }
            url = "https://amazon-price1.p.rapidapi.com/search?keywords=$keywords&marketplace=ES"
            request = Request.Builder()
                .addHeader("x-rapidapi-host", "amazon-price1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "be7e768182msh87b3865d647e4dfp102bf6jsn5be5d01a323a")
                .url(url)
                .build()
            var call: Call = client.newCall(request)
            call.enqueue(object : Callback {
                @Throws(IOException::class)
                override fun onFailure(call: Call, e: IOException) {
                    activity?.TextBox?.setText("ERRO!")
                    activity?.DoAdapter()
                    activity?.Hide()
                }

                override fun onResponse(call: Call, response: Response) {
                    try
                    {
                        activity?.newArrList?.clear()
                        var gson = Gson()
                        val array = JSONArray(response.body?.string().toString())
                        for (i in 0 until (array.length())) {
                            val `object` = array.getJSONObject(i)
                            activity?.newArrList?.add(
                                Results(
                                    `object`.getString("ASIN"),
                                    `object`.getString("title"),
                                    `object`.getString("price"),
                                    `object`.getString("imageUrl"),
                                    `object`.getString("detailPageURL"),
                                )
                            )
                            activity?.imageId =
                                arrayOf(Uri.parse(activity?.newArrList?.get(i)?.imageUrl.toString()))
                            activity?.MainName = arrayOf(activity?.newArrList?.get(i)?.title.toString())
                            activity?.PriceName =
                                arrayOf(activity?.newArrList?.get(i)?.price.toString())
                        }
                        activity?.DoAdapter()
                        activity?.Hide()
                    }
                    catch(e : Exception){
                        activity?.TextBox?.setText("ERRO! Não encontrado!")
                        activity?.DoAdapter()
                        activity?.Hide()
                    }

                }
            })
            return "ok"

        } else {
            activity?.Show()
            var temp = keyz.replace(" ","%20")
            var keywords = temp
            val client = OkHttpClient()
            var url = "https://amazon-price1.p.rapidapi.com/search?keywords=$keywords&marketplace=ES"
            var request = Request.Builder()
                .addHeader("x-rapidapi-host", "amazon-price1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "be7e768182msh87b3865d647e4dfp102bf6jsn5be5d01a323a")
                .url(url)
                .build()
            var call: Call = client.newCall(request)
            call.enqueue(object : Callback {
                @Throws(IOException::class)
                override fun onFailure(call: Call, e: IOException) {
                    activity?.TextBox?.setText("ERRO!")
                    activity?.DoAdapter()
                    activity?.Hide()
                }

                override fun onResponse(call: Call, response: Response) {
                    try{
                        activity?.newArrList?.clear()
                        var gson = Gson()
                        val array = JSONArray(response.body?.string().toString())
                        for (i in 0 until (array.length())) {
                            val `object` = array.getJSONObject(i)
                            activity?.newArrList?.add(
                                Results(
                                    `object`.getString("ASIN"),
                                    `object`.getString("title"),
                                    `object`.getString("price"),
                                    `object`.getString("imageUrl"),
                                    `object`.getString("detailPageURL"),
                                )
                            )
                            activity?.imageId = arrayOf(Uri.parse(activity?.newArrList?.get(i)?.imageUrl.toString()))
                            activity?.MainName = arrayOf(activity?.newArrList?.get(i)?.title.toString())
                            activity?.PriceName = arrayOf(activity?.newArrList?.get(i)?.price.toString())
                        }
                        activity?.DoAdapter()
                        activity?.Hide()
                    }
                    catch(e :Exception){
                        activity?.TextBox?.setText("ERRO! Não encontrado!")
                        activity?.DoAdapter()
                        activity?.Hide()
                    }
                }
            })
            return "ok"
        }


    }
}


class MainApp : AppCompatActivity()  {

        lateinit var newRecViewer : RecyclerView
        lateinit var Rez : Results
        lateinit var newArrList :  MutableList<Results>
        lateinit var imageId : Array<Uri>
        lateinit var MainName : Array<String>
        private lateinit var myAdapter: MyAdapter
        lateinit var PriceName : Array<String>
        val REQUEST_PERM_WRITE_STORAGE = 102
        private val CAPTURE_PHOTO = 104
        lateinit var searchBTN : Button
        lateinit var floatAddBtn : FloatingActionButton
        lateinit var floatCamBtn : FloatingActionButton
        lateinit var floatGalBtn : FloatingActionButton
        internal var imagePath: String? = ""
        private val mImageDetails: TextView? = null
        private val mMainImage: ImageView? = null
        private val pickImage = 100
        lateinit var TextBox : EditText
        lateinit var backgroundCover : TextView
        lateinit var progressBar : ProgressBar
        private var imageUri: Uri? = null
        private lateinit var auth: FirebaseAuth
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        var mActivity: Activity? = null
        lateinit var mAccount: Account
        var mRequestCode = 0
        lateinit var mScope: String
        private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim)}
        private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim)}
        private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim)}
        private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim)}
        var clicked = false

    override fun onCreate(savedInstanceState: Bundle?)
        {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            searchBTN = findViewById(R.id.searchBTN) as Button
            floatAddBtn = findViewById(R.id.addBTN) as FloatingActionButton
            floatCamBtn = findViewById(R.id.camBTN) as FloatingActionButton
            floatGalBtn = findViewById(R.id.galBTN) as FloatingActionButton
            newArrList = arrayListOf<Results>()
            myAdapter = MyAdapter(newArrList)
            TextBox = findViewById(R.id.editTextTextPersonName) as EditText
            TextBox.setText("")
            TextBox.setHint("Introduza o termo a pesquisar...")
            newRecViewer = findViewById(R.id.recView) as RecyclerView
            newRecViewer.layoutManager = LinearLayoutManager(this)
            newRecViewer.setHasFixedSize(true)
            newRecViewer.addItemDecoration(DividerItemDecoration(this, OrientationHelper.VERTICAL))
            newRecViewer.adapter = myAdapter
            backgroundCover = findViewById(R.id.backgroundCover) as TextView
            progressBar = findViewById(R.id.progressBar) as ProgressBar
            backgroundCover.visibility = View.INVISIBLE
            progressBar.visibility = View.INVISIBLE
            getSupportActionBar()?.hide();
            auth = Firebase.auth
            val currentUserName = auth.currentUser?.displayName.toString()
            var logOut = findViewById(R.id.logOut) as TextView
            var mainTextView = findViewById(R.id.mainTextView) as TextView
            mainTextView.text = currentUserName
            searchBTN.setOnClickListener {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                onSearchBtn(TextBox.text.toString())
            }
            floatAddBtn.setOnClickListener {
                onAddButtonClicked()
            }
            floatCamBtn.setOnClickListener{
                onAddButtonClicked()
                takePhotoByCamera()
            }
            floatGalBtn.setOnClickListener {
                onAddButtonClicked()
                choosePhotoByGallery()
            }
            logOut.setOnClickListener {
                logout()
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        private fun onAddButtonClicked()
        {
            setVisibility(clicked)
            setAnimation(clicked)
            setClicable(clicked)
            clicked = !clicked
        }

    private fun setClicable(clicked : Boolean) {
        this@MainApp.runOnUiThread(java.lang.Runnable {
            if(clicked){
                floatCamBtn.isClickable = false
                floatGalBtn.isClickable = false
            }else{
                floatCamBtn.isClickable = true
                floatGalBtn.isClickable = true
            }
        })

    }

    private fun setVisibility(clicked : Boolean){
        this@MainApp.runOnUiThread(java.lang.Runnable {
            if(!clicked)
            {
                floatGalBtn.show()
                floatCamBtn.show()
            }else{
                floatGalBtn.hide()
                floatCamBtn.hide()
            }
        })


        }
        private fun setAnimation(clicked : Boolean){
            this@MainApp.runOnUiThread(java.lang.Runnable {
                if(!clicked){
                    floatGalBtn.startAnimation(fromBottom)
                    floatCamBtn.startAnimation(fromBottom)
                    floatAddBtn.startAnimation(rotateOpen)
                }else{
                    floatGalBtn.startAnimation(toBottom)
                    floatCamBtn.startAnimation(toBottom)
                    floatAddBtn.startAnimation(rotateClose)
                }
            })

        }
        @Override
        fun DoToast(content : String)
        {
            this@MainApp.runOnUiThread(java.lang.Runnable {
            Toast.makeText(baseContext,content,Toast.LENGTH_LONG).show()
            })
        }
        @Override
        fun DoAdapter()
        {
            this@MainApp.runOnUiThread(java.lang.Runnable {
                myAdapter.notifyDataSetChanged()
            })
        }
        @Override
        fun Hide(){
            this@MainApp.runOnUiThread(java.lang.Runnable {
                backgroundCover.visibility = View.INVISIBLE
                progressBar.visibility = View.INVISIBLE
            })

        }
        @Override
        fun Show(){
            this@MainApp.runOnUiThread(java.lang.Runnable {
                backgroundCover.visibility = View.VISIBLE
                progressBar.visibility = View.VISIBLE
            })

        }
        private fun logout() {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("245675093028-o3jsivavs6l8mr6sa563grm06fqm1gek.apps.googleusercontent.com")
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            googleSignInClient.signOut()
        }
    fun takePhotoByCamera() {

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAPTURE_PHOTO)
    }
    fun choosePhotoByGallery()
    {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }

    val REQUEST_IMAGE_CAPTURE = 1

    fun onSearchBtn(searchTXT : String){
        val a = AsyncTaskExample(this,"","TXT",searchTXT).execute()

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var imageView = findViewById(R.id.imageView) as ImageView
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            pickImage -> {
                if (resultCode == RESULT_OK && requestCode == pickImage) {
                    imageUri = data?.data
                    imageView.setImageURI(imageUri)
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                    val imageBytes = baos.toByteArray()
                    val base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
                    val a = AsyncTaskExample(this,base64String,"IMAGE","").execute()

                    }
                }
            CAPTURE_PHOTO ->{
                val imageBitmap = data?.extras?.get("data") as Bitmap
                imageView.setImageBitmap(imageBitmap)
                val baos = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                val imageBytes = baos.toByteArray()
                val base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
                val a = AsyncTaskExample(this,base64String,"IMAGE","").execute()
        }}




    }


}
