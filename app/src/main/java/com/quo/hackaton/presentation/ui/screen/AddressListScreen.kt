package com.quo.hackaton.presentation.ui.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quo.hackaton.R
import com.quo.hackaton.domain.model.Company
import com.quo.hackaton.domain.model.Status

val ralewayBold = FontFamily(Font(R.font.raleway_bold, FontWeight.Bold))
val ralewayMedium = FontFamily(Font(R.font.raleway_medium, FontWeight.Medium))

@Composable
fun AddressListScreen(
    companies: List<Company>,
    onCommentUpdate: (Company, String) -> Unit,
    onShowMap: () -> Unit,
) {
    var query by remember { mutableStateOf("") }
    var filteredCompanies by remember(companies) { mutableStateOf(companies) }

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
        Row(verticalAlignment = Alignment.CenterVertically) {
            CustomSearchField(
                query = query,
                onQueryChange = { newText -> query = newText },
                modifier = Modifier.weight(0.8f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (query.isNotBlank()) {
                        filteredCompanies = companies.filter { company ->
                            company.address.contains(query, ignoreCase = true)
                                    || company.name.contains(query, ignoreCase = true)
                        }
                        filteredCompanies.forEach { comp ->
                            Log.d("Filter", comp.address)
                        }
                    }
                },
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxWidth(),
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
            fontSize = 18.sp,
            color = Color(0xFF2B2B2B),
            style = TextStyle(letterSpacing = 0.sp, textDecoration = TextDecoration.Underline),
            modifier = Modifier.clickable { onShowMap() }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Log.d("size", filteredCompanies.size.toString())
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = filteredCompanies,
                key = { it.id }
            ) { company ->
                CompanyCard(
                    company = company,
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth()
                        .height(32.dp),
                    onConfirm = { comment -> onCommentUpdate(company, comment) }
                )
            }
        }
    }
}


@Composable
fun CustomSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Введите адрес..."
) {
    val shape = RoundedCornerShape(10.dp)
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        cursorBrush = SolidColor(Color(0xFF2B2B2B)),
        textStyle = TextStyle(
            fontFamily = ralewayMedium,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = Color(0xFF2B2B2B),
            letterSpacing = 0.sp
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier
                    .height(40.dp)
                    .border(
                        width = 0.75.dp,
                        color = Color(0xFF2B2B2B),
                        shape = shape
                    )
                    .clip(shape)
                    .padding(horizontal = 13.dp, vertical = 6.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (query.isEmpty()) {
                    Text(
                        text = placeholder,
                        fontFamily = ralewayMedium,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = Color(0xFF2B2B2B).copy(alpha = 0.75f),
                        letterSpacing = 0.sp,
                    )
                }
                innerTextField()
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun CommentInput(
    commentText: String,
    onCommentChange: (String) -> Unit,
    onCameraClick: () -> Unit
) {
    BasicTextField(
        value = commentText,
        onValueChange = onCommentChange,
        singleLine = true,
        cursorBrush = SolidColor(Color(0xFF2B2B2B)),
        textStyle = TextStyle(
            letterSpacing = 0.sp,
            fontFamily = ralewayMedium,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Color(0xFF2B2B2B)
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color(0xFFD9D9D9), RoundedCornerShape(8.dp))
                    .padding(start = 13.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // placeholder
                Box(modifier = Modifier.weight(1f)) {
                    if (commentText.isEmpty()) {
                        Text(
                            text = "Комментарий к нарушению…",
                            fontFamily = ralewayMedium,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp,
                            color = Color(0xFF5B5959),
                            letterSpacing = 0.sp
                        )
                    }
                    innerTextField()
                }
                IconButton(onClick = onCameraClick, modifier = Modifier.padding(4.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.ic_camera),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun  CompanyCard(
    company: Company,
    modifier: Modifier,
    onConfirm: (String) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<Status?>(Status.PENDING) }
    var expandedComment by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEDEDED)),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        modifier = Modifier.padding(top = 18.dp),
                        text = company.address,
                        fontFamily = ralewayMedium,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF11A538),
                        letterSpacing = 0.sp,
                        lineHeight = 16.sp,
                        textDecoration = TextDecoration.Underline
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        modifier = Modifier.padding(bottom = 4.dp),
                        text = company.name,
                        fontFamily = ralewayMedium,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF5B5959),
                        letterSpacing = 0.sp
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Box(modifier = Modifier.weight(0.5f)) {
                    when (selectedOption) {
                        Status.PENDING -> {
                            Button(
                                onClick = { menuExpanded = true },
                                shape = RoundedCornerShape(8.dp),
                                modifier = modifier,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFFD8D8D8
                                    )
                                )
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

                        else -> {
                            OutlinedButton(
                                onClick = { menuExpanded = true },
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(
                                    1.dp, when (selectedOption) {
                                        Status.OK -> Color(0xFF11A538)
                                        else -> Color(0xFFDD2E13)
                                    }
                                ),
                                modifier = modifier,
                                contentPadding = PaddingValues(horizontal = 8.dp),
                            ) {
                                Text(
                                    text = when (selectedOption) {
                                        Status.OK -> "Нет нарушений"
                                        else -> "Нарушение"
                                    },
                                    fontFamily = ralewayMedium,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 11.sp,
                                    color = when (selectedOption) {
                                        Status.OK -> Color(0xFF11A538)
                                        else -> Color(0xFFDD2E13)
                                    },
                                    letterSpacing = 0.sp
                                )
                                if (selectedOption == Status.VIOLATION) {
                                    IconButton(
                                        onClick = { expandedComment = !expandedComment }
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_arrow_down),
                                            contentDescription = "Show comment",
                                            tint = Color(0xFFDD2E13)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier
                            .background(Color.White)
                            .wrapContentSize()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF7F7F7), shape = RoundedCornerShape(10.dp))
                            .border(0.5.dp, Color(0xFF2B2B2B), shape = RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp),
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedOption == Status.VIOLATION,
                                        onClick = {
                                            selectedOption = Status.VIOLATION
                                            menuExpanded = false
                                        }
                                    )
                                    Text(
                                        text = "Подтвердить нарушение",
                                        fontFamily = ralewayMedium,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp,
                                        color = Color(0xFF2B2B2B),
                                        letterSpacing = 0.sp
                                    )
                                }
                            },
                            onClick = {
                                selectedOption = Status.VIOLATION
                                menuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = selectedOption == Status.OK,
                                        onClick = {
                                            selectedOption = Status.OK
                                            menuExpanded = false
                                        }
                                    )
                                    Text(
                                        text = "Нет нарушения",
                                        fontFamily = ralewayMedium,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp,
                                        color = Color(0xFF2B2B2B),
                                        letterSpacing = 0.sp
                                    )
                                }
                            },
                            onClick = {
                                selectedOption = Status.OK
                                menuExpanded = false
                            }
                        )
                    }
                }
            }

            if (expandedComment) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp)
                        .background(color = Color(0xFFEDEDED), shape = RoundedCornerShape(8.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(0.9f).padding(bottom = 18.dp)) {
                        CommentInput(commentText, { newText -> commentText = newText }, { })
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = {
                            expandedComment = false
                            onConfirm(commentText)
                            commentText = ""
                        },
                        modifier = Modifier.weight(0.1f).aspectRatio(1f).padding(bottom = 18.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_check),
                            contentDescription = null,
                            tint = Color(0xFF2B2B2B),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}