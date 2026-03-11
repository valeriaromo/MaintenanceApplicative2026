package com.gildedrose;

class GildedRose {

    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {

        for (Item item : items) {

            switch (item.name) {

                case "Aged Brie":
                    updateAgedBrie(item);
                    break;

                case "Backstage passes to a TAFKAL80ETC concert":
                    updateBackstagePass(item);
                    break;

                case "Sulfuras, Hand of Ragnaros":
                    updateSulfuras(item);
                    break;

                default:
                    updateNormalItem(item);
                    break;
            }
        }
    }

    private void updateNormalItem(Item item) {

        decreaseQuality(item);
        item.sellIn--;

        if (item.sellIn < 0) {
            decreaseQuality(item);
        }
    }

    private void updateAgedBrie(Item item) {

        increaseQuality(item);
        item.sellIn--;

        if (item.sellIn < 0) {
            increaseQuality(item);
        }
    }

    private void updateBackstagePass(Item item) {

        increaseQuality(item);

        if (item.sellIn < 11) {
            increaseQuality(item);
        }

        if (item.sellIn < 6) {
            increaseQuality(item);
        }

        item.sellIn--;

        if (item.sellIn < 0) {
            item.quality = 0;
        }
    }

    private void updateSulfuras(Item item) {
    }

    private void increaseQuality(Item item) {
        if (item.quality < 50) {
            item.quality++;
        }
    }

    private void decreaseQuality(Item item) {
        if (item.quality > 0) {
            item.quality--;
        }
    }
}