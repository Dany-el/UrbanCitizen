package com.dyablonskyi.transpod.ui

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp(Application::class)
class TranspodApplication @Inject constructor(): Application()