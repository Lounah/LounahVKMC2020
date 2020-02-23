@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package com.lounah.vkmc.feature.feature_market.data

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
import com.lounah.vkmc.feature.feature_market.data.core.DatabaseContract
import com.lounah.vkmc.feature.feature_market.data.sql.InsertQueryEngine
import com.lounah.vkmc.feature.feature_market.data.sql.SelectQueryEngine
import io.reactivex.Completable
import io.reactivex.Single

inline class FavProductEntity(val id: String)

interface FavProductsDao {
    fun add(productId: String): Completable
    fun contains(productId: String): Single<Boolean>
    fun remove(productId: String): Completable
    fun get(): Single<List<FavProductEntity>>
}

class FavProductsDaoImpl(
    private val sqLiteDatabase: SQLiteDatabase,
    private val selectQueryEngine: SelectQueryEngine<FavProductEntity>,
    private val insertQueryEngine: InsertQueryEngine,
    private val table: String = DatabaseContract.FavProductsDatabase.TABLE_NAME
) : FavProductsDao {

    override fun add(productId: String): Completable {
        val cv = ContentValues().apply {
            put(DatabaseContract.FavProductsDatabase.COLUMN_NAME_ID, productId)
        }

        return Completable.fromAction {
            insertQueryEngine.execute(
                "",
                sqLiteDatabase,
                table,
                cv,
                CONFLICT_REPLACE
            )
        }
    }

    override fun remove(productId: String): Completable {
        return Completable.fromAction {
            sqLiteDatabase.execSQL("DELETE FROM $table WHERE id=$productId")
        }
    }

    override fun get(): Single<List<FavProductEntity>> {
        return Single.just(getAllInternal())
    }

    override fun contains(productId: String): Single<Boolean> {
        return Single.just(getAllInternal().any { it.id == productId })
    }

    private fun getAllInternal(): List<FavProductEntity> {
        return selectQueryEngine.executeRawQuery("SELECT * FROM $table", sqLiteDatabase, table, 0)
    }
}
