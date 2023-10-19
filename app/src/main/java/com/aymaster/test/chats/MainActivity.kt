package com.aymaster.test.chats

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aymaster.test.feauture.chat.presentation.fragment.ChatFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appComponent.inject(this)

        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragmentContainer,
                ChatFragment(),
                ChatFragment::class.java.simpleName
            )
            .commit()
    }
}