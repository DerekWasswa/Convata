package com.rosen.convata

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class ConvataTestRunner: AndroidJUnitRunner() {

    override fun newApplication(
        classLoader: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(classLoader, ConvataTestApplication::class.java.name, context)
    }


}