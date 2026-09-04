from typing import Any, Iterable, Self

import json
import qrcode

from catalog import Catalog
from inventory import Inventory
from product import Product

catalog = Catalog(json.load(open("./spec/products.json")))
inventory = Inventory(catalog)

qr = qrcode.QRCode(error_correction=qrcode.ERROR_CORRECT_L)
qr_data = inventory.generate_qr_data()

qr.add_data(qr_data)
qr.print_ascii()
