package it.vergeit.watchface.theme.bin

import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.gson.Gson
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.utils.SimpleFile
import java.io.*
import java.lang.Exception


class ThemeAssets(val context: Context, private val themeName: String) {

    private var imageCache = mutableMapOf<String, Bitmap>()

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

    fun getBitmap(service: Service, imageIdx: Int, slpt: Boolean = false, slptBetter: Boolean = false): Bitmap {
        val bmp: Bitmap
        val key = "$imageIdx" + if (slpt) if (slptBetter) "_26w" else "_8c" else ""

        if (!imageCache.containsKey(key)) {
            val path = getPath(imageIdx, slpt, slptBetter)

            bmp = try {
                if (isLocal) {
                    BitmapFactory.decodeFile(path) ?: throw IOException()
                } else {
                    Util.decodeImage(service.resources, path)
                }
            } catch (e: Exception) {
                when (e) {
                    is FileNotFoundException,
                    is IOException -> {
                        if (slptBetter) getBitmap(service, imageIdx, slpt, false)
                        else getBitmap(service, imageIdx, slpt = false, slptBetter = false)
                    }
                    else -> throw e
                }
                // recupero prendendo l'immagine non ottimizzata
                getBitmap(service, imageIdx)
            }

            imageCache[key] = bmp
        } else {
            bmp = imageCache[key]!!
        }
        return bmp
    }

    private fun getPath(imageIdx: Int, slpt: Boolean = false, slptBetter: Boolean = false): String {
        val suffix = if (slpt) if (slptBetter) "_26w" else "_8c" else ""
        return if (isLocal)
            String.format("%s/%04d%s.png", File(localPath).parent, imageIdx, suffix)
        else String.format("%s/%04d%s.png", File(this.themeName).parent, imageIdx, suffix)
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