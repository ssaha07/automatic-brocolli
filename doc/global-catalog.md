# Global Catalog

The **Global Catalog** contains the list of all items which can be sold in any
retailer shop. This list is centralized and is the same for all.

## Purpose

Each product has some metadata attached to it, for example the name of the
product, its MRP (maximum retail price), etc. The **Global Catalog** stores
everything about all the products in a single place.

## Design

### Understanding a "Product"

To understand the design, the product needs to be understood first. Broadly
speaking products can be of two types:

- Products which are packaged (or similar) where only whole units of the
product can be sold.

- Products which are sold without packaging, thus are sold based on some
physical measurement (weight, volume, etc.)

The descriptor of product must be able to discern between the two. Properties
will be different for both; for instance the packaged products will only ever
be bought in positive numbers, but non-packaged products can be bought in
floating-point numbers.

However, how much stock is bought in an order isn't significant in this design,
but there should be some way to identify. The approach adopted here is to
assign a unit property to the product. If it has a unit, then said physical
measurement is used to sell the product.

### Schema

- `version` – The current version of the global catalog.

- `products` – The list of all the products *(array of objects)*

  - `uuid` – The unique identifier. As with all UUIDs, this is a randomized
  128-bit number. *(string, in standardized UUID pattern)*

  - `added` – The database version it was added in. This is used for collision
  resolution. *(number, greater than 0)* <!-- TODO: document collision resolution -->

  - `name` – The name of the product. *(string)*

  - `brand` – The name of the brand. *(string)*

  - `mrp` – The maximum retail price of the product. *(integer, greater than 0,
  or 0 if not applicable)*

  - `unit` – What unit the product is measured in. It should store the least
  significant SI unit of the quantity used to measure it. *(string, empty
  string if not applicable)*

All fields are compulsory.

Example (JSON):

```json
{
  "version": 1,
  "products": [
    {
      "uuid": "2d3268b2-a639-8813-8c88-a9919a9b0d33",
      "added": 1,
      "name": "Onion",
      "brand": "",
      "mrp": 0,
      "unit": "g"
    },
    {
      "uuid": "0336b16b-f4dc-4c5f-a16d-78846a2d422a",
      "added": 1,
      "name": "Gold Full Cream Milk (500 mL)",
      "brand": "Amul",
      "mrp": 34,
      "unit": ""
    }
  ]
}
```
