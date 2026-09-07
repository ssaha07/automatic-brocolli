package io.github.chkrb.pqcompanion.ui.pages.shop

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.navigation.NavController
import io.github.chkrb.pqcompanion.ui.viewmodels.ShopViewModel

@Composable
fun ShopExplorePage(navController: NavController, vm: ShopViewModel): Unit {
    Scaffold {
        LazyColumn {
            items(vm.retailerStatus!!.products) { retailerProduct ->
                Text(retailerProduct.catalogProduct.name)
            }
        }
    }
}
