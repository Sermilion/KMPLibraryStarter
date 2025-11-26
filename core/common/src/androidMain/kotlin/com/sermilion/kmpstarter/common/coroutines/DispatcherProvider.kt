package com.sermilion.kmpstarter.common.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual class DispatcherProvider : DispatcherProviderContract {
  actual override val io: CoroutineDispatcher = Dispatchers.IO
  actual override val main: CoroutineDispatcher = Dispatchers.Main
  actual override val default: CoroutineDispatcher = Dispatchers.Default
}
