package com.dinodevs.greatfitwatchface.theme.bin

import android.content.Context
import com.google.gson.Gson
import com.ingenic.iwds.slpt.view.utils.SimpleFile
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.StringReader

class ThemeAssets(context: Context, path: String) {

    private val base = File(path).parent

    val theme: Theme
        get() = _theme

    private val _theme = Gson().fromJson<Theme>(StringReader(String(SimpleFile.readFileFromAssets(context, path))), Theme::class.java)

    fun path(imageIndex: Int): String = String.format("%s/%04d.png", base, imageIndex)
}