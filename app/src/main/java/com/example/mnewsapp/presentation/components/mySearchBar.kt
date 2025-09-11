package com.example.mnewsapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mnewsapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MSearchBar(
    hint: String = "",
    query: String,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onClearClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
){

    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        title = {

            TextField(
                modifier = Modifier.fillMaxWidth()
                    .height(50.dp)
                    .background(color = Color.Transparent,
                        shape = RoundedCornerShape(50.dp)
                    ),

                value = query,
                shape = RoundedCornerShape(50.dp),
                onValueChange = {

                    onValueChange(it)
                },
                placeholder = {
                    Text(text = hint,
                        modifier = Modifier.padding(start = 5.dp)
                            .fillMaxHeight(),
                        color = Color(0xffbdbdbd)
                    )
                },
                keyboardActions = keyboardActions,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                singleLine = true,
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = Color.White,
                    unfocusedPlaceholderColor = Color.White,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onBackground,

                ),
                trailingIcon = {

                    if(query.isNotEmpty()) {

                        IconButton(onClick = {
                            onClearClick()
                        },
                            modifier = Modifier.padding(end = 7.dp))
                        {

                            Icon(
                                painter = painterResource(id = R.drawable.ic_clear),
                                modifier = Modifier.size(24.dp),
                                contentDescription = "Search Icon",
                                tint = MaterialTheme.colorScheme.onBackground)

                        }

                    }


                })

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent

            ),
        scrollBehavior = scrollBehavior)







}