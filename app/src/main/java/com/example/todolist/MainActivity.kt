package com.example.todolist

import FirstFragment
import SecondFragment
import ThirdFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var todoAdapter : TodoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val firstFragment=FirstFragment()
        val secondFragment=SecondFragment()
        val thirdFragment=ThirdFragment()
        setCurrentFragment(firstFragment)
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(firstFragment)
                R.id.person->setCurrentFragment(secondFragment)
                R.id.settings->setCurrentFragment(thirdFragment)

            }
            true
        }

        val db = TodoDBHelper(this, null)
        todoAdapter = TodoAdapter(mutableListOf(), db, false)
        val RecyclerViewTODOItems = findViewById<RecyclerView>(R.id.RecyclerViewTODOItems)
        RecyclerViewTODOItems.adapter = todoAdapter
        RecyclerViewTODOItems.layoutManager = LinearLayoutManager(this)

        todoAdapter.loadAndParseDataOnInit()

        findViewById<Button>(R.id.ButtonAddTODO).setOnClickListener {

            val todoTitle = findViewById<EditText>(R.id.EditTextTODOTitle).text.toString()
            if(todoTitle.isNotEmpty()) {
                val todo = TodoItem(db.getNextAvailableID(), todoTitle, false)
                todoAdapter.addTODO(todo)
                findViewById<EditText>(R.id.EditTextTODOTitle).text.clear()
                Toast.makeText(this, "$todoTitle added! ", Toast.LENGTH_LONG).show()
            }
        }

        findViewById<Button>(R.id.ButtonDeleteDoneTODOs).setOnClickListener {
            todoAdapter.deleteDoneTODOs()
            Toast.makeText(this, "Finished tasks deleted! ", Toast.LENGTH_LONG).show()
        }
    }

    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
}