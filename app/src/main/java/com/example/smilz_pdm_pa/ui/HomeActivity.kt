package com.example.smilz_pdm_pa.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.smilz_pdm_pa.R
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home) // Conecta o layout à Activity

        // Configura a Toolbar personalizada
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)  // Define a toolbar como a ActionBar


        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        //toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            when(it.itemId){

                //R.id.nav_home -> Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT).show()
                //R.id.nav_beneficiarios -> Toast.makeText(applicationContext, "Clicked Beneficiarios", Toast.LENGTH_SHORT).show()
                //R.id.nav_calendars -> Toast.makeText(applicationContext, "Clicked Escalas", Toast.LENGTH_SHORT).show()
                //R.id.nav_stats -> Toast.makeText(applicationContext, "Clicked Estatisticas", Toast.LENGTH_SHORT).show()
                //R.id.nav_logout -> Toast.makeText(applicationContext, "Clicked Logout", Toast.LENGTH_SHORT).show()

                R.id.nav_home -> {
                    // Redireciona para a MainActivity (Beneficiarios)
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.nav_beneficiarios -> {
                    // Redireciona para a MainActivity (Beneficiarios)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.nav_calendars -> {
                    val intent = Intent(this, EscalaActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                R.id.nav_stats -> Toast.makeText(applicationContext, "Clicked Estatisticas", Toast.LENGTH_SHORT).show()

                R.id.nav_logout -> {
                    LoginActivity.logout(this)
                }

            }
            drawerLayout.closeDrawers()

            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }


        return super.onOptionsItemSelected(item)
    }
}


