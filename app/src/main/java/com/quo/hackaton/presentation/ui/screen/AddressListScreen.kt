package com.quo.hackaton.presentation.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quo.hackaton.R
import com.quo.hackaton.domain.model.Company
import com.quo.hackaton.domain.model.Status

@Composable
fun AddressListScreen(
    companies: List<Company>,
//    onSearch: (String) -> Unit,
    onStatusChange: (Company, Status) -> Unit,
    onShowMap: () -> Unit,
//    onMark: (String) -> Unit,
) {
    val ralewayBold = FontFamily(Font(R.font.raleway_bold, FontWeight.Bold))
    val ralewayMedium = FontFamily(Font(R.font.raleway_medium, FontWeight.Medium))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.big_logo),
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )
        Spacer(modifier = Modifier.height(11.dp))
        Text(
            text = "Потенциальные нарушения",
            fontFamily = ralewayBold,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color(0xFF2B2B2B),
            style = TextStyle(letterSpacing = 0.sp),
        )
        Spacer(modifier = Modifier.height(22.dp))
        var query by remember { mutableStateOf("") }
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = {
                    Text(
                        text = "Введите адрес...",
                        fontFamily = ralewayMedium,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = Color(0xFF2B2B2B).copy(alpha = 0.75f),
                        textAlign = TextAlign.Center
                    )
                },
                modifier = Modifier
                    .weight(0.8f)
                    .height(36.dp)
                    .padding(0.dp)
                    .border(
                        BorderStroke(0.75.dp, Color(0xFF2B2B2B).copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(10.dp)
                    ),
                textStyle = TextStyle(
                    fontFamily = ralewayMedium,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color.Black,
                    letterSpacing = 0.sp
                ),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                ),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {  },  // TODO search
                modifier = Modifier
                    .weight(0.4f)
                    .height(36.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF11A538))
            ) {
                Text(
                    text = "Поиск",
                    fontFamily = ralewayMedium,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = Color(0xFFF7F7F7),
                    style = TextStyle(letterSpacing = 0.sp),
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Подготовить маршрут",
            fontFamily = ralewayMedium,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            color = Color(0xFF2B2B2B),
            style = TextStyle(letterSpacing = 0.sp, textDecoration = TextDecoration.Underline),
            modifier = Modifier.clickable { onShowMap() }
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(companies) { company ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEDEDED)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = company.address,
                                fontFamily = ralewayMedium,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color = Color(0xFF11A538),
                                style = TextStyle(
                                    letterSpacing = 0.sp,
                                    textDecoration = TextDecoration.Underline
                                ),
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = company.name,
                                fontFamily = ralewayMedium,
                                fontWeight = FontWeight.Medium,
                                fontSize = 8.sp,
                                color = Color(0xFF5B5959),
                                style = TextStyle(letterSpacing = 0.sp),
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { onStatusChange(company, Status.OK) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(0.4f)
                                .height(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD8D8D8))
                        ) {
                            Text(
                                text = "Отметить",
                                fontFamily = ralewayMedium,
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp,
                                color = Color(0xFF2B2B2B),
                                style = TextStyle(letterSpacing = 0.sp),
                            )
                        }
                    }
                }
            }
        }
    }
}
