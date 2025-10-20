package com.example.prueba_2_levelup.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.prueba_2_levelup.data.dao.CartDao
import com.example.prueba_2_levelup.data.dao.ProductDao
import com.example.prueba_2_levelup.data.dao.UserDao
import com.example.prueba_2_levelup.data.entities.CartItemEntity
import com.example.prueba_2_levelup.data.entities.ProductEntity
import com.example.prueba_2_levelup.data.entities.UserEntity

@Database(entities = [UserEntity::class, ProductEntity::class, CartItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "level_up_database" // Nombre de la base de datos
                )
                    // .fallbackToDestructiveMigration() // Opcional: Borra la BD si la versión cambia (útil en desarrollo)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}