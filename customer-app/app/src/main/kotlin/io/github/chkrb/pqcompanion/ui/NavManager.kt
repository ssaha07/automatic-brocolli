package io.github.chkrb.pqcompanion.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import io.github.chkrb.pqcompanion.ui.pages.HomePage
import io.github.chkrb.pqcompanion.ui.pages.shop.ShopCheckoutPage
import io.github.chkrb.pqcompanion.ui.pages.shop.ShopExplorePage
import io.github.chkrb.pqcompanion.ui.pages.shop.ShopScanPage

enum class NavDestination {
    HOME,
    SHOP,
    SHOP_SCAN,
    SHOP_EXPLORE,
    SHOP_CHECKOUT,
    ;

    fun route(): String =
        when (this) {
            HOME -> "/home"
            SHOP -> "/shop"
            SHOP_SCAN -> "/shop/scan"
            SHOP_EXPLORE -> "/shop/explore"
            SHOP_CHECKOUT -> "/shop/checkout"
        }
}

@Composable
fun NavManager() {
    val navController = rememberNavController()

    Surface(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = NavDestination.HOME.route(),
            enterTransition = { fadeIn() + slideInHorizontally { it } },
            exitTransition = { fadeOut() + slideOutHorizontally { -it } },
            popEnterTransition = { fadeIn() + slideInHorizontally { -it } },
            popExitTransition = { fadeOut() + slideOutHorizontally { it } },
        ) {
            composable(NavDestination.HOME.route()) { HomePage(navController) }
            navigation(
                route = NavDestination.SHOP.route(),
                startDestination = NavDestination.SHOP_SCAN.route(),
            ) {
                composable(NavDestination.SHOP_SCAN.route()) { ShopScanPage(navController) }
                composable(NavDestination.SHOP_EXPLORE.route()) { ShopExplorePage(navController) }
                composable(NavDestination.SHOP_CHECKOUT.route()) { ShopCheckoutPage(navController) }
            }
        }
    }
}
