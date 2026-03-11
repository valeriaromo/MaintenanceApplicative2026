package com.gildedrose;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

    @Test
    void normalItemDecreasesQuality() {
        Item[] items = { new Item("Normal Item", 10, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(19, items[0].quality);
        assertEquals(9, items[0].sellIn);
    }

    @Test
    void qualityNeverNegative() {
        Item[] items = { new Item("Normal Item", 10, 0) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(0, items[0].quality);
    }

    @Test
    void agedBrieIncreasesQuality() {
        Item[] items = { new Item("Aged Brie", 10, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(21, items[0].quality);
    }

    @Test
    void agedBrieQualityMax50() {
        Item[] items = { new Item("Aged Brie", 10, 50) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(50, items[0].quality);
    }

    @Test
    void sulfurasNeverChanges() {
        Item[] items = { new Item("Sulfuras, Hand of Ragnaros", 0, 80) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(80, items[0].quality);
        assertEquals(0, items[0].sellIn);
    }

    @Test
    void backstagePassIncreaseQuality() {
        Item[] items = { new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(21, items[0].quality);
    }

    @Test
    void backstagePassIncreaseMoreWhen10Days() {
        Item[] items = { new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(22, items[0].quality);
    }

    @Test
    void backstagePassIncreaseMoreWhen5Days() {
        Item[] items = { new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(23, items[0].quality);
    }

    @Test
    void backstagePassDropToZeroAfterConcert() {
        Item[] items = { new Item("Backstage passes to a TAFKAL80ETC concert", 0, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(0, items[0].quality);
    }

    @Test
    void normalItemDegradesTwiceAfterSellDate() {
        Item[] items = { new Item("Normal Item", 0, 10) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals(8, items[0].quality);
    }
    @Test
    void backstagePassNeverAbove50() {
    Item[] items = { new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49) };
    GildedRose app = new GildedRose(items);
    app.updateQuality();
    assertEquals(50, items[0].quality);
    }

    @Test
    void agedBrieIncreasesTwiceAfterSellDate() {
    Item[] items = { new Item("Aged Brie", 0, 20) };
    GildedRose app = new GildedRose(items);
    app.updateQuality();
    assertEquals(22, items[0].quality);
    }

    @Test
    void normalItemExpiredNeverNegative() {
    Item[] items = { new Item("Normal Item", 0, 1) };
    GildedRose app = new GildedRose(items);
    app.updateQuality();
    assertEquals(0, items[0].quality);
    }
    
    @Test
    void itemToStringReturnsCorrectFormat() {
    Item item = new Item("Normal Item", 5, 10);
    assertEquals("Normal Item, 5, 10", item.toString());
    Item agedBrie = new Item("Aged Brie", 2, 20);
    assertEquals("Aged Brie, 2, 20", agedBrie.toString());
    Item sulfuras = new Item("Sulfuras, Hand of Ragnaros", 0, 80);
    assertEquals("Sulfuras, Hand of Ragnaros, 0, 80", sulfuras.toString());
}
}