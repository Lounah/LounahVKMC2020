package com.lounah.vkmc.feature.feature_market.data.core

import android.provider.BaseColumns

object DatabaseContract {

    abstract class FavProductsDatabase : BaseColumns {
        companion object {
            const val TABLE_NAME = "favourite_products"
            const val COLUMN_NAME_ID = "id"

            const val CREATE_DATABASE = (
                    "CREATE TABLE IF NOT EXISTS $TABLE_NAME ( $COLUMN_NAME_ID STRING PRIMARY KEY );")

            const val DELETE_DATABASE = "DROP TABLE IF EXIST $TABLE_NAME"
        }
    }
}