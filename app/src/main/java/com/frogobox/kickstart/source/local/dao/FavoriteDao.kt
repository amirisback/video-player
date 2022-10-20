package com.frogobox.kickstart.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.frogobox.kickstart.model.Favorite
import com.frogobox.kickstart.util.Constant.RoomDatabase.TABLE_NAME_FAVORITE
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * Created by Faisal Amir
 * FrogoBox Inc License
 * =========================================
 * BaseMusicPlayer
 * Copyright (C) 04/09/2019.
 * All rights reserved
 * -----------------------------------------
 * Name     : Muhammad Faisal Amir
 * E-mail   : faisalamircs@gmail.com
 * Github   : github.com/amirisback
 * LinkedIn : linkedin.com/in/faisalamircs
 * -----------------------------------------
 * FrogoBox Software Industries
 * com.frogobox.basemusicplayer.source.dao
 *
 */
@Dao
interface FavoriteDao {

    @Query("SELECT * FROM $TABLE_NAME_FAVORITE")
    fun getAllData(): Single<List<Favorite>>

    @Insert
    fun insertData(data: Favorite) : Completable

    @Query("DELETE FROM $TABLE_NAME_FAVORITE WHERE table_id = :tableId")
    fun deleteDataFromTableId(tableId: Int) : Completable

    @Query("DELETE FROM $TABLE_NAME_FAVORITE")
    fun nukeData() : Completable

}