package ge.njebirashvili.freeunifinalproject.utils

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity


fun View.setVisible(boolean: Boolean) {
    visibility = if(boolean) View.VISIBLE else View.GONE
}

fun AppCompatActivity.goOnActivity(activity: Class<*>) {
    this.startActivity(Intent(this,activity))
}