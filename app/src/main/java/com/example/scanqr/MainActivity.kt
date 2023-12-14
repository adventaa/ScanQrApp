package com.example.scanqr

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonClick: ImageButton = findViewById(R.id.image_next)
        buttonClick.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.image_next -> {
                    val moveIntent = Intent(this, MainActivity2::class.java)
                    startActivities(arrayOf(moveIntent))
                }
            }
        }
    }
}
