import math
import qrcode

def status_data_to_paged_matrices(data: bytes) -> list[tuple[int, int, bytes]]:
    # Data is divided, and a header is added to it.
    # - The first byte stores the meta info:
    #   - bit 7 indicates that the page is the final page in sequence.
    #   - bits 6:3 are reserved.
    #   - bits 2:0 indicates the number of bytes required to store the page
    #     number, minus 1.
    # - The next byte(s) store the variable-width page number.

    # Reference: https://www.qrcode.com/en/about/version.html
    QR_VERSION = 25
    QR_MAX_BYTES = 1273

    out = []
    data_byte_counter = 0
    page_number = 0
    while data_byte_counter < len(data):
        meta = 0

        page_number_bytes = max(math.ceil(math.log2(page_number + 1) / 8), 1)
        assert page_number_bytes <= 8
        meta |= page_number_bytes - 1

        header_len = 1 + page_number_bytes
        data_len = QR_MAX_BYTES - header_len

        page_data = data[data_byte_counter : data_byte_counter + data_len]

        data_byte_counter += data_len
        if data_byte_counter >= len(data):
            meta |= 1 << 7  # last page

        qr = qrcode.QRCode(QR_VERSION, qrcode.ERROR_CORRECT_L)
        qr.add_data(
            meta.to_bytes(1)
            + page_number.to_bytes(page_number_bytes, "little")
            + page_data
        )

        encoded = qr.make_image().get_image().convert("RGB")
        encoded = encoded.resize((encoded.width // 2, encoded.height // 2))
        out.append((encoded.width, encoded.height, encoded.tobytes()))

        page_number += 1

    return out
