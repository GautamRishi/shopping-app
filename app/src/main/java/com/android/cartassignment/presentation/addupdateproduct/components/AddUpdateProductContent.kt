package com.android.cartassignment.presentation.addupdateproduct.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.cartassignment.R
import com.android.cartassignment.components.LoadingIndicator
import com.android.cartassignment.core.ShowAlertDialog
import com.android.cartassignment.presentation.addupdateproduct.AddUpdateProductViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddUpdateProductContent(
    viewModel: AddUpdateProductViewModel = hiltViewModel(),
    productID: String,
    productName: String,
    productDetails: String,
    imageUrl: String,
    productQuantity: Int,
    price: Int,
    productVisibility: Boolean,
    innerPadding: PaddingValues,
    onSaveData: (String, String, Int, Int, String, Boolean) -> Unit,
    navigateBack: () -> Unit
) {
    var loading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var productName by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(productName)) }
    var productDetails by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(productDetails)) }
    var productQuantity by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(if (productQuantity > 0) productQuantity.toString() else "")) }
    var price by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) { mutableStateOf(TextFieldValue(if (price > 0) price.toString() else "")) }
    var productVisibility by rememberSaveable { mutableStateOf(productVisibility) }
    var imageUri by remember { mutableStateOf(imageUrl) }
    var selectedImageUri by remember { mutableStateOf("") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = uri.toString()
            selectedImageUri = uri.toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .height(200.dp)
                .padding(16.dp),
            backgroundColor = Color.White,
            shape = RoundedCornerShape(10.dp),
            elevation = 3.dp,
            border = BorderStroke(1.dp, colorResource(R.color.gray)),
            onClick = {
                imagePickerLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
            }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(if (imageUri.isEmpty()) R.drawable.ic_add_gray else imageUri)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.loading_img),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = productDetails.text,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Product name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp),
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = productDetails,
                onValueChange = {
                    productDetails = it
                },
                label = { Text("Product details") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp),
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = price,
                    onValueChange = {
                        price = it
                    },
                    label = { Text("Price(₹)") },
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = productQuantity,
                    onValueChange = {
                        productQuantity = it
                    },
                    label = { Text("Quantity") },
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = productVisibility == false,
                    onClick = { productVisibility = false },
                    colors = RadioButtonDefaults.colors(selectedColor = Color.Black)
                )
                Text(text = "Draft")
                Spacer(modifier = Modifier.width(24.dp))
                RadioButton(
                    selected = productVisibility == true,
                    onClick = { productVisibility = true },
                    colors = RadioButtonDefaults.colors(selectedColor = Color.Black)
                )
                Text(text = "Live")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    onSaveData(
                        productName.text,
                        productDetails.text,
                        price.text.toInt(),
                        productQuantity.text.toInt(),
                        selectedImageUri,
                        productVisibility
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.yellow_900)
                )
            ) {
                Text("Save")
            }
            if (productID.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        showDeleteConfirmation = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red
                    )
                ) {
                    Text("Delete")
                }
            }
        }
    }
    if (loading) {
        LoadingIndicator()
    }
    if (showDialog) {
        ShowAlertDialog(
            dialogMessage = dialogMessage,
            onDismiss = {
                showDialog = false
                dialogMessage = ""
                navigateBack()
            }
        )
    }
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            text = { Text("Are you sure you want to delete this ${productName.text}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                        loading = true
                        viewModel.deleteProduct(productID = productID,
                            onSuccess = {
                                loading = false
                                showDialog = true
                                dialogMessage = "${productName.text} deleted successfully"
                            },
                            onFailure = { exception ->
                                loading = false
                                showDialog = true
                                dialogMessage = exception.message ?: ""
                            }
                        )
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmation = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}