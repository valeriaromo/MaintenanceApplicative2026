package com.gildedrose;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

    @Test
    void normalItemDecreasesQualityAndSellIn() {
        Item[] items = { new Item("Normal Item", 10, 20) };
        GildedRose app = new GildedRose(items);

        app.updateQuality();

        assertEquals(19, items[0].quality);
        assertEquals(9, items[0].sellIn);
    }

    @Test
    void normalItemDegradesTwiceAfterSellDate() {
        Item[] items = { new Item("Normal Item", 0, 10) };
        GildedRose app = new GildedRose(items);

        app.updateQuality();

        assertEquals(8, items[0].quality);
    }

    @Test
    void qualityNeverNegative() {
        Item[] items = { new Item("Normal Item", 5, 0) };
        GildedRose app = new GildedRose(items);

        app.updateQuality();

        assertEquals(0, items[0].quality);
    }

    @Test
    void agedBrieIncreasesQuality() {
        Item[] items = { new Item("Aged Brie", 2, 20) };
        GildedRose app = new GildedRose(items);

        app.updateQuality();

        assertEquals(21, items[0].quality);
    }

    @Test
    void agedBrieIncreasesTwiceAfterSellDate() {
        Item[] items = { new Item("Aged Brie", 0, 20) };
        GildedRose app = new GildedRose(items);

        app.updateQuality();

        assertEquals(22, items[0].quality);
    }

    @Test
    void agedBrieNeverAbove50() {
        Item[] items = { new Item("Aged Brie", 5, 50) };
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
    void backstagePassIncreasesQualityMoreThan10Days() {
        Item[] items = { new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20) };
        GildedRose app = new GildedRose(items);

        app.updateQuality();
        assertEquals(21, items[0].quality); // solo +1 porque >10 días
    }

    @Test
    void backstagePassIncreasesQuality10DaysOrLess() {
        Item[] items = { new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20) };
        GildedRose app = new GildedRose(items);

        app.updateQuality();
        assertEquals(22, items[0].quality); // +2 porque ≤10 días
    }

    @Test
    void backstagePassIncreasesQuality5DaysOrLess() {
        Item[] items = { new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20) };
        GildedRose app = new GildedRose(items);

        app.updateQuality();
        assertEquals(23, items[0].quality); // +3 porque ≤5 días
    }

    @Test
    void backstagePassDropsToZeroAfterConcert() {
        Item[] items = { new Item("Backstage passes to a TAFKAL80ETC concert", 0, 20) };
        GildedRose app = new GildedRose(items);

        app.updateQuality();
        assertEquals(0, items[0].quality);
    }

    @Test
    void backstagePassNeverAbove50() {
        Item[] items = { new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49) };
        GildedRose app = new GildedRose(items);

        app.updateQuality();
        assertEquals(50, items[0].quality);
    }

    @Test
    void conjuredItemDegradesTwiceAsFast() {
        Item[] items = { new Item("Conjured", 5, 10) };
        GildedRose app = new GildedRose(items);

        app.updateQuality();
        assertEquals(8, items[0].quality);
    }

    @Test
    void conjuredItemDegradesFourAfterSellDate() {
        Item[] items = { new Item("Conjured", 0, 10) };
        GildedRose app = new GildedRose(items);

        app.updateQuality();
        assertEquals(6, items[0].quality); 
    }

    @Test
    void conjuredQualityNeverNegative() {
        Item[] items = { new Item("Conjured", 0, 3) };
        GildedRose app = new GildedRose(items);

        app.updateQuality();
        assertEquals(0, items[0].quality);
    }

  
    @Test
    void itemToStringReturnsCorrectFormat() {
        Item item = new Item("Normal Item", 5, 10);
        assertEquals("Normal Item, 5, 10", item.toString());
    }
}