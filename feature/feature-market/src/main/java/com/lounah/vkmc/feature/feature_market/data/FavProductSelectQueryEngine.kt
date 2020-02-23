package com.lounah.vkmc.feature.feature_market.data

import android.database.Cursor
import com.lounah.vkmc.feature.feature_market.data.sql.SelectQueryEngine

class FavProductSelectQueryEngine : SelectQueryEngine<FavProductEntity>() {
    override fun buildEntityFromCursor(cursor: Cursor): FavProductEntity {
        return FavProductEntity(cursor.getString(0))
    }
}