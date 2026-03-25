package com.gildedrose;

class GildedRose {

    private UpdatableItem[] items;

    public GildedRose(Item[] items) {
        this.items = new UpdatableItem[items.length];

        for (int i = 0; i < items.length; i++) {
            this.items[i] = createItem(items[i]);
        }
    }

    public void updateQuality() {
        for (UpdatableItem item : items) {
            item.update();
        }
    }

    private UpdatableItem createItem(Item item) {
        switch (item.name) {
            case "Aged Brie": return new AgedBrieItem(item);
            case "Backstage passes to a TAFKAL80ETC concert": return new BackstagePassItem(item);
            case "Sulfuras, Hand of Ragnaros": return new SulfurasItem(item);
            case "Conjured": return new ConjuredItem(item);
            default: return new NormalItem(item);
        }
    }

    // ======= CLASE BASE =======
    abstract class UpdatableItem {
        protected Item item;

        public UpdatableItem(Item item) {
            this.item = item;
        }

        abstract void update();

        protected void increaseQuality() {
            if (item.quality < 50) item.quality++;
        }

        protected void decreaseQuality() {
            if (item.quality > 0) item.quality--;
        }

        protected void decreaseSellIn() {
            item.sellIn--;
        }
    }

    // ======= TIPOS DE ITEM =======
    class NormalItem extends UpdatableItem {
        public NormalItem(Item item) { super(item); }

        void update() {
            decreaseQuality();
            decreaseSellIn();
            if (item.sellIn < 0) decreaseQuality();
        }
    }

    class AgedBrieItem extends UpdatableItem {
        public AgedBrieItem(Item item) { super(item); }

        void update() {
            increaseQuality();
            decreaseSellIn();
            if (item.sellIn < 0) increaseQuality();
        }
    }

    class BackstagePassItem extends UpdatableItem {
        public BackstagePassItem(Item item) { super(item); }

        void update() {
            increaseQuality();
            if (item.sellIn < 11) increaseQuality();
            if (item.sellIn < 6) increaseQuality();
            decreaseSellIn();
            if (item.sellIn < 0) item.quality = 0;
        }
    }

    class SulfurasItem extends UpdatableItem {
        public SulfurasItem(Item item) { super(item); }

        void update() {
            // no cambia nunca
        }
    }

    class ConjuredItem extends UpdatableItem {
        public ConjuredItem(Item item) { super(item); }

        void update() {
            decreaseQuality();
            decreaseQuality();
            decreaseSellIn();
            if (item.sellIn < 0) {
                decreaseQuality();
                decreaseQuality();
            }
        }
    }
}