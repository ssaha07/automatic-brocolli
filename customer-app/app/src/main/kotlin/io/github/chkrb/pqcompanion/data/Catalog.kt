package io.github.chkrb.pqcompanion.data

import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.InputStream

@Serializable
data class CatalogProduct(
    /** The random unique identifier assigned to the product. (since v1) */
    val uuid: String,
    /** The catalog version in which this product was added. (since v1) */
    val added: UInt,
    /** The marketing name of the product. (since v1) */
    val name: String,
    /** The name of the product brand. (since v1) */
    val brand: String,
    /** The MRP (maximum retail price) of the product. (since v1) */
    val mrp: UInt,
    /** The unit used to measure the stock of the product. (since v1) */
    val unit: String,
)

@OptIn(kotlin.ExperimentalUnsignedTypes::class)
@Serializable
data class Catalog(
    /** The version of the catalog as released by concerned authority. (since v1) */
    val version: UInt,
    /** The list of all products enlisted for selling across stores. (since v1) */
    val products: List<CatalogProduct>,
) {
    companion object {
        fun loadFromJsonStream(jsonStream: InputStream): Catalog {
            @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
            val deserialized = Json.decodeFromStream<Catalog>(jsonStream)

            return deserialized.copy(products = deserialized.products.sortedBy { it.uuid })
        }
    }

    fun searchProduct(uuidFragment: UByteArray, catalogVersion: UInt): CatalogProduct? {
        val productsFiltered = products.filter { it.added <= catalogVersion }

        var lo = 0
        var hi = productsFiltered.size - 1

        assert(uuidFragment.size <= 16)

        binarySearch@ while (lo <= hi) {
            val mid = lo + (hi - lo) / 2
            val uuidBytes = productsFiltered[mid].uuid
                .replace("-".toRegex(), "")
                .chunked(2)
                .map { it.hexToUByte() }

            for (i in 0..<uuidFragment.size) {
                if (uuidFragment[i] < uuidBytes[i]) {
                    hi = mid - 1
                    continue@binarySearch
                } else if (uuidFragment[i] > uuidBytes[i]) {
                    lo = mid + 1
                    continue@binarySearch
                }
            }

            return products[mid]
        }

        return null
    }
}
