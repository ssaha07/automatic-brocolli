package io.github.chkrb.pqcompanion.ui.viewmodels

import android.util.Log
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.chkrb.pqcompanion.R
import io.github.chkrb.pqcompanion.data.Catalog
import io.github.chkrb.pqcompanion.data.RetailerStatus
import java.io.InputStream

class ShopViewModel(globalCatalogStream: InputStream) : ViewModel() {
    var catalog: Catalog
    var retailerStatus: RetailerStatus? = null

    init {
        catalog = Catalog.loadFromJsonStream(globalCatalogStream)
    }

    fun reset() {
        retailerStatus = null
    }

    fun processRetailerStatusData(obj: RetailerStatus) {
        retailerStatus = obj
    }
}

class ShopViewModelFactory(private val globalCatalogStream: InputStream) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(ShopViewModel::class.java) -> {
                ShopViewModel(globalCatalogStream) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class $modelClass")
        }
}
