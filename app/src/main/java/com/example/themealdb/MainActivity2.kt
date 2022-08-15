package com.example.themealdb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class MainActivity2 : AppCompatActivity(), ItemClicked2 {

    companion object {
        const val NAME_EXTRA = "name_extra"
    }

    private lateinit var city: String

    private lateinit var recyclerview2: RecyclerView

    private var arrayListdetails2: ArrayList<Model2> =  ArrayList()

    private lateinit var customAdapter2: CustomAdapter2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        recyclerview2 = findViewById(R.id.recyclerView2)
        recyclerview2.layoutManager = GridLayoutManager(this, 2)

        runFun2()

        val actionBar = supportActionBar
        actionBar!!.title = city
        actionBar.setDisplayHomeAsUpEnabled(true)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu2, menu)

        val search2 = menu.findItem(R.id.search_bar2)
        val searchView2 = search2.actionView as androidx.appcompat.widget.SearchView

        searchView2.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                customAdapter2.getFilter2().filter(newText)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun runFun2() {

        city = intent.getStringExtra(NAME_EXTRA).toString()

        val queue = Volley.newRequestQueue(this)
        val url2 ="https://www.themealdb.com/api/json/v1/1/filter.php?c=$city"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url2, null,
            { response ->

                val mealsArray: JSONArray = response.getJSONArray("meals")

                val size: Int = mealsArray.length()

                for (i in 0 until size) {
                    val categoryDetail: JSONObject = mealsArray.getJSONObject(i)
                    val model2 = Model2()
                    model2.name2 = categoryDetail.getString("strMeal")
                    model2.img2 = categoryDetail.getString("strMealThumb")

                    arrayListdetails2.add(model2)

                }
                runOnUiThread {
                    customAdapter2 = CustomAdapter2(arrayListdetails2,this)
                    recyclerview2.adapter = customAdapter2
                }

            },
            {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_LONG).show()
            })

        queue.add(jsonObjectRequest)

    }

    override fun onItemClicked2(item: Model2) {
//        Toast.makeText(this,"Clicked item is : ${item.name2}",Toast.LENGTH_LONG).show()
        val intent = Intent(this, MainActivity3::class.java)
        intent.putExtra(MainActivity3.NAME_EXTRA2,item.name2)
        startActivity(intent)
    }

}