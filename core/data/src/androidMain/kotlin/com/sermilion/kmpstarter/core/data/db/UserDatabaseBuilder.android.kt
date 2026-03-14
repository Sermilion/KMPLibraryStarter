package com.sermilion.kmpstarter.core.data.db

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase

fun createUserDatabaseBuilder(
  context: Context,
  path: String = context.applicationContext.getDatabasePath(USER_DATABASE_FILE_NAME).absolutePath,
): RoomDatabase.Builder<UserDatabase> {
  val appContext = context.applicationContext

  return Room.databaseBuilder<UserDatabase>(
    context = appContext,
    name = path,
    factory = UserDatabaseConstructor::initialize,
  )
}
