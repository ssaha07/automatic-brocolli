from collections.abc import Generator
from typing import Any, Iterable, Self

from functools import reduce
import qrcode
import random

from catalog import Catalog
from product import Product

class Inventory:
    def __init__(self, catalog: Catalog):
        self.catalog = catalog

        # XXX: here, there's gonna be a database where we get metrics such as
        # remaining stock and stuff
        uuids = list(map(lambda x: x.uuid, self.catalog.products))

        self.selling_prices = dict(zip(uuids, random.sample(range(10, 10000), len(uuids))))
        self.quantities = dict(zip(uuids, random.sample(range(10, 10000), len(uuids))))

    def generate_qr_data(self, spec_version: int = 1) -> bytes:
        products_data_array = reduce(
            lambda accum, x: accum + x,
            map(
                lambda x: x.generate_qr_data(
                    self.catalog.products,
                    self.selling_prices[x.uuid],
                    self.quantities[x.uuid],
                    spec_version,
                ),
                self.catalog.products,
            ),
        )
        products_data_sentinel = b"\0\0\0\0"

        return (
            self.catalog.version.to_bytes(2, "little")
            + products_data_array
            + products_data_sentinel
        )
