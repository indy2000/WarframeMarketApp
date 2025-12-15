package com.fukajima.warframerepo.repository

import androidx.room.Dao
import androidx.room.DeleteTable
import androidx.room.Insert
import androidx.room.Query
import com.fukajima.warframerepo.entity.Item

@Dao
interface ItemDAO {

    @Insert
    fun insertAllItems(item:List<Item>)

    @Query("DELETE FROM ITEM")
    fun dropTable()

    @Query("SELECT * FROM ITEM WHERE id = :id")
    fun getItemById(id:String) : Item
}