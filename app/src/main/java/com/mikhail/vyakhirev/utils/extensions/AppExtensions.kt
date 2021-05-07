package com.mikhail.vyakhirev.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mikhail.vyakhirev.R

fun ImageView.loadImageFromLink(link: String?) {
    if (!link.isNullOrEmpty()) {
        Glide.with(context.applicationContext)
            .load(link)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .into(this)
    }
}

fun Bitmap.getCroppedBitmap(): Bitmap {
    val bitmap = this
    val output = Bitmap.createBitmap(
        bitmap.width,
        bitmap.height, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(output)
    val color = -0xbdbdbe
    val paint = Paint()
    val rect = Rect(0, 0, bitmap.width, bitmap.height)
    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    paint.color = color
    canvas.drawCircle(
        (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
        (bitmap.width / 2).toFloat(), paint
    )
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap, rect, rect, paint)
    return output
}

fun loadLink(view: View, link: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    view.context.startActivity(intent)
}

fun Activity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(msg: String) {
    Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun hasInternet(view: View): Boolean {
    val cm =
        view.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val success = cm.activeNetworkInfo != null && cm.activeNetworkInfo?.isConnected ?: false
    if (!success) {
        view.context.toast(view.context.getString(R.string.no_internet))
    }
    return success
}

fun hasInternet(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val success = cm.activeNetworkInfo != null && cm.activeNetworkInfo?.isConnected ?: false
    if (!success) {
        context.toast(context.getString(R.string.no_internet))
    }
    return success
}


fun checkGPS(context: Context): Boolean {
    val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager


    val gpsEnabled = try {
        lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    } catch (ex: Exception) {
        false
    }

    val networkEnabled = try {
        lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    } catch (ex: Exception) {
        false
    }

    if (!gpsEnabled && !networkEnabled) {
        context.toast(context.getString(R.string.turn_on_geo))
        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    return gpsEnabled && networkEnabled
}

fun myLog(msg: Any?) {
    Log.d("Kanotop", msg.toString())
}
