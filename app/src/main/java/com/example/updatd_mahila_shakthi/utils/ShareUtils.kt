package com.example.updatd_mahila_shakthi.utils

import android.content.Context
import android.content.Intent

object ShareUtils {
    fun shareReport(context: Context, reportText: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, reportText)
            putExtra(Intent.EXTRA_SUBJECT, "Mahila-Shakti Unnati - Group Report")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Report via")
        context.startActivity(shareIntent)
    }
}
