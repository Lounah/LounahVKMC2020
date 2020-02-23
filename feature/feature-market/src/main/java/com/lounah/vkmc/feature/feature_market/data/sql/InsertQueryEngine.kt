package com.lounah.vkmc.feature.feature_market.data.sql

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

class InsertQueryEngine : SQLQueryEngine<Long> {
    override fun execute(
        query: String,
        db: SQLiteDatabase,
        tableName: String,
        contentValues: ContentValues,
        onConflictStrategy: Int
    ): Long? {
        db.insertWithOnConflict(
            tableName, null,
            contentValues,
            onConflictStrategy
        )
        return 0L
    }

    override fun executeRawQuery(
        query: String,
        db: SQLiteDatabase,
        tableName: String,
        onConflictStrategy: Int
    ): List<Long>? = null
}
