package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

data class Dish(
    val name: String,
    val description: String,
    val price: Int
)

private val menu = listOf(
    Dish("宫保鸡丁", "鸡肉鲜嫩，香辣下饭", 26),
    Dish("鱼香肉丝", "酸甜微辣，经典川味", 24),
    Dish("番茄炒蛋", "家常美味，营养搭配", 16),
    Dish("红烧牛肉", "软烂入味，酱香浓郁", 38),
    Dish("清炒时蔬", "清爽可口，新鲜时蔬", 14),
    Dish("米饭", "香软米饭，每份一碗", 2)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                OrderScreen()
            }
        }
    }
}

@Composable
fun OrderScreen() {
    val quantities = remember {
        mutableStateListOf<Int>().apply {
            repeat(menu.size) { add(0) }
        }
    }

    var showConfirm by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var submittedTotal by remember { mutableStateOf(0) }

    val totalCount = quantities.sum()
    val totalPrice = menu.indices.sumOf { index ->
        menu[index].price * quantities[index]
    }

    Scaffold(
        bottomBar = {
            Surface(
                tonalElevation = 4.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("已选 $totalCount 份")
                        Text(
                            text = "合计：¥$totalPrice",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Button(
                        onClick = { showConfirm = true },
                        enabled = totalCount > 0
                    ) {
                        Text("确认订单")
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "美味点单",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "欢迎光临，请选择喜欢的菜品",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            itemsIndexed(menu) { index, dish ->
                DishCard(
                    dish = dish,
                    quantity = quantities[index],
                    onDecrease = {
                        if (quantities[index] > 0) {
                            quantities[index]--
                        }
                    },
                    onIncrease = {
                        quantities[index]++
                    }
                )
            }
        }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("确认订单") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    menu.forEachIndexed { index, dish ->
                        val quantity = quantities[index]
                        if (quantity > 0) {
                            Text(
                                "${dish.name} × $quantity" +
                                        "    ¥${dish.price * quantity}"
                            )
                        }
                    }

                    HorizontalDivider()

                    Text(
                        text = "共 $totalCount 份，合计 ¥$totalPrice",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        submittedTotal = totalPrice
                        showConfirm = false
                        quantities.indices.forEach { index ->
                            quantities[index] = 0
                        }
                        showSuccess = true
                    }
                ) {
                    Text("提交订单")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirm = false }
                ) {
                    Text("继续选菜")
                }
            }
        )
    }

    if (showSuccess) {
        AlertDialog(
            onDismissRequest = { showSuccess = false },
            title = { Text("下单成功") },
            text = {
                Text("订单金额：¥$submittedTotal\n感谢您的惠顾！")
            },
            confirmButton = {
                TextButton(
                    onClick = { showSuccess = false }
                ) {
                    Text("好的")
                }
            }
        )
    }
}

@Composable
fun DishCard(
    dish: Dish,
    quantity: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = dish.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = dish.description,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "¥${dish.price}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDecrease,
                        enabled = quantity > 0,
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Text("−", fontSize = 22.sp)
                    }

                    Text(
                        text = quantity.toString(),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        fontSize = 18.sp
                    )

                    Button(
                        onClick = onIncrease,
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Text("+", fontSize = 22.sp)
                    }
                }
            }
        }
    }
}