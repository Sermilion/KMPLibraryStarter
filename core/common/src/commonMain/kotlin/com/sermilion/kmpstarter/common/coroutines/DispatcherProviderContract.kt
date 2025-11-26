package com.sermilion.kmpstarter.common.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProviderContract {
  val io: CoroutineDispatcher
  val main: CoroutineDispatcher
  val default: CoroutineDispatcher
}
