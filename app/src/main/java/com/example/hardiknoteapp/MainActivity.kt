package com.example.hardiknoteapp


import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.hardiknoteapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    var backPressedTime: Long = 0
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        navController.navigate(R.id.noteList)

        onBackPressedDispatcher.addCallback(this ) {
            val currentFragment = navController.currentDestination?.id
            if (currentFragment==R.id.addEditNoteFragment) {
                navController.navigate(R.id.noteList)
            }else{
                if (backPressedTime + 3000 > System.currentTimeMillis()) {
                    finish()
                } else {
                    Toast.makeText(this@MainActivity, getString(R.string.press_back_again_to_leave_the_app), Toast.LENGTH_LONG).show()
                }
                backPressedTime = System.currentTimeMillis()
            }


        }

    }



}