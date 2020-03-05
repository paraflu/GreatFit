package it.vergeit.watchface.theme.bin

import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.gson.Gson
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.utils.SimpleFile
import java.io.File
import java.io.FileReader
import java.io.StringReader


class ThemeAssets(val context: Context, private val themeName: String) {

    private var imageCache = mutableMapOf<Int, Bitmap>()

    private var _theme: Theme
    private var localPath: String = ""

    private var isLocal = false

    companion object {
        const val TAG = "VergeIT Tools"
    }

    private fun preCache() {
        Log.d(TAG, "precache start")
        Log.d(TAG, "precache stop")
    }

    init {
        ///data/data/it.vergeit.watchface/files/theme/Nuclear_pure_analog_cmp_vergelite/config.json
        ///data/data/it.vergeit.watchface/files/theme/Nuclear_pure_analog_cmp_vergelite/config.json
//        localPath = context.filesDir.absolutePath + "/theme/$themeName/config.json";
        localPath = "/sdcard/vergeit/md131/config.json";
        Log.d(TAG, "theme $localPath")
        if (File(localPath).exists()) {
            isLocal = true
            _theme = Gson().fromJson<Theme>(FileReader(localPath), Theme::class.java)
            preCache()
        } else {
            Log.d(TAG, "theme not found")
            val content = StringReader(String(SimpleFile.readFileFromAssets(context, "gtr/config.json")))
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
        else String.format("%s/%04d.png", File(this.themeName).parent, imageIdx)
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