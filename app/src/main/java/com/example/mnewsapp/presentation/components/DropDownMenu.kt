package com.example.mnewsapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mnewsapp.R
import com.example.mnewsapp.ui.theme.secondTitleColor

@Composable
fun DropdownMenu(isOpen: Boolean,
                 onDismissClick:() -> Unit,
                 onMenuItemClick:() -> Unit,
                 onThreeDotsClick:() -> Unit){


    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {

        IconButton(onClick = {
            onThreeDotsClick()
        }) {

            Icon(painter = painterResource(id = R.drawable.ic_more),
                contentDescription = "Three Dots Icon",
                tint = secondTitleColor)


        }

        DropdownMenu(
            expanded = isOpen,
            onDismissRequest = {
                onDismissClick()
            },
            containerColor = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(width = 0.5.dp, color = secondTitleColor)
        ) {

            DropdownMenuItem(
                text = {
                    Text(
                        text = "Delete All",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                leadingIcon = {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_del),
                        contentDescription = "Delete Icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                onClick = {
                    onMenuItemClick()
                },

                )

        }



    }









}