package com.sermilion.kmpstarter.core.data.db

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

internal const val USER_DATABASE_FILE_NAME = "user.db"

@Database(
  entities = [UserDataModel::class],
  version = 1,
  exportSchema = true,
)
@ConstructedBy(UserDatabaseConstructor::class)
abstract class UserDatabase : RoomDatabase() {
  abstract fun userDao(): UserDao
}

@Suppress("KotlinNoActualForExpect")
expect object UserDatabaseConstructor : RoomDatabaseConstructor<UserDatabase> {
  override fun initialize(): UserDatabase
}

fun createUserDatabase(builder: RoomDatabase.Builder<UserDatabase>): UserDatabase = builder
  .setDriver(BundledSQLiteDriver())
  .build()
