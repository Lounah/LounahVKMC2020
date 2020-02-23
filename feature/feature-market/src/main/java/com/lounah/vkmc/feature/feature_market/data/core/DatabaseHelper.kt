package com.lounah.vkmc.feature.feature_market.data.core

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


private const val DATABASE_NAME = "app_db"
private const val DATABASE_VERSION = 1

class DatabaseHelper(
    context: Context
) : SQLiteOpenHelper(context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DatabaseContract.FavProductsDatabase.CREATE_DATABASE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DatabaseContract.FavProductsDatabase.DELETE_DATABASE)
    }
}
