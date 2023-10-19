package com.aymaster.test.feauture.chat.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.renderscript.Allocation
import android.renderscript.Element.U8_4
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View
import kotlin.math.roundToInt

object BlurBuilder {

    private const val BITMAP_SCALE = 0.6f
    private const val BLUR_RADIUS = 15f

    fun blur(context: Context?, view: View): Bitmap {
        val width = (view.width * BITMAP_SCALE).roundToInt()
        val height = (view.height * BITMAP_SCALE).roundToInt()
        val inputBitmap = Bitmap.createScaledBitmap(getBitmapFromView(view), width, height, false)
        val outputBitmap = Bitmap.createBitmap(inputBitmap)
        val rs = RenderScript.create(context)
        val intrinsicBlur = ScriptIntrinsicBlur.create(rs, U8_4(rs))
        val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
        intrinsicBlur.setRadius(BLUR_RADIUS)
        intrinsicBlur.setInput(tmpIn)
        intrinsicBlur.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)
        return outputBitmap
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bitmap)
        view.layout(view.left, view.top, view.right, view.bottom)
        view.draw(c)
        return bitmap
    }
}