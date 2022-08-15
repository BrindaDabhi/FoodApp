package com.example.themealdb

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity(), ItemClicked {

    private lateinit var recyclerview: RecyclerView

    private var arrayListdetails: ArrayList<Model1> =  ArrayList()

    private lateinit var customAdapter: CustomAdapter


    lateinit var toggle : ActionBarDrawerToggle


    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawer_layout)
        val navView : NavigationView = findViewById(R.id.navView)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            when(it.itemId){
                R.id.share -> {

                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Hey, Checkout this recipe App "
                    )
                    val chooser = Intent.createChooser(intent, "Share this app using...")
                    startActivity(chooser)
                }


                R.id.rate_us -> Toast.makeText(applicationContext,"Clicked Rate Us",Toast.LENGTH_SHORT).show()
                R.id.help -> Toast.makeText(applicationContext,"Clicked Help",Toast.LENGTH_SHORT).show()
            }

            true

        }


        recyclerview = findViewById(R.id.recyclerView)
        recyclerview.layoutManager = GridLayoutManager(this,2)

        runFun()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val search = menu.findItem(R.id.search_bar)
        val searchView = search.actionView as androidx.appcompat.widget.SearchView

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                customAdapter.getFilter().filter(newText)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    private fun runFun() {

        val queue = Volley.newRequestQueue(this)
        val url1 ="https://www.themealdb.com/api/json/v1/1/categories.php"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url1, null,
            { response ->

                val categoryArray: JSONArray = response.getJSONArray("categories")

                val size: Int = categoryArray.length()

                for (i in 0 until size) {
                    val categoryDetail: JSONObject = categoryArray.getJSONObject(i)
                    val model1 = Model1()
                    model1.name = categoryDetail.getString("strCategory")
                    model1.img = categoryDetail.getString("strCategoryThumb")

                    arrayListdetails.add(model1)

                }
                runOnUiThread {
                    customAdapter = CustomAdapter(arrayListdetails,this)
                    recyclerview.adapter = customAdapter
                }

            },
            {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_LONG).show()
            })

        queue.add(jsonObjectRequest)

    }

    override fun onItemClicked(item: Model1) {
//        Toast.makeText(this,"Clicked item is : ${item.name}",Toast.LENGTH_LONG).show()
        val intent = Intent(this, MainActivity2::class.java)
        intent.putExtra(MainActivity2.NAME_EXTRA,item.name)
        startActivity(intent)
    }

}