package com.example.generatedapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.generatedapp.data.AppDatabase
import com.example.generatedapp.data.CustomerEntity
import com.example.generatedapp.data.MenuItemEntity
import com.example.generatedapp.data.OrderEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LokantaViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val menuDao = db.menuDao()
    private val orderDao = db.orderDao()
    private val customerDao = db.customerDao()

    val menuItems: StateFlow<List<MenuItemEntity>> = menuDao.getAllMenuItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<OrderEntity>> = orderDao.getAllOrders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val customers: StateFlow<List<CustomerEntity>> = customerDao.getAllCustomers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalRevenue: StateFlow<Double> = orderDao.getTotalRevenue()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private val _currentOrderItems = MutableStateFlow<List<MenuItemEntity>>(emptyList())
    val currentOrderItems: StateFlow<List<MenuItemEntity>> = _currentOrderItems.asStateFlow()

    fun addToOrder(item: MenuItemEntity) {
        _currentOrderItems.value = _currentOrderItems.value + item
    }

    fun removeFromOrder(item: MenuItemEntity) {
        val list = _currentOrderItems.value.toMutableList()
        list.remove(item)
        _currentOrderItems.value = list
    }

    fun clearCurrentOrder() {
        _currentOrderItems.value = emptyList()
    }

    fun completeOrder(paymentType: String, onComplete: () -> Unit) {
        val items = _currentOrderItems.value
        if (items.isEmpty()) return
        val totalPrice = items.sumOf { it.price }
        val summary = items.groupBy { it.name }.entries.joinToString(", ") { "${it.key} x${it.value.size}" }

        viewModelScope.launch {
            orderDao.insertOrder(
                OrderEntity(
                    itemsSummary = summary,
                    totalPrice = totalPrice,
                    paymentType = paymentType
                )
            )
            clearCurrentOrder()
            onComplete()
        }
    }

    fun addCustomer(name: String, nickname: String) {
        viewModelScope.launch {
            customerDao.insertCustomer(CustomerEntity(name = name, nickname = nickname, balance = 0.0))
        }
    }

    fun updateCustomerBalance(customer: CustomerEntity, amount: Double) {
        viewModelScope.launch {
            val updated = customer.copy(balance = customer.balance + amount)
            customerDao.updateCustomer(updated)
        }
    }
}
