package com.example.generatedapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.generatedapp.data.CustomerEntity
import com.example.generatedapp.data.MenuItemEntity
import com.example.generatedapp.viewmodel.LokantaViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: LokantaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SanayiLokantasiTheme {
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
fun SanayiLokantasiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFFFD700),
            onPrimary = Color.Black,
            background = Color.Black,
            onBackground = Color.White,
            surface = Color(0xFF1E1E1E),
            onSurface = Color.White
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            content = content
        )
    }
}

@Composable
fun MainScreen(viewModel: LokantaViewModel) {
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFD700))
                .padding(16px_to_dp(16)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SANAYİ ESNAF LOKANTASI",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Tabs
        Row(
            modifier = Modifier.fillMaxWidth().background(Color(0xFF2C2C2C)),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TabButton(text = "Hızlı Satış", selected = selectedTab == 0) { selectedTab = 0 }
            TabButton(text = "Veresiye", selected = selectedTab == 1) { selectedTab = 1 }
            TabButton(text = "Rapor / Kasa", selected = selectedTab == 2) { selectedTab = 2 }
        }

        // Content
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> FastSaleScreen(viewModel)
                1 -> VeresiyeScreen(viewModel)
                2 -> ReportScreen(viewModel)
            }
        }
    }
}

fun 16px_to_dp(v: Int) = v.dp

@Composable
fun TabButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFFFFD700) else Color.Transparent,
            contentColor = if (selected) Color.Black else Color.White
        ),
        modifier = Modifier.padding(8.dp).height(50.dp)
    ) {
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun FastSaleScreen(viewModel: LokantaViewModel) {
    val menuItems by viewModel.menuItems.collectAsState()
    val currentOrder by viewModel.currentOrderItems.collectAsState()
    var showPaymentDialog by remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxSize()) {
        // Menu Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .weight(1.5f)
                .fillMaxHeight()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(menuItems) {
                item ->
                Button(
                    onClick = { viewModel.addToOrder(item) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333)),
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = item.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "${item.price} TL", color = Color(0xFFFFD700), fontSize = 14.sp)
                    }
                }
            }
        }

        // Current Order & Actions
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color(0xFF121212))
                .padding(8.dp)
        ) {
            Text(
                text = "Adisyon",
                color = Color(0xFFFFD700),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth()
            ) {
                items(currentOrder) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(Color(0xFF222222))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = item.name, color = Color.White, fontSize = 14.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "${item.price} TL", color = Color(0xFFFFD700), fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { viewModel.removeFromOrder(item) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                modifier = Modifier.size(36.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("-", color = Color.White, fontSize = 18.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            val total = currentOrder.sumOf { it.price }
            Text(
                text = "Toplam: $total TL",
                color = Color(0xFFFFD700),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.clearCurrentOrder() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                    modifier = Modifier.weight(1f).height(60.dp)
                ) {
                    Text("İptal", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { if (currentOrder.isNotEmpty()) showPaymentDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                    modifier = Modifier.weight(1f).height(60.dp)
                ) {
                    Text("Ödeme Al", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showPaymentDialog) {
        AlertDialog(
            onDismissRequest = { showPaymentDialog = false },
            title = { Text("Ödeme Tipi Seçin") },
            text = { Text("Toplam Tutar: ${currentOrder.sumOf { it.price }} TL") },
            confirmButton = {
                Button(onClick = {
                    viewModel.completeOrder("Nakit") {
                        showPaymentDialog = false
                    }
                }) {
                    Text("Nakit")
                }
            },
            dismissButton = {
                Button(onClick = {
                    viewModel.completeOrder("Kredi Kartı") {
                        showPaymentDialog = false
                    }
                }) {
                    Text("Kredi Kartı")
                }
            }
        )
    }
}

@Composable
fun VeresiyeScreen(viewModel: LokantaViewModel) {
    val customers by viewModel.customers.collectAsState()
    var showNewCustomerDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newNickname by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Veresiye Defteri (Cari)",
                color = Color(0xFFFFD700),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = { showNewCustomerDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
            ) {
                Text("Yeni Cari Aç", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(customers) { customer ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = customer.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(text = "(${customer.nickname})", color = Color.Gray, fontSize = 14.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "${customer.balance} TL", color = Color(0xFFFFD700), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { viewModel.updateCustomerBalance(customer, 50.0) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                            ) {
                                Text("+50 TL Ekle")
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Button(
                                onClick = { viewModel.updateCustomerBalance(customer, -50.0) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                            ) {
                                Text("Ödeme Tahsil Et")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showNewCustomerDialog) {
        AlertDialog(
            onDismissRequest = { showNewCustomerDialog = false },
            title = { Text("Yeni Cari Hesap") },
            text = {
                Column {
                    TextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Ad Soyad") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = newNickname,
                        onValueChange = { newNickname = it },
                        label = { Text("Lakap / Dükkan") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (newName.isNotBlank()) {
                        viewModel.addCustomer(newName, newNickname)
                        newName = ""
                        newNickname = ""
                        showNewCustomerDialog = false
                    }
                }) {
                    Text("Kaydet")
                }
            },
            dismissButton = {
                Button(onClick = { showNewCustomerDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
}

@Composable
fun ReportScreen(viewModel: LokantaViewModel) {
    val totalRevenue by viewModel.totalRevenue.collectAsState()
    val orders by viewModel.orders.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Gün Sonu Raporu",
            color = Color(0xFFFFD700),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Toplam Ciro", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "$totalRevenue TL", color = Color(0xFFFFD700), fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Son Siparişler", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(orders) { order ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF222222))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = order.itemsSummary, color = Color.White, fontSize = 14.sp)
                            Text(text = "${order.totalPrice} TL", color = Color(0xFFFFD700), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Ödeme: ${order.paymentType}", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
