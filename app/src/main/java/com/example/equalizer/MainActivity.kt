package com.example.equalizer

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_start.setOnClickListener {
            if(equalizer_view.isStarted){
                btn_start.text = "Start"
                equalizer_view.stop()
            }else{
                btn_start.text = "Stop"
                equalizer_view.start()
            }
        }

        btn_red.setOnClickListener { equalizer_view.setColorRes(R.color.red) }
        btn_blue.setOnClickListener { equalizer_view.setColorRes(R.color.blue) }
        btn_white.setOnClickListener { equalizer_view.setColorRes(R.color.white) }

    }
}
