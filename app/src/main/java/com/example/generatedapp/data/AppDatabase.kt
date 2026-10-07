package com.example.generatedapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [MenuItemEntity::class, OrderEntity::class, CustomerEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuDao(): MenuDao
    abstract fun orderDao(): OrderDao
    abstract fun customerDao(): CustomerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sanayi_lokanta_db"
                )
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)!
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database)
                    }
                }
            }

            private suspend fun populateInitialData(db: AppDatabase) {
                val menuDao = db.menuDao()
                if (menuDao.getCount() == 0) {
                    menuDao.insertItem(MenuItemEntity(name = "Tam Döner", price = 120.0, category = "Döner"))
                    menuDao.insertItem(MenuItemEntity(name = "Yarım Döner", price = 70.0, category = "Döner"))
                    menuDao.insertItem(MenuItemEntity(name = "Zurna Döner", price = 150.0, category = "Döner"))
                    menuDao.insertItem(MenuItemEntity(name = "Pilavüstü Döner", price = 160.0, category = "Döner"))
                    menuDao.insertItem(MenuItemEntity(name = "Mercimek Çorbası", price = 40.0, category = "Çorba"))
                    menuDao.insertItem(MenuItemEntity(name = "Kuru Fasulye", price = 60.0, category = "Sulu Yemek"))
                    menuDao.insertItem(MenuItemEntity(name = "Pilav", price = 40.0, category = "Yan Yemek"))
                    menuDao.insertItem(MenuItemEntity(name = "Ayran", price = 15.0, category = "İçecek"))
                }

                val customerDao = db.customerDao()
                if (customerDao.getCount() == 0) {
                    customerDao.insertCustomer(CustomerEntity(name = "Hasan Usta", nickname = "Tornacı Hasan", balance = 350.0))
                    customerDao.insertCustomer(CustomerEntity(name = "Mehmet Kaya", nickname = "Oto Mehmet", balance = 120.0))
                    customerDao.insertCustomer(CustomerEntity(name = "Ali Demir", nickname = "Kaporta Ali", balance = 0.0))
                }
            }
        }
    }
}
