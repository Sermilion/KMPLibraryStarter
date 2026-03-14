package com.sermilion.kmpstarter.core.data.db

import androidx.room3.Room
import androidx.room3.RoomDatabase
import java.io.File

fun createUserDatabaseBuilder(path: String = defaultUserDatabasePath()): RoomDatabase.Builder<UserDatabase> =
  Room.databaseBuilder<UserDatabase>(
    name = path,
    factory = UserDatabaseConstructor::initialize,
  )

private fun defaultUserDatabasePath(): String =
  File(System.getProperty("java.io.tmpdir"), USER_DATABASE_FILE_NAME).absolutePath
