package com.example.themealdb

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

class MainActivity3 : AppCompatActivity() {

    companion object {
        const val NAME_EXTRA2 = "name_extra2"
    }

    private lateinit var foodName: String

    private lateinit var txtInfo: TextView
    private lateinit var imgFood: ImageView

    private lateinit var ytUrlIntent: String
    private lateinit var infoUrlIntent: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        txtInfo = findViewById(R.id.infoText)
        imgFood = findViewById(R.id.imageView3)

        runFun3()

        val actionBar = supportActionBar
        actionBar!!.title = foodName
//        actionBar.setDisplayHomeAsUpEnabled(true)

    }

    private fun runFun3() {

        foodName = intent.getStringExtra(NAME_EXTRA2).toString()

        val queue = Volley.newRequestQueue(this)
        var url3 ="https://www.themealdb.com/api/json/v1/1/search.php?s=$foodName"
        url3 = url3.replace(" ","%20")

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url3, null,
            { response ->

                val mealsArray: JSONArray = response.getJSONArray("meals")
                val mealsObject: JSONObject = mealsArray.getJSONObject(0)

                txtInfo.text = mealsObject.getString("strInstructions")
                Picasso.get().load(mealsObject.getString("strMealThumb")).into(imgFood)

                ytUrlIntent = mealsObject.getString("strYoutube")
                infoUrlIntent = mealsObject.getString("strSource")

            },
            {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_LONG).show()
            })

        queue.add(jsonObjectRequest)

    }

    fun onVideo(view: View) {

        val linK = ytUrlIntent
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.addCategory(Intent.CATEGORY_BROWSABLE)

        intent.data = Uri.parse(linK)
        startActivity(intent)

    }

    fun onInfo(view: View) {
        val intent = Intent(this, MainActivity5::class.java)
        intent.putExtra(MainActivity5.NAME_EXTRA,infoUrlIntent)
        startActivity(intent)
    }

}