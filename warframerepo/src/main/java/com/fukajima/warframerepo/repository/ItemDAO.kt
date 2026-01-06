package com.fukajima.warframerepo.repository

import androidx.room.Dao
import androidx.room.DeleteTable
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fukajima.warframerepo.entity.Item

@Dao
interface ItemDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllItems(item:List<Item>)

    @Query("DELETE FROM ITEM")
    fun deleteAll()

    @Query("SELECT * FROM ITEM WHERE id = :id")
    fun getItemById(id:String) : Item
}