package com.example.generatedapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF121212)
            ) {
                CalculatorScreen()
            }
        }
    }
}

@Composable
iotensin CalculatorScreen() {
    var input by remember { mutableStateOf("0") }
    var firstValue by remember { mutableStateOf<Double?>(null) }
    var operation by remember { mutableStateOf<String?>(null) }
    var isNewEntry by remember { mutableStateOf(true) }

    fun onNumberClick(number: String) {
        if (isNewEntry) {
            input = number
            isNewEntry = false
        } else {
            input = if (input == "0") number else input + number
        }
    }

    fun onOperationClick(op: String) {
        firstValue = input.toDoubleOrNull()
        operation = op
        isNewEntry = true
    }

    fun onEqualsClick() {
        val secondValue = input.toDoubleOrNull()
        if (firstValue != null && operation != null && secondValue != null) {
            val result = when (operation) {
                "+" -> firstValue!! + secondValue
                "-" -> firstValue!! - secondValue
                "*" -> firstValue!! * secondValue
                "/" -> {
                    if (secondValue == 0.0) {
                        null
                    } else {
                        firstValue!! / secondValue
                    }
                }
                else -> null
            }

            if (result == null) {
                input = "Hata"
            } else {
                input = if (result == result.toLong().toDouble()) {
                    result.toLong().toString()
                } else {
                    result.toString()
                }
            }
            firstValue = null
            operation = null
            isNewEntry = true
        }
    }

    fun onClearClick() {
        input = "0"
        firstValue = null
        operation = null
        isNewEntry = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Ekran
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = input,
                color = Color.White,
                fontSize = 64.sp,
                textAlign = TextAlign.End,
                maxLines = 1
            )
        }

        // Tuş Takımı
        val buttonColorsOp = Color(0xFFFF9F0A)
        val buttonColorsNum = Color(0xFF2D2D2D)
        val buttonColorsClear = Color(0xFFA5A5A5)

        val buttonSize = 80.dp

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CalculatorButton("C", buttonColorsClear, Color.Black, buttonSize) { onClearClick() }
                CalculatorButton("", Color.Transparent, Color.Transparent, buttonSize) {}
                CalculatorButton("", Color.Transparent, Color.Transparent, buttonSize) {}
                CalculatorButton("/", buttonColorsOp, Color.White, buttonSize) { onOperationClick("/") }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CalculatorButton("7", buttonColorsNum, Color.White, buttonSize) { onNumberClick("7") }
                CalculatorButton("8", buttonColorsNum, Color.White, buttonSize) { onNumberClick("8") }
                CalculatorButton("9", buttonColorsNum, Color.White, buttonSize) { onNumberClick("9") }
                CalculatorButton("*", buttonColorsOp, Color.White, buttonSize) { onOperationClick("*") }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CalculatorButton("4", buttonColorsNum, Color.White, buttonSize) { onNumberClick("4") }
                CalculatorButton("5", buttonColorsNum, Color.White, buttonSize) { onNumberClick("5") }
                CalculatorButton("6", buttonColorsNum, Color.White, buttonSize) { onNumberClick("6") }
                CalculatorButton("-", buttonColorsOp, Color.White, buttonSize) { onOperationClick("-") }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CalculatorButton("1", buttonColorsNum, Color.White, buttonSize) { onNumberClick("1") }
                CalculatorButton("2", buttonColorsNum, Color.White, buttonSize) { onNumberClick("2") }
                CalculatorButton("3", buttonColorsNum, Color.White, buttonSize) { onNumberClick("3") }
                CalculatorButton("+", buttonColorsOp, Color.White, buttonSize) { onOperationClick("+") }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CalculatorButton("0", buttonColorsNum, Color.White, buttonSize) { onNumberClick("0") }
                CalculatorButton(".", buttonColorsNum, Color.White, buttonSize) { onNumberClick(".") }
                CalculatorButton("=", buttonColorsOp, Color.White, buttonSize) { onEqualsClick() }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    symbol: String,
    bgColor: Color,
    textColor: Color,
    size: Dp,
    onClick: () -> Unit
) {
    if (symbol.isEmpty()) {
        Spacer(modifier = Modifier.size(size))
        return
    }
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(size)
            .background(bgColor, CircleShape),
        colors = ButtonDefaults.buttonColors(containerColor = bgColor)
    ) {
        Text(
            text = symbol,
            fontSize = 32.sp,
            color = textColor
        )
    }
}
