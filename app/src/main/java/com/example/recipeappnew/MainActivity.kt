package com.example.recipeappnew

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.recipeappnew.viewmodel.NavDrawerState
import kotlinx.android.synthetic.main.menu_layout.*

class MainActivity : AppCompatActivity() {

    private lateinit var container: View
    private lateinit var navDrawerState: NavDrawerState
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        container = findViewById(R.id.motionLayout1)
        val drawerLayout = findViewById<DrawerLayout>(R.id.motionLayout)

        homeTv.setOnClickListener { goToHomePage() }

        drawerLayout.setScrimColor(resources.getColor(android.R.color.transparent))

        navDrawerState = ViewModelProvider(this@MainActivity).get(NavDrawerState::class.java)
        navDrawerState.isOpen.observe(this, Observer {
            if (it) {
                drawerLayout.openDrawer(GravityCompat.START)
                changeState(container)
            } else {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        })

    }

    private fun goToHomePage() {
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.homeFragment)
        navDrawerState.updateNavDrawer(false)
    }

    private fun changeState(v: View?) {
        val motionLayout = container as? MotionLayout ?: return
        motionLayout.transitionToEnd()

    }
}