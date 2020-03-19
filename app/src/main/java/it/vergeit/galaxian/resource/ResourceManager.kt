package it.vergeit.galaxian.resource

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Typeface
import com.huami.watch.watchface.util.Util
import com.ingenic.iwds.slpt.view.utils.SimpleFile
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * Resource manager for caching purposes
 */
object ResourceManager {
    private val TYPE_FACES: MutableMap<Font, Typeface?> = EnumMap<Font, Typeface>(Font::class.java)
    @JvmStatic
    fun getTypeFace(resources: Resources, font: Font): Typeface? {
        var typeface = TYPE_FACES[font]
        if (typeface == null) {
            typeface = Typeface.createFromAsset(resources.assets, font.path)
            TYPE_FACES[font] = typeface
        }
        return typeface
    }

    // This function can scale images for verge
    fun getVergeImageFromAssets(verge: Boolean?, var0: Context, var1: String?): ByteArray {
        val file: ByteArray
        if (!verge!!) {
            file = SimpleFile.readFileFromAssets(var0, var1)
        } else {
            var image = Util.decodeImage(var0.resources, "background.png")
            image = Bitmap.createScaledBitmap(image, 360, 360, false)
            file = getBytesFromBitmap(image)
        }
        return file
    }

    // Convert from bitmap to byte array
    fun getBytesFromBitmap(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        return stream.toByteArray()
    }

    // Get font names
    @JvmStatic
    fun getNames(e: Class<out Enum<*>?>): Array<String> {
        return Arrays.toString(e.enumConstants).replace("^.|.$".toRegex(), "").split(", ").toTypedArray()
    }

    enum class Font(// More fonts can go here
            val path: String) {
        Thin("fonts/Thin.otf"), Regular("fonts/Regular.otf"), Bold("fonts/Bold.ttf");

    }
}