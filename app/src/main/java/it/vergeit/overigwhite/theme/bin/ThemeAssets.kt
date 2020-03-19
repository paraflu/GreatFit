package it.vergeit.overigwhite.theme.bin

import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import com.google.gson.Gson
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.utils.SimpleFile
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream


class ThemeAssets(val context: Context, var themeName: String = "white") {
    private var isArchive: Boolean = false
    private var imageCache = mutableMapOf<String, Bitmap>()
    private var _theme: Theme? = null
    private var localPath: String = ""
    private var isLocal = false
    private var _archive: ZipInputStream? = null

    companion object {
        const val TAG = "VergeIT Tools"
    }

    init {
        Log.d(TAG, "init")
        try {
            val f = FileReader("${Environment.getExternalStorageDirectory()}/vergeit/theme.txt")
            themeName = f.readLines().first()
            f.close()
        } catch (e:Exception) {
            Log.d(TAG, "theme name $themeName ${e.message}")
        }

        localPath = "${Environment.getExternalStorageDirectory()}/vergeit/$themeName.zip";
        if (File(localPath).exists()) {
            Log.d(TAG, "found zip file $localPath")
            isArchive = true
            val zip: ZipFile = ZipFile(File(localPath))
            val entries = zip.entries()
            while (entries.hasMoreElements()) {
                // Get ZipEntry which is a file or a directory
                val entry: ZipEntry = entries.nextElement() as ZipEntry
                val inputStream: InputStream = zip.getInputStream(entry)
                val isr = InputStreamReader(inputStream)
                Log.d(TAG, "unpack zip file ${entry.name}")
                if (entry.name == "config.json") {
                    _theme = Gson().fromJson(isr, Theme::class.java)
                    Log.d(TAG, "theme loaded")
                } else if (entry.name.endsWith(".png")) {
                    val buffer = ByteArray(1024)
                    val buffList = ByteArrayOutputStream()
                    while (inputStream.read(buffer, 0, buffer.size) != -1) {
                        buffList.write(buffer)
                    }
                    Log.d(TAG, "${entry.name} siz ${buffList.size()}")
                    val bmp = BitmapFactory.decodeByteArray(buffList.toByteArray(), 0, buffList.size())
                    imageCache[File(entry.name).nameWithoutExtension] = bmp
                    Log.d(TAG, "file loaded")
                }
            }
            Log.d(TAG, "load complete")
        } else {
            localPath = "${Environment.getExternalStorageDirectory()}/vergeit/$themeName/config.json";
            Log.d(TAG, "localPath $localPath")
            if (File(localPath).exists()) {
                isLocal = true
                Log.d(TAG, "isLocal $isLocal")
                _theme = Gson().fromJson<Theme>(FileReader(localPath), Theme::class.java)
                Log.d(TAG, "Theme loaded")
            } else {
                val content = StringReader(String(SimpleFile.readFileFromAssets(context, "$themeName/config.json")))
                Log.d(TAG, "content $content")
                _theme = Gson().fromJson<Theme>(content, Theme::class.java)
                Log.d(TAG, "NO Local, config $themeName/config.json")
            }
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
                    Log.d(TAG, "now load image from assets $path")
                    Util.decodeImage(service.resources, path)
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
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
        else String.format("%s/%04d%s.png", themeName, imageIdx, suffix)
    }

    val theme: Theme
        get() = _theme!!

//    private fun path(imageIndex: Int): String {
//        if (isLocal) {
//
//        }
//        val base =  File(path).parent
//        return String.format("%s/%04d.png", base, imageIndex)
//    }
}