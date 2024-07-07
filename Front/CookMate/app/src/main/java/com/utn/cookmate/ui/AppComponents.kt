package com.utn.cookmate.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton


import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.utn.cookmate.R
import java.util.Base64

@Composable
fun RecetaEnLista(value: String, funBorrar : (idReceta:String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = value,
            color = Color.Black,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable { println("click en receta") }
        )
        Spacer(modifier = Modifier.weight(1f).clickable { println("click en receta")})
        Text(
            modifier = Modifier.clickable { funBorrar(value) },
            text = "\uD83D\uDDD1\uFE0F",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun RecetaCard(idRecetaElegida: String, funcionRecetaElegida : (idReceta:String) -> Unit){
    var localFocusManager = LocalFocusManager.current
    Card (
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(24.dp).size(130.dp),
        elevation = CardDefaults.cardElevation(4.dp)){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = Color.Green,// else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                ).clickable {
                    //ir a la otra pantalla
                    funcionRecetaElegida(idRecetaElegida) //llama a la funcion que recibio por parametro
                    localFocusManager.clearFocus()
                }){

        }

    }
}

@Composable
fun TopBar(value: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = value,
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier.size(80.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de CookMate"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar("Preview Text")
}

@Composable
fun NormalBar(textValue: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = textValue,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CheckboxRow(textValue: String,onClickCallback:()->Unit) {
    val checked = remember { mutableStateOf(false) }
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = textValue,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.weight(1f))
        Checkbox(
            checked = checked.value,
            onCheckedChange = {
                checked?.value = it
                onClickCallback()
            }
        )
    }

}

var blankImage : ByteArray = Base64.getDecoder().decode("/9j/4AAQSkZJRgABAQEAYABgAAD/4QBmRXhpZgAATU0AKgAAAAgABgESAAMAAAABAAEAAAMBAAUAAAABAAAAVgMDAAEAAAABAAAAAFEQAAEAAAABAQAAAFERAAQAAAABAAAOw1ESAAQAAAABAAAOwwAAAAAAAYagAACxj//bAEMAAgEBAgEBAgICAgICAgIDBQMDAwMDBgQEAwUHBgcHBwYHBwgJCwkICAoIBwcKDQoKCwwMDAwHCQ4PDQwOCwwMDP/bAEMBAgICAwMDBgMDBgwIBwgMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDP/AABEIAK8AxwMBIgACEQEDEQH/xAAfAAABBQEBAQEBAQAAAAAAAAAAAQIDBAUGBwgJCgv/xAC1EAACAQMDAgQDBQUEBAAAAX0BAgMABBEFEiExQQYTUWEHInEUMoGRoQgjQrHBFVLR8CQzYnKCCQoWFxgZGiUmJygpKjQ1Njc4OTpDREVGR0hJSlNUVVZXWFlaY2RlZmdoaWpzdHV2d3h5eoOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4eLj5OXm5+jp6vHy8/T19vf4+fr/xAAfAQADAQEBAQEBAQEBAAAAAAAAAQIDBAUGBwgJCgv/xAC1EQACAQIEBAMEBwUEBAABAncAAQIDEQQFITEGEkFRB2FxEyIygQgUQpGhscEJIzNS8BVictEKFiQ04SXxFxgZGiYnKCkqNTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqCg4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2dri4+Tl5ufo6ery8/T19vf4+fr/2gAMAwEAAhEDEQA/AP38ooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKAP/2Q==")
@Composable
fun NormalBar(textValue: String, imageBytes:ByteArray?) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = textValue,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.weight(1f))
        if(imageBytes != null){
            Image(
                bitmap = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size).asImageBitmap(),
                contentDescription = "contentDescription"
            )
        } else {
            Image(
                bitmap = BitmapFactory.decodeByteArray(blankImage,0,blankImage.size).asImageBitmap(),
                contentDescription = "contentDescription"
            )
        }

    }
}

@Composable
fun TextComponent(
    textValue: String,
    textSize: TextUnit,
    colorValue: Color = Color.Black,
    textAlign: TextAlign = TextAlign.Start
) { //por default usara color negro
    Text(text = textValue, fontSize = textSize, color = colorValue, fontWeight = FontWeight.Light, textAlign = textAlign)
}

@Preview(showBackground = true)
@Composable
fun TextComponentPreview() {
    TextComponent(textValue = "Preview texto", textSize = 24.sp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldComponent(
    placeholderText : String,
    onTextChanged : (name:String) -> Unit
) {
    var currentValue by remember {
        mutableStateOf("")
    }
    var localFocusManager = LocalFocusManager.current
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = currentValue,
        onValueChange = {
            currentValue = it
            onTextChanged(it)  //it es el valor que puso el usuario
                        },
        placeholder = { Text(text = placeholderText, fontSize = 18.sp) },
        textStyle = TextStyle.Default.copy(fontSize = 24.sp),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
            //onTextChanged(currentValue)
        }
        )
}

@Composable
fun FoodCard(image:Int, selected: Boolean, foodSelected : (foodName:String) -> Unit){
    var localFocusManager = LocalFocusManager.current
    Card (
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(24.dp)
            .size(130.dp),
        elevation = CardDefaults.cardElevation(4.dp)){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = if (selected) Color.Green else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )){
                    Image(
                        modifier =
                        Modifier
                            .padding(16.dp)
                            .wrapContentHeight()
                            .wrapContentWidth()
                            .clickable {
                                val foodName =
                                    if (image == R.drawable.pizza) "pizza" else "hamburguesa"
                                foodSelected(foodName)
                                localFocusManager.clearFocus()
                            },
                            painter = painterResource(id = image),
                            contentDescription = "imagen de hamburguesa"
                    )
                }

    }
}

@Preview
@Composable
fun FoodCardPreview(){
    FoodCard(R.drawable.pizza,true, {})
}

//@Preview(showBackground = true)
//@Composable
//fun TextFieldComponentPreview() {
//    TextFieldComponent()
//}

@Composable
fun ButtonComponent(
        goToDetailsScreen : () -> Unit   //funcion que no recibe nada y devuelve Unit
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {goToDetailsScreen()}
    ) {
        TextComponent(textValue = "Go to details screen", textSize = 18.sp,colorValue = Color.White)
    }

}

@Composable
fun CustomAlertDialog(onClickFunction : () -> Unit,titulo:String,texto:String,textoBoton:String,userInputViewModel:UserInputViewModel,shouldShowDialog: MutableState<Boolean>) {
    if (shouldShowDialog.value) {
        AlertDialog(
            onDismissRequest = {
                onClickFunction()
                shouldShowDialog.value = false
            },
            title = { Text(text = titulo) },
            text = { Text(text = texto) },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_700)),
                    onClick = {
                        onClickFunction()
                        shouldShowDialog.value = false
                    },
                ) {
                    Text(
                        text = textoBoton,
                        color = Color.White
                    )
                }
            }
        )
    }
}

@Composable
fun CustomQuestionDialog(onClickNoFunction : () -> Unit,onClickSiFunction : () -> Unit,titulo:String,texto:String,textoBotonNo:String,textoBotonSi:String,userInputViewModel:UserInputViewModel,shouldShowDialog: MutableState<Boolean>) {
    if (shouldShowDialog.value) {
        AlertDialog(
            onDismissRequest = {
                onClickNoFunction()
                shouldShowDialog.value = false
            },
            title = { Text(text = titulo) },
            text = { Text(text = texto) },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_700)),
                    onClick = {
                        onClickSiFunction()
                        shouldShowDialog.value = false
                    },
                ) {
                    Text(
                        text = textoBotonSi,
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.purple_700)),
                    onClick = {
                        onClickNoFunction()
                        shouldShowDialog.value = false
                    },
                ) {
                    Text(
                        text = textoBotonNo,
                        color = Color.White
                    )
                }
            }
        )
    }
}