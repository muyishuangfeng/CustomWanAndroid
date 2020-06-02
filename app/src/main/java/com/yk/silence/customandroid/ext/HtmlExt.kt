package com.yk.silence.customandroid.ext

import androidx.core.text.HtmlCompat

fun String?.htmlToSpanned() =
    if (this.isNullOrEmpty()) "" else HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
