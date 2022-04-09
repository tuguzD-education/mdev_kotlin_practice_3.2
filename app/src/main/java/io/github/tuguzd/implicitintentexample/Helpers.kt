package io.github.tuguzd.implicitintentexample

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun showSnackbar(view: View, text: CharSequence): Unit =
    Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
