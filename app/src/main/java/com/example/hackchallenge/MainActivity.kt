package com.example.hackchallenge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val NUM_FRAGMENTS = 3

class MainActivity : AppCompatActivity(), AskFragment.Callback {
//    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavBar: BottomNavigationView
    private lateinit var fragmentView: FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        viewPager = findViewById(R.id.viewPager)
//        viewPager.adapter = ViewPagerAdapter(this)

        bottomNavBar = findViewById(R.id.navigationBar)
        val fragmentManager = supportFragmentManager

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

//    private inner class ViewPagerAdapter(activity : MainActivity) : FragmentStateAdapter(activity) {
//        override fun getItemCount(): Int {
//            return NUM_FRAGMENTS
//        }

//        override fun createFragment(position: Int): Fragment {
//            return when(position) {
//                0 -> AnswerFragment()
//                1 -> AskFragment()
//                else -> HistoryFragment()
//            }
//        }
//
//    }

    override fun onClick(src: Int) {
        TODO("Not yet implemented")
    }
}