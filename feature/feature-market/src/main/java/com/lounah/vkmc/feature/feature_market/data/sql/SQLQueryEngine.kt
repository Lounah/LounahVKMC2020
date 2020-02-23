package com.lounah.vkmc.feature.feature_market.data.sql

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

interface SQLQueryEngine<T> {
    fun execute(
        query: String,
        db: SQLiteDatabase,
        tableName: String,
        contentValues: ContentValues,
        onConflictStrategy: Int
    ): T?

    fun executeRawQuery(
        query: String,
        db: SQLiteDatabase,
        tableName: String,
        onConflictStrategy: Int
    ): List<T>?
}
