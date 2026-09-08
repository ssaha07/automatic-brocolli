package io.github.chkrb.pqcompanion.data

import android.util.Log

@OptIn(kotlin.ExperimentalUnsignedTypes::class)
data class OrderRequestProduct(
    /** The part of the UUID enough to uniquely identify the product from a catalog. (since v1) */
    val uuidFragment: UByteArray,
    /** The stock of the product intended to be bought by the customer. (since v1) */
    var orderedStock: UInt,
    /** The product reference from the retailer inventory status. */
    val retailerStatusProduct: RetailerStatusProduct,
)

@OptIn(kotlin.ExperimentalUnsignedTypes::class)
data class OrderRequest(
    /** Array of products and their details. (since v1) */
    val products: List<OrderRequestProduct>,
) {
    companion object {
        fun loadFromRetailerStatus(retailerStatus: RetailerStatus): OrderRequest {
            return OrderRequest(
                products = retailerStatus.products.map { product ->
                    OrderRequestProduct(
                        uuidFragment = product.uuidFragment,
                        orderedStock = 0u,
                        retailerStatusProduct = product,
                    )
                }
            )
        }
    }
}
