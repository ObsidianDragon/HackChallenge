package com.example.hackchallenge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val NUM_FRAGMENTS = 3

class MainActivity : AppCompatActivity(), AskFragment.Callback {
    private lateinit var bottomNavBar: BottomNavigationView
    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavBar = findViewById(R.id.navigationBar)

        fragmentManager.beginTransaction().add(R.id.fragmentView, AnswerFragment()).commit()

        bottomNavBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.answer_item -> {
                    fragmentManager.beginTransaction().replace(R.id.fragmentView, AnswerFragment()).commit()
                }
                R.id.ask_item -> {
                    fragmentManager.beginTransaction().replace(R.id.fragmentView, AskFragment()).commit()
                }
                R.id.history_item -> {
                    fragmentManager.beginTransaction().replace(R.id.fragmentView, HistoryFragment()).commit()
                }
            }
            true
        }
    }

    //method to change to history fragment when a question is asked
    override fun onClick() {
        fragmentManager.beginTransaction().replace(R.id.fragmentView, HistoryFragment()).commit()
        bottomNavBar.selectedItemId=R.id.history_item
    }
}