package com.fukajima.warframerepo.repository

import androidx.room.Dao

@Dao
interface ItemDAO {

    fun insertAllItems()

    fun dropTable()
}