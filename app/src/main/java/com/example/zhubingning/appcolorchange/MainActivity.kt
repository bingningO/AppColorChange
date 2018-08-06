package com.example.zhubingning.appcolorchange

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.zhubingning.appcolorchange.ColorFinder.CallbackInterface
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {

    companion object {
        private const val RESULT_CAMERA = 1001
        private const val TAG = "MainActivity"
    }

    @OnClick(R.id.button_change) fun onClickButton() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, RESULT_CAMERA)
    }

    @OnClick(R.id.button_reset) fun onClickResetButton() {
        main_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.background_material_light))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        // get camera image
        if (requestCode == RESULT_CAMERA) {
            if (data.extras != null) {
                Log.d(TAG, data.extras.toString())
                val bitmap = data.extras.get("data") as Bitmap
                ColorFinder(object : CallbackInterface {
                    override fun onCompleted(color: String) {
                        toast( "Take Color : " + color)
                        main_layout.setBackgroundColor(Color.parseColor(color))
                    }
                }).findDominantColor(bitmap)
            }
        }
    }
}
