package com.example.generatedapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {
    @Query("SELECT * FROM menu_items")
    fun getAllMenuItems(): Flow<List<MenuItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: MenuItemEntity)

    @Query("SELECT COUNT(*) FROM menu_items")
    suspend fun getCount(): Int
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Insert
    suspend fun insertOrder(order: OrderEntity)

    @Query("SELECT SUM(totalPrice) FROM orders")
    fun getTotalRevenue(): Flow<Double?>
}

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity)

    @Update
    suspend fun updateCustomer(customer: CustomerEntity)

    @Query("SELECT COUNT(*) FROM customers")
    suspend fun getCount(): Int
}
