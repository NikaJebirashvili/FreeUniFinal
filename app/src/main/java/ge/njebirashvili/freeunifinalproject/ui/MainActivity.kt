package ge.njebirashvili.freeunifinalproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ge.njebirashvili.freeunifinalproject.R
import ge.njebirashvili.freeunifinalproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationMenu.background = null
    }
}