package com.utn.cookmate.ui.screens

import GenerarRecetaScreen
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.utn.cookmate.connection.Server
import com.utn.cookmate.ui.UserInputViewModel

@Composable
fun CookMateNavigationGraph(userInputViewModel: UserInputViewModel = viewModel()){
    val navController = rememberNavController();
    Server(userInputViewModel).checker()
    NavHost(navController = navController, startDestination = Routes.LOGIN_SCREEN) {
//        composable(Routes.USER_INPUT_SCREEN){
//            UserInputScreen(userInputViewModel,
//                showWelcomeScreen={
//                    println("Coming inside showWelcomeScreen")
//                    println(it.first)
//                    println(it.second)
//                    navController.navigate(Routes.WELCOME_SCREEN + "/${it.first}/${it.second}")
//                }
//            );
//        }
//
//        composable("${Routes.WELCOME_SCREEN}/{${Routes.USER_NAME}}/{${Routes.FOOD_SELECTED}}",arguments = listOf(
//            navArgument(name = Routes.USER_NAME) {type= NavType.StringType},
//            navArgument(name = Routes.FOOD_SELECTED) {type= NavType.StringType}
//
//        )){
//            var username = it?.arguments?.getString(Routes.USER_NAME)
//            var foodSelected = it?.arguments?.getString(Routes.FOOD_SELECTED)
//            WelcomeScreen(username,foodSelected);
//        }

        composable(Routes.LOGIN_SCREEN){
            LoginScreen(userInputViewModel,navController)
        }

        composable(Routes.CREATE_USER_SCREEN){
            CreateUserScreen(userInputViewModel,navController)
        }

        composable(Routes.ABOUT_SCREEN){
            AboutScreen(navController)
        }

        composable(Routes.MIS_RECETAS_SCREEN){
            MisRecetasScreen(userInputViewModel,navController)
        }

        composable(Routes.BUSCAR_RECETA_ONLINE_SCREEN){
            GenerarRecetaScreen(userInputViewModel,navController)
        }

        composable(Routes.RECETAS_ENCONTRADAS_SCREEN){
            RecetasEncontradasScreen(userInputViewModel,navController)
        }

        composable(Routes.RECETAS_ENCONTRADAS_NOESTRICTO_SCREEN){
            RecetasEncontradasNoEstrictoScreen(userInputViewModel,navController)
        }

        composable(Routes.PASO_A_PASO_SCREEN){
            PasoAPasoScreenWithPermissionCheck(userInputViewModel,navController)
        }

    }

}
@Composable
fun PasoAPasoScreenWithPermissionCheck(
    userInputViewModel: UserInputViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    // Función para manejar el inicio de la pantalla paso a paso
    val startPasoAPaso = {
        navController.navigate(Routes.PASO_A_PASO_SCREEN)
    }

    // Función para manejar la lógica cuando se conceden los permisos
    val onPermissionGranted = {
        startPasoAPaso()
    }

    // Función para mostrar el diálogo de solicitud de permisos si no están concedidos
    val showPermissionDialog = remember { mutableStateOf(false) }

    // Lanzar efecto para verificar y solicitar permisos
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.FOREGROUND_SERVICE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            showPermissionDialog.value = true
        } else {
            startPasoAPaso()
        }
    }

    // Mostrar el diálogo de solicitud de permisos
    if (showPermissionDialog.value) {
        PermissionRequestDialog(
            permission = Manifest.permission.FOREGROUND_SERVICE,
            onPermissionGranted = onPermissionGranted
        )
    }
}
