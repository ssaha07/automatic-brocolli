#!/usr/bin/env python3

import json
import wx

from catalog import Catalog
from datamatrix import status_data_to_paged_matrices
from inventory import Inventory
from product import Product

class Window(wx.Frame):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)

        self.root_panel = wx.Panel(self)

        vbox = wx.BoxSizer(wx.VERTICAL)
        self.root_panel.SetSizer(vbox)

        hbox = wx.BoxSizer(wx.HORIZONTAL)
        vbox.Add(hbox, 1, wx.ALL | wx.ALIGN_CENTER, 5)

        self.innerPanel = wx.Panel(self.root_panel, -1, style=wx.ALIGN_CENTER)
        hbox.Add(self.innerPanel, 0, wx.ALL | wx.ALIGN_CENTER)

        self.innerBox = wx.BoxSizer(wx.VERTICAL)
        self.innerPanel.SetSizer(self.innerBox)

        # QR stuff
        catalog = Catalog(json.load(open("../playground/spec/products.json")))
        inventory = Inventory(catalog)

        self.qr_pages = status_data_to_paged_matrices(inventory.to_data())
        self.qr_page_index = 0
        qr_image = wx.Image(*self.qr_pages[self.qr_page_index])
        qr_bitmap = qr_image.ConvertToBitmap()

        self.qr_bitmap_widget = wx.StaticBitmap(self.innerPanel, bitmap=qr_bitmap)
        self.qr_bitmap_widget.SetScaleMode(wx.StaticBitmap.Scale_AspectFit)
        self.innerBox.Add(self.qr_bitmap_widget, 0, wx.CENTER)

        self.Bind(wx.EVT_TIMER, self.cycle_qr)

        self.timer = wx.Timer(self)
        self.timer.Start(200)

    def cycle_qr(self, timer_event: wx.TimerEvent):
        self.qr_bitmap_widget.Destroy()
        self.qr_page_index = (self.qr_page_index + 1) % len(self.qr_pages)

        qr_image = wx.Image(*self.qr_pages[self.qr_page_index])
        qr_bitmap = qr_image.ConvertToBitmap()

        self.qr_bitmap_widget = wx.StaticBitmap(self.innerPanel, bitmap=qr_bitmap)
        self.qr_bitmap_widget.SetScaleMode(wx.StaticBitmap.Scale_AspectFit)
        self.innerBox.Add(self.qr_bitmap_widget, 0, wx.CENTER)

if __name__ == "__main__":
    app = wx.App()
    window = Window(None, title="Hello World 2")

    window.Show()
    app.MainLoop()
