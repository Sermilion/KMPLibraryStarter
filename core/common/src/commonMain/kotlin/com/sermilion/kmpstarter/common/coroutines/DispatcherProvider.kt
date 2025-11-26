package com.sermilion.kmpstarter.common.coroutines

import kotlinx.coroutines.CoroutineDispatcher

expect class DispatcherProvider() : DispatcherProviderContract {
  override val io: CoroutineDispatcher
  override val main: CoroutineDispatcher
  override val default: CoroutineDispatcher
}
