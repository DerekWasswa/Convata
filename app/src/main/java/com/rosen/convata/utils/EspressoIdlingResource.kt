package com.rosen.convata.utils

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {
    private const val TASK_COUNTER = "COUNTER"

    @JvmField val countingIdlingResource = CountingIdlingResource(TASK_COUNTER)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}