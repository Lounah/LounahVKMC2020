package com.lounah.vkmc.feature.feature_market.data.sql

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


abstract class SelectQueryEngine<T> : SQLQueryEngine<T> {

    override fun execute(
        query: String,
        db: SQLiteDatabase,
        tableName: String,
        contentValues: ContentValues,
        onConflictStrategy: Int
    ): T? = null

    override fun executeRawQuery(
        query: String,
        db: SQLiteDatabase,
        tableName: String,
        onConflictStrategy: Int
    ): List<T> {

        val result = mutableListOf<T>()
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val selectedItem = buildEntityFromCursor(cursor)
                result.add(selectedItem)
            } while (cursor.moveToNext())
        }

        cursor.close()

        return result
    }

    abstract fun buildEntityFromCursor(cursor: Cursor): T
}
