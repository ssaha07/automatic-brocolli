from enum import unique
from typing import Any, Iterable, Self

import ctypes
import json
import math
import qrcode

class Product:
    def __init__(self, init: dict[str, Any]):
        self.uuid: bytes = int(init["uuid"].replace("-", ""), 16).to_bytes(16, "little")
        self.added: int = int(init["added"])
        self.removed: int = int(init["added"])
        self.name: str = init["name"]
        self.brand: str = init["brand"]
        self.mrp: int = int(init["mrp"])
        self.unit: str = init["unit"]

        assert self.added > 0  # non-negative version code
        assert self.removed >= 0  # non-negative version code or not applicable (0)
        assert self.mrp >= 0  # non-negative price or not applicable (0)

    def generate_qr_data_v1(
        self,
        products: Iterable[Self],
        selling_price: int,
        quantity: int,
    ) -> bytes:
        header = 0

        # 2.4. Selling price: variable from 1-byte to 4-byte, represented in LE.
        assert selling_price > 0
        if selling_price < (1 << 8):
            selling_price_bytes_le = selling_price.to_bytes(1)
        elif selling_price < (1 << 16):
            selling_price_bytes_le = selling_price.to_bytes(2, "little")
        elif selling_price < (1 << 32):
            selling_price_bytes_le = selling_price.to_bytes(3, "little")
        elif selling_price < (1 << 64):
            selling_price_bytes_le = selling_price.to_bytes(4, "little")
        else:
            raise ValueError(f"selling price {selling_price} is too large to represent")

        # 2.3. Quantity: variable from 1-byte to 4-byte, represented in LE.
        assert quantity > 0
        if quantity < (1 << 8):
            quantity_bytes_le = quantity.to_bytes(1)
        elif quantity < (1 << 16):
            quantity_bytes_le = quantity.to_bytes(2, "little")
        elif quantity < (1 << 32):
            quantity_bytes_le = quantity.to_bytes(3, "little")
        elif quantity < (1 << 64):
            quantity_bytes_le = quantity.to_bytes(4, "little")
        else:
            raise ValueError(f"quantity {quantity} is too large to represent")

        # 2.2. UUID, variable from 1-byte to 16-bytes, represented in LE.
        unique_bytes = 1
        for product in products:
            if product is self:
                continue

            unique_bytes_pass = 1
            assert len(self.uuid) == len(product.uuid)
            for i in range(0, len(self.uuid)):
                if self.uuid[i] ^ product.uuid[i]:
                    break

                unique_bytes_pass += 1

            unique_bytes = max(unique_bytes, unique_bytes_pass)

        assert 1 <= unique_bytes <= 16
        uuid_bytes_le = self.uuid[:unique_bytes]

        # 2.1. Header: 1-byte.
        # bits 3:0 represent number of bytes of the UUID, minus one.
        header |= unique_bytes - 1
        # bits 5:4 represent number of bytes taken up by the quantity value,
        # minus 1.
        header |= (len(quantity_bytes_le) - 1) << 4
        # bits 7:6 represent number of bytes taken up by the selling price
        # value, minus 1.
        header |= (len(selling_price_bytes_le) - 1) << 6

        assert header <= 0xff, "product data header must be precisely 1 byte"
        header_byte = header.to_bytes(1)

        return header_byte + uuid_bytes_le + selling_price_bytes_le + quantity_bytes_le

    def generate_qr_data(
        self,
        products: Iterable[Self],
        selling_price: int,
        quantity: int,
        spec_version: int = 1,
    ) -> bytes:
        match spec_version:
            case 1:
                return self.generate_qr_data_v1(products, selling_price, quantity)
            case _:
                raise ValueError(f"incorrect or unknown specification {spec_version}")
