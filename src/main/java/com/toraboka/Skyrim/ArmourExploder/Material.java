package com.toraboka.Skyrim.ArmourExploder;

enum Material {
    Cloth,
    Hide,
    Fur,
    Studded,
    Leather,
    Elven,
    Chitin,
    Scaled,
    GildedElven("Gilded Elven"),
    Glass,
    Stalhrim,
    Dragonscale,
    Iron,
    BandedIron("Banded Iron"),
    Steel,
    Bonemold,
    Dwarven,
    ImprovedBonemold("Improved Bonemold"),
    SteelPlate("Steel Plate"),
    HeavyChitin("Heavy Chitin"),
    Orcish,
    Ebony,
    NordicCarved("Nordic Carved"),
    Dragonplate,
    HeavyStalhrim("Heavy Stalhrim"),
    Daedric;

    private String displayName;

    Material() {
    }

    Material(String displayName) {
        this.displayName = displayName;
    }

    String displayName() {
        if (displayName == null) {
            return toString();
        }
        return displayName;
    }
}