package com.fukajima.warframerepo.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fukajima.warframerepo.entity.Item

@Database(entities = [Item::class], version = 3, exportSchema = false)
abstract class ItemDataBase : RoomDatabase() {

    abstract fun itemDao(): ItemDAO


    companion object {

        private lateinit var instance: ItemDataBase

        fun getDataBase(context: Context): ItemDataBase {

            if (!::instance.isInitialized) {
                //synchronized(ItemDataBase::class) {
                    instance = Room.databaseBuilder(context, ItemDataBase::class.java, "warframedb")
                        .fallbackToDestructiveMigration()
                        //.allowMainThreadQueries()
                        //.addMigrations(MIGRATION_1_2)
                        .build()
                //}

            }

            return instance

        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Exemplo: nenhuma alteração
            }

        }

    }
}