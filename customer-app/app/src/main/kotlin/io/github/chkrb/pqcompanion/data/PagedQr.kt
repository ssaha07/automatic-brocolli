package io.github.chkrb.pqcompanion.data

import android.util.Log

@OptIn(kotlin.ExperimentalUnsignedTypes::class)
class PagedQrData {
    // WARN: `dataPages` has a method `.size()` which reports how many elements
    // are in the map. However, the return type is Int. Therefore the
    // implementation cannot use this method to check how many elements are in
    // the map because there can be a maximum of 2^16 pages.
    //
    // The solution is to keep track of the number of elements externally.
    // However we require a data type which can store any value from 0 to 2^16.
    // We can instead sneakily fit this into a ULong instead, with some compromises.
    //
    // For starters, we keep track of one less than the number of elements
    // present, i.e. -1 to 2^16 - 1. The -1 value represents an empty map, thus
    // we can only reliably check that variable if `dataPages.isEmpty()` is `true`.
    private var dataPages = mutableMapOf<ULong, UByteArray>()
    private var numDataPagesMinusOne = 0uL
    private var lastDataPageNumber = ULong.MAX_VALUE

    fun addDataPageAndConstruct(page: UByteArray): UByteArray? {
        // Data is divided, and a header is added to it.
        // - The first byte stores the meta info:
        //   - bit 7 indicates that the page is the final page in sequence.
        //   - bits 6:3 are reserved.
        //   - bits 2:0 indicates the number of bytes required to store the page
        //     number, minus 1.
        // - The next byte(s) store the variable-width page number.

        val headerMeta = page[0].toUInt()
        val headerMetaLastPage = headerMeta shr 7 != 0u
        val headerMetaPageBytes = (headerMeta and 0b00000111u) + 1u

        var headerPageNumber = 0uL
        for (i in 0..<headerMetaPageBytes.toInt()) {
            headerPageNumber =
                headerPageNumber or (page[i + 1].toULong() shl (8 * i))
        }

        if (headerPageNumber !in dataPages.keys) {
            // We assume that the total number of pages is the maximum possible
            // pages. However if the last page as indicated by the header is
            // received, the total number of pages is changed.
            //
            // NOTE: This logic is very fragile, one may mix pages two different
            // POSes, which assembles garbage data.
            if (headerMetaLastPage && lastDataPageNumber == ULong.MAX_VALUE) {
                Log.d(this.javaClass.name, "this is the last page, correct expecting pages")
                lastDataPageNumber = headerPageNumber
            }

            val dataPagesNotEmpty = !dataPages.isEmpty()
            dataPages[headerPageNumber] =
                page.filterIndexed { index, byte -> index >= headerMetaPageBytes.toInt() + 1 }
                    .toUByteArray()

            if (dataPagesNotEmpty) numDataPagesMinusOne++

            Log.d(this.javaClass.name, "received page $headerPageNumber")
        }

        if (numDataPagesMinusOne == lastDataPageNumber) {
            var accum = ubyteArrayOf()

            for (i in 0uL..lastDataPageNumber) {
                if (dataPages[i] == null) return null
                accum += dataPages[i]!!
            }

            return accum
        }

        return null
    }
}
