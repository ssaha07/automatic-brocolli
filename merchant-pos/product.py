import ctypes
from typing import Any, Iterable, Self

class Product:
    def __init__(self, serialized: dict[str, Any]):
        self.uuid = int(serialized["uuid"].replace("-", ""), 16).to_bytes(16, "little")
        self.added = int(serialized["added"])
        self.removed = int(serialized["added"])
        self.name: str = serialized["name"]
        self.brand: str = serialized["brand"]
        self.mrp = int(serialized["mrp"])
        self.unit: str = serialized["unit"]

        assert self.added > 0  # non-negative version code
        assert self.removed >= 0  # non-negative version code or not applicable (0)
        assert self.mrp >= 0  # non-negative price or not applicable (0)

    def to_data(
        self,
        products: Iterable[Self],
        selling_price: int,
        stock: int,
        spec_version: int = 1,
    ) -> bytes:
        match spec_version:
            case 1:
                return self.to_data_v1(products, selling_price, stock)
            case _:
                raise ValueError(f"incorrect or unknown specification {spec_version}")

    def to_data_v1(
        self,
        products: Iterable[Self],
        selling_price: int,
        stock: int,
    ) -> bytes:
        # UUID: variable from 1 byte to 16 bytes, represented in LE.
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

        # Stock: variable from 1 byte to 4 bytes, represented in LE.
        assert stock > 0
        if stock < (1 << 8):
            stock_bytes_le = stock.to_bytes(1)
        elif stock < (1 << 16):
            stock_bytes_le = stock.to_bytes(2, "little")
        elif stock < (1 << 32):
            stock_bytes_le = stock.to_bytes(3, "little")
        elif stock < (1 << 64):
            stock_bytes_le = stock.to_bytes(4, "little")
        else:
            raise ValueError(f"quantity {stock} is too large to represent")

        # Selling price: variable from 1 byte to 4 bytes, represented in LE.
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

        # Header: 1 byte.
        header = 0
        # bits 3:0 represent number of bytes of the UUID, minus one.
        header |= unique_bytes - 1
        # bits 5:4 represent number of bytes taken up by the stock value,
        # minus 1.
        header |= (len(stock_bytes_le) - 1) << 4
        # bits 7:6 represent number of bytes taken up by the selling price
        # value, minus 1.
        header |= (len(selling_price_bytes_le) - 1) << 6

        assert header <= 0xff, "product data header must be precisely 1 byte"
        header_byte = header.to_bytes(1)

        return header_byte + uuid_bytes_le + selling_price_bytes_le + stock_bytes_le
