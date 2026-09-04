from typing import Any, Iterable, Self

from functools import reduce
import qrcode
import random

from product import Product

# The catalog is to be provided by the public schema published by the government authority.
# Optionally, can be served by a REST API call too?
class Catalog:
    def __init__(self, init: dict[str, Any]):
        self.version = int(init["version"])
        self.products = list(map(lambda x: Product(x), init["products"]))[:200]

        assert 0 < self.version < (1 << 16)  # version is a unsigned 16-bit (non-zero) number
