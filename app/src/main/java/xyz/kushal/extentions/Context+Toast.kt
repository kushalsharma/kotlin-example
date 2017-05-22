package xyz.kushal.extentions

import android.content.Context
import android.widget.Toast

/**
 * @author kushal.sharma
 */

fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()