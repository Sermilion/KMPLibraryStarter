package com.sermilion.kmpstarter.core.data.db

import androidx.room3.Room
import androidx.room3.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun createUserDatabaseBuilder(path: String = defaultUserDatabasePath()): RoomDatabase.Builder<UserDatabase> =
  Room.databaseBuilder<UserDatabase>(
    name = path,
    factory = UserDatabaseConstructor::initialize,
  )

@OptIn(ExperimentalForeignApi::class)
private fun defaultUserDatabasePath(): String {
  val documentDirectory =
    NSFileManager.defaultManager.URLForDirectory(
      directory = NSDocumentDirectory,
      inDomain = NSUserDomainMask,
      appropriateForURL = null,
      create = false,
      error = null,
    )

  return requireNotNull(documentDirectory?.path) + "/$USER_DATABASE_FILE_NAME"
}
