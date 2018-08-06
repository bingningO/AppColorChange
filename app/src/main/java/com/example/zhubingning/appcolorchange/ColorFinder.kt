package com.example.zhubingning.appcolorchange


import android.graphics.Bitmap
import android.os.AsyncTask

import java.util.HashMap

/**
 * Created by zhubingning on 2018/08/05.
 */
class ColorFinder(private val callback: CallbackInterface?) {

    fun findDominantColor(bitmap: Bitmap) {
        GetDominantColor().execute(bitmap)
    }

    private fun getDominantColorFromBitmap(bitmap: Bitmap): Int {
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        val pixelList = getMostDominantPixelList(pixels)
        return getDominantPixel(pixelList)
    }

    private fun getMostDominantPixelList(pixels: IntArray): Map<Int, PixelObject> {
        val pixelList = HashMap<Int, PixelObject>()

        for (pixel in pixels) {
            if (pixelList.containsKey(pixel)) {
                pixelList[pixel]!!.pixelCount++
            } else {
                pixelList.put(pixel, PixelObject(pixel, 1))
            }
        }

        return pixelList
    }

    private fun getDominantPixel(pixelList: Map<Int, PixelObject>): Int {
        var dominantColor = 0
        var largestCount = 0
        for ((_, pixelObject) in pixelList) {

            if (pixelObject.pixelCount > largestCount) {
                largestCount = pixelObject.pixelCount
                dominantColor = pixelObject.pixel
            }
        }

        return dominantColor
    }

    private inner class GetDominantColor : AsyncTask<Bitmap, Int, Int>() {

        override fun doInBackground(vararg params: Bitmap): Int? {
            return getDominantColorFromBitmap(params[0])
        }

        override fun onPostExecute(dominantColor: Int?) {
            val hexColor = colorToHex(dominantColor!!)
            callback?.onCompleted(hexColor)
        }

        private fun colorToHex(color: Int): String {
            return String.format("#%06X", 0xFFFFFF and color)
        }
    }

    interface CallbackInterface {
        fun onCompleted(dominantColor: String)
    }

    companion object {
        private val TAG = ColorFinder::class.java.simpleName
    }

    inner class PixelObject(var pixel: Int, var pixelCount: Int)
}