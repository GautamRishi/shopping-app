package com.android.cartassignment.presentation.productlist.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.Navigator
import com.android.cartassignment.R
import com.android.cartassignment.core.Constants.USER_TYPE_ADMIN
import com.android.cartassignment.core.Constants.USER_TYPE_CUSTOMER

@Composable
fun ProductListTopBar(
    userName: String,
    userType: String,
    navigate: () -> Unit,
    signOut: () -> Unit
) {
    var openMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = colorResource(id = R.color.white)

                )
            )
        },
        backgroundColor = colorResource(id = R.color.yellow_700),
        actions = {
            if (userType.equals(USER_TYPE_CUSTOMER, ignoreCase = true)) {
                IconButton(
                    onClick = {
                        navigate()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingCart,
                        contentDescription = stringResource(R.string.my_cart_title),
                        tint = colorResource(id = R.color.white)
                    )
                }
            }
            IconButton(
                onClick = {
                    openMenu = !openMenu
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "Name: $userName Role: $userType ${stringResource(R.string.sign_out_item)}",
                    tint = colorResource(id = R.color.white)
                )
            }
            DropdownMenu(
                expanded = openMenu,
                onDismissRequest = {
                    openMenu = !openMenu
                }
            ) {
                DropdownMenuItem(
                    onClick = {
                        signOut()
                        openMenu = !openMenu
                    }
                ) {
                    Text(
                        text = "Name: $userName \n\n Role: $userType \n\n ${stringResource(R.string.sign_out_item)}",
                        style = TextStyle(
                            color = colorResource(id = R.color.yellow_900)

                        )
                    )
                }
            }
        }
    )
}