package io.github.chkrb.pqcompanion.data

import android.util.Log

@OptIn(kotlin.ExperimentalUnsignedTypes::class)
class PagedQrData {
    private var dataPages = mutableMapOf<ULong, UByteArray>() // FIXME: .size returns Int, concerning
    private var dataTotalPages = ULong.MAX_VALUE

    fun addDataPage(page: UByteArray) {
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
        for (i in 0u..<headerMetaPageBytes) {
            headerPageNumber =
                headerPageNumber or (page[i.toInt() + 1].toULong() shl (8 * i.toInt()))
        }

        if (headerPageNumber in dataPages.keys) return

        // We assume that the total number of pages is the maximum possible
        // pages. However if the last page as indicated by the header is
        // received, the total number of pages is changed.
        //
        // NOTE: This logic is very fragile, one may mix pages two different
        // POSes, which assembles garbage data.
        if (headerMetaLastPage && dataTotalPages == ULong.MAX_VALUE) {
            Log.d(this.javaClass.name, "this is the last page, correct expecting pages")
            dataTotalPages = headerPageNumber + 1u
        }

        dataPages[headerPageNumber] =
            page.filterIndexed { index, byte -> index >= headerMetaPageBytes.toInt() + 1 }
                .toUByteArray()

        Log.d(
            this.javaClass.name,
            "recv page ${headerPageNumber}; collected ${dataPages.size}, expecting $dataTotalPages}"
        )
    }

    fun assembleDataAsRetailerStatus(catalog: Catalog): RetailerStatus? {
        var accum = ubyteArrayOf()

        for (i in 0uL..<dataTotalPages) {
            if (dataPages[i] == null) return null
            accum += dataPages[i]!!
        }

        return RetailerStatus.loadFromPosData(accum, catalog)
    }
}
