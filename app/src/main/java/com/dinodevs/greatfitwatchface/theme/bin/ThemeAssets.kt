package com.dinodevs.greatfitwatchface.theme.bin

import android.app.Service
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.gson.Gson
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.utils.SimpleFile
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.io.StringReader


class ThemeAssets(val context: Context, val path: String) {

    private var imageCache = mutableMapOf<Int, Bitmap>()

    private var _theme: Theme
    private var localPath: String? = null

    private var isLocal = false

    companion object {
        val TAG = "VergeIT Tools"
    }

    private fun preCache() {
        Log.d(TAG, "precache start")
        Log.d(TAG, "precache stop")
    }

    init {
        localPath = context.filesDir.absolutePath + "/theme/config.json";
        if (File(localPath).exists()) {
            isLocal = true
            _theme = Gson().fromJson<Theme>(FileReader(localPath), Theme::class.java)
            preCache()
        } else {
            val content = StringReader(String(SimpleFile.readFileFromAssets(context, path)))
            _theme = Gson().fromJson<Theme>(content, Theme::class.java)
        }
    }

    fun getBitmap(service: Service, imageIdx: Int): Bitmap {
        val bmp: Bitmap?
        if (!imageCache.containsKey(imageIdx)) {
            bmp = if (isLocal) {
                BitmapFactory.decodeFile(getPath(imageIdx))
            } else {
                Util.decodeImage(service.resources, getPath(imageIdx))
            }
            Log.d(TAG, "cache image $imageIdx")
            imageCache[imageIdx] = bmp
        } else {
            bmp = imageCache[imageIdx]
        }
        return bmp!!;
    }

    fun getPath(imageIdx: Int): String {
        return if (isLocal)
            String.format("%s/%04d.png", File(localPath).parent, imageIdx)
        else String.format("%s/%04d.png", File(this.path).parent, imageIdx)
    }

    val theme: Theme
        get() = _theme

//    private fun path(imageIndex: Int): String {
//        if (isLocal) {
//
//        }
//        val base =  File(path).parent
//        return String.format("%s/%04d.png", base, imageIndex)
//    }
}