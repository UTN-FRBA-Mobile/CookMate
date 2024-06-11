package com.utn.cookmate.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.utn.cookmate.ui.UserInputViewModel

@Composable
fun CookMateNavigationGraph(userInputViewModel: UserInputViewModel = viewModel()){
    val navController = rememberNavController();
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

        composable(Routes.ABOUT_SCREEN){
            AboutScreen(navController)
        }

        composable(Routes.MIS_RECETAS_SCREEN){
            MisRecetasScreen(userInputViewModel,navController)
        }

        composable(Routes.GENERAR_RECETA_SCREEN){
            GenerarRecetaScreen(userInputViewModel,navController)
        }

        composable(Routes.RECETAS_ENCONTRADAS_SCREEN){
            RecetasEncontradasScreen(userInputViewModel,navController)
        }

        composable(Routes.PASO_A_PASO_SCREEN){
            PasoAPasoScreen(userInputViewModel,navController)
        }

    }

}