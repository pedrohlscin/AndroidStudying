package com.android.example.aboutMe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.android.example.aboutMe.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val myName : MyName = MyName("Pedro")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.myName = myName
        binding.botao.setOnClickListener{
            addNickName(it)
        }
    }

    private fun addNickName(view: View) {
        binding.apply {
            myName?.nickname = nicknameEdit.text.toString()
            invalidateAll()
//            nicknameEdit.visibility = View.GONE
//            botao.visibility = View.GONE
            nicknameText.visibility = View.VISIBLE
            var i : String? = "null"
            println(i?.contains('a'))
        }
    }
}