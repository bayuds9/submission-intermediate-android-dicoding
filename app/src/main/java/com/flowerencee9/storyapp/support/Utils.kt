package com.flowerencee9.storyapp.support

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Environment
import android.os.Parcelable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import com.flowerencee9.storyapp.R
import com.google.android.material.snackbar.Snackbar
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

fun String.isEmailValid(): Boolean {
    val emailPattern = Pattern.compile(
        "[a-zA-Z0-9+._%\\-]{1,256}" +
                "@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    return emailPattern.matcher(this).matches()
}

fun View.toHide() {
    this.visibility = View.GONE
}

fun View.toShow() {
    this.visibility = View.VISIBLE
}

fun View.isViewShown() = this.visibility == View.VISIBLE

fun View.showView(states: Boolean) {
    when(states){
        true -> this.toShow()
        else -> this.toHide()
    }
}

fun View.toInvisible() {
    this.visibility = View.INVISIBLE
}

interface SpannableListener {
    fun onClick()
}

fun Context.spanText(
    fullString: String,
    keyword: String,
    listener: SpannableListener? = null,
    spanColor: Int = R.color.red_velvet
): Spannable {
    val startPosition = fullString.indexOf(keyword)
    val spannable = SpannableStringBuilder(fullString)
    spannable.setSpan(
        ForegroundColorSpan(ContextCompat.getColor(this, spanColor)),
        startPosition, startPosition.plus(keyword.length),
        Spannable.SPAN_INCLUSIVE_INCLUSIVE
    )
    if (listener != null) {
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(p0: View) {
                    listener.onClick()
                }

                override fun updateDrawState(ds: TextPaint) {}
            }, startPosition, startPosition.plus(keyword.length),
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
    }
    return spannable
}

fun View.snackbar(message: String) {
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_LONG
    ).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}

private const val FILENAME_FORMAT = "dd-MMM-yyyy"
val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()
    return myFile
}

fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 1000000)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun View.animateVisibility(setVisible: Boolean, animDuration: Long) {
    if (setVisible) expand(this, animDuration) else collapse(this, animDuration)
}

private fun expand(view: View, animDuration: Long) {
    view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    val initialHeight = 0
    val targetHeight = view.measuredHeightAndState * 4
    view.layoutParams.height = 0
    view.visibility = View.VISIBLE

    animateView(view, initialHeight, targetHeight, animDuration)
}

private fun collapse(view: View, animDuration: Long) {
    val initialHeight = view.measuredHeight
    val targetHeight = 0
    view.visibility = View.INVISIBLE

    animateView(view, initialHeight, targetHeight, animDuration)
}

private fun animateView(v: View, initialHeight: Int, targetHeight: Int, animDuration: Long) {
    val valueAnimator = ValueAnimator.ofInt(initialHeight, targetHeight)
    valueAnimator.addUpdateListener { animation ->
        v.layoutParams.height = animation.animatedValue as Int
        v.requestLayout()
    }
    valueAnimator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationEnd(animation: Animator) {
            v.layoutParams.height = targetHeight
        }

        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })
    valueAnimator.duration = animDuration
    valueAnimator.interpolator = DecelerateInterpolator()
    valueAnimator.start()
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}
