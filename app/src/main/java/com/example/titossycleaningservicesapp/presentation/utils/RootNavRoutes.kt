package com.example.titossycleaningservicesapp.presentation.utils

sealed class RootNavRoutes(val route: String) {
    data object ROOT : RootNavRoutes("ROOT")
    data object ONBOARDING : RootNavRoutes("onBoarding")
    data object AUTH : RootNavRoutes("AUTH")
    data object HOME : RootNavRoutes("HOME")
}

sealed class Authentication(val route: String) {
    data object LOGIN : Authentication("LOGIN")
    data object SIGNUP : Authentication("SIGN_UP")
    data object EMPLOYEE : Authentication("employee_sign_in")
}

/*Navigation user routes*/
sealed class UserRoutes(val route: String) {
    data object Manager : UserRoutes("manager")
    data object Finance : UserRoutes("finance")
    data object Supervisor : UserRoutes("supervisor")
    data object Cleaner : UserRoutes("cleaner")
    data object Supplier : UserRoutes("supplier")
    data object Customer : UserRoutes("customer")
}