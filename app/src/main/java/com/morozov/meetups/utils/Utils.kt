package com.morozov.meetups.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.text.withStyle
import java.util.regex.Pattern

object StringUtils {
    /**
     *
     */
    @OptIn(ExperimentalTextApi::class)
    fun multiLinkAnnotatedString(
        text: String,
        clickableTexts: List<Pair<String, String>>,
        style: SpanStyle
    ): Pair<AnnotatedString, String> {
        val clickableIndexes = clickableTexts.map {
            val startIndex = text.indexOf(it.first)
            startIndex to startIndex + it.first.length
        }
        var endClickableIndex = 0
        val TAG = "URI"
        return buildAnnotatedString {
            clickableIndexes.forEach { (start, end) ->
                val index = clickableIndexes.indexOf((start to end))
                if (start == -1) {
                    append(text.substring(endClickableIndex))
                } else if (start > 0) {
                    append(text.substring(endClickableIndex, start))
                }
                append(
                    createAnnotatedText(
                        text = clickableTexts[index].first,
                        tag = TAG,
                        annotation = clickableTexts[index].second,
                        style
                    )
                )
                endClickableIndex = this.length
            }
            if (endClickableIndex < text.length - 1) {
                append(text.substring(endClickableIndex))
            }
        } to TAG
    }

    @OptIn(ExperimentalTextApi::class)
    private fun createAnnotatedText(
        text: String,
        tag: String,
        annotation: String,
        style: SpanStyle
    ) =
        buildAnnotatedString {
            withStyle(
                style = style
            ) {
                withAnnotation(tag, annotation) {
                    append(text)
                }
            }
        }

    fun String.isValidEmail(): Boolean {
        val emailAddressPattern = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return emailAddressPattern.matcher(this).matches()
    }
}