package com.toraboka.Skyrim.ArmourExploder;

import static com.toraboka.Skyrim.ArmourExploder.Material.BandedIron;
import static com.toraboka.Skyrim.ArmourExploder.Material.Bonemold;
import static com.toraboka.Skyrim.ArmourExploder.Material.Chitin;
import static com.toraboka.Skyrim.ArmourExploder.Material.Daedric;
import static com.toraboka.Skyrim.ArmourExploder.Material.Dragonplate;
import static com.toraboka.Skyrim.ArmourExploder.Material.Dragonscale;
import static com.toraboka.Skyrim.ArmourExploder.Material.Dwarven;
import static com.toraboka.Skyrim.ArmourExploder.Material.Ebony;
import static com.toraboka.Skyrim.ArmourExploder.Material.Elven;
import static com.toraboka.Skyrim.ArmourExploder.Material.Fur;
import static com.toraboka.Skyrim.ArmourExploder.Material.GildedElven;
import static com.toraboka.Skyrim.ArmourExploder.Material.Glass;
import static com.toraboka.Skyrim.ArmourExploder.Material.HeavyChitin;
import static com.toraboka.Skyrim.ArmourExploder.Material.HeavyStalhrim;
import static com.toraboka.Skyrim.ArmourExploder.Material.Hide;
import static com.toraboka.Skyrim.ArmourExploder.Material.ImprovedBonemold;
import static com.toraboka.Skyrim.ArmourExploder.Material.Iron;
import static com.toraboka.Skyrim.ArmourExploder.Material.Leather;
import static com.toraboka.Skyrim.ArmourExploder.Material.NordicCarved;
import static com.toraboka.Skyrim.ArmourExploder.Material.Orcish;
import static com.toraboka.Skyrim.ArmourExploder.Material.Scaled;
import static com.toraboka.Skyrim.ArmourExploder.Material.Stalhrim;
import static com.toraboka.Skyrim.ArmourExploder.Material.Steel;
import static com.toraboka.Skyrim.ArmourExploder.Material.SteelPlate;
import static com.toraboka.Skyrim.ArmourExploder.Material.Studded;
import static com.toraboka.Skyrim.ArmourExploder.Material.Cloth;
import static com.toraboka.Skyrim.ArmourExploder.Piece.Armour;
import static com.toraboka.Skyrim.ArmourExploder.Piece.Boots;
import static com.toraboka.Skyrim.ArmourExploder.Piece.Gauntlets;
import static com.toraboka.Skyrim.ArmourExploder.Piece.Helmet;
import static com.toraboka.Skyrim.ArmourExploder.Piece.Shield;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.tuple.ImmutablePair;

import skyproc.Mod;

import com.google.common.collect.ImmutableList;

public class Template {

    private static final ImmutableList<BaseArmourTemplate> baseTemplate = makeTemplateArmour();

    private static void registerArmour(List<BaseArmourTemplate> ta, Piece p, Material m, String formid) {
        registerArmour(ta, p, m, formid, "Skyrim.esm");
    }

    private static void registerArmour(List<BaseArmourTemplate> ta, Piece p, Material m, String formid, String mod) {
        ta.add(new BaseArmourTemplate(p, m, formid, mod));
    }

    private static ImmutableList<BaseArmourTemplate> makeTemplateArmour() {
        ArrayList<BaseArmourTemplate> ta = new ArrayList<BaseArmourTemplate>(200);

        // 'Cloth' Armour
        registerArmour(ta, Boots, Cloth, "065B9B");
        registerArmour(ta, Gauntlets, Cloth, "065B9D");
        registerArmour(ta, Helmet, Cloth, "065B99");
        registerArmour(ta, Armour, Cloth, "065B94");
        
        registerArmour(ta, Armour, Hide, "013911");
        registerArmour(ta, Armour, Fur, "06f393");
        registerArmour(ta, Armour, Studded, "01b3a2");
        registerArmour(ta, Armour, Leather, "03619e");
        registerArmour(ta, Armour, Elven, "0896a3");
        registerArmour(ta, Armour, Chitin, "01cd87", "Dragonborn.esm");
        registerArmour(ta, Armour, Scaled, "01b3a3");
        registerArmour(ta, Armour, GildedElven, "01392a");
        registerArmour(ta, Armour, Glass, "013939");
        registerArmour(ta, Armour, Stalhrim, "01cda2", "Dragonborn.esm");
        registerArmour(ta, Armour, Dragonscale, "01393e");

        registerArmour(ta, Armour, Iron, "012e49");
        registerArmour(ta, Armour, BandedIron, "013948");
        registerArmour(ta, Armour, Steel, "013952");
        registerArmour(ta, Armour, Bonemold, "01cd93", "Dragonborn.esm");
        registerArmour(ta, Armour, Bonemold, "037564", "Dragonborn.esm");
        registerArmour(ta, Armour, Bonemold, "037563", "Dragonborn.esm");
        registerArmour(ta, Armour, Dwarven, "01394d");
        registerArmour(ta, Armour, ImprovedBonemold, "03ab26", "Dragonborn.esm");
        registerArmour(ta, Armour, SteelPlate, "01395c");
        registerArmour(ta, Armour, HeavyChitin, "01cd8a", "Dragonborn.esm");
        registerArmour(ta, Armour, Orcish, "013957");
        registerArmour(ta, Armour, Ebony, "013961");
        registerArmour(ta, Armour, NordicCarved, "01cd97", "Dragonborn.esm");
        registerArmour(ta, Armour, Dragonplate, "013966");
        registerArmour(ta, Armour, HeavyStalhrim, "01cd9f", "Dragonborn.esm");
        registerArmour(ta, Armour, Daedric, "01396b");

        registerArmour(ta, Boots, Hide, "013910");
        registerArmour(ta, Boots, Fur, "06f398");
        registerArmour(ta, Boots, Leather, "013920");
        registerArmour(ta, Boots, Elven, "01391a");
        registerArmour(ta, Boots, Chitin, "01cd86", "Dragonborn.esm");
        registerArmour(ta, Boots, Scaled, "01b39f");
        registerArmour(ta, Boots, Glass, "013938");
        registerArmour(ta, Boots, Stalhrim, "01cd7e", "Dragonborn.esm");
        registerArmour(ta, Boots, Dragonscale, "01393d");

        registerArmour(ta, Boots, Iron, "012e4b");
        registerArmour(ta, Boots, Steel, "013951");
        registerArmour(ta, Boots, Bonemold, "01cd92", "Dragonborn.esm");
        registerArmour(ta, Boots, ImprovedBonemold, "03ab25", "Dragonborn.esm");
        registerArmour(ta, Boots, Dwarven, "01394c");
        registerArmour(ta, Boots, SteelPlate, "01395b");
        registerArmour(ta, Boots, Chitin, "01cd82");
        registerArmour(ta, Boots, Orcish, "013956");
        registerArmour(ta, Boots, NordicCarved, "01cd96", "Dragonborn.esm");
        registerArmour(ta, Boots, Ebony, "013960");
        registerArmour(ta, Boots, Dragonplate, "013965");
        registerArmour(ta, Boots, Stalhrim, "01cd9e", "Dragonborn.esm");
        registerArmour(ta, Boots, Daedric, "01396a");

        registerArmour(ta, Gauntlets, Hide, "013912");
        registerArmour(ta, Gauntlets, Fur, "06f39b");
        registerArmour(ta, Gauntlets, Leather, "013921");
        registerArmour(ta, Gauntlets, Elven, "01391c");
        registerArmour(ta, Gauntlets, Chitin, "01cd88", "Dragonborn.esm");
        registerArmour(ta, Gauntlets, Scaled, "01b3a0");
        registerArmour(ta, Gauntlets, Glass, "01393a");
        registerArmour(ta, Gauntlets, Stalhrim, "01cda5", "Dragonborn.esm");
        registerArmour(ta, Gauntlets, Dragonscale, "01393f");

        registerArmour(ta, Gauntlets, Iron, "012e46");
        registerArmour(ta, Gauntlets, Steel, "0f6f23");
        registerArmour(ta, Gauntlets, Steel, "013953");
        registerArmour(ta, Gauntlets, Bonemold, "01cd94", "Dragonborn.esm");
        registerArmour(ta, Gauntlets, ImprovedBonemold, "03ab22", "Dragonborn.esm");
        registerArmour(ta, Gauntlets, Dwarven, "01394e");
        registerArmour(ta, Gauntlets, SteelPlate, "01395d");
        registerArmour(ta, Gauntlets, Chitin, "01cd8b", "Dragonborn.esm");
        registerArmour(ta, Gauntlets, Orcish, "013958");
        registerArmour(ta, Gauntlets, NordicCarved, "01cd98", "Dragonborn.esm");
        registerArmour(ta, Gauntlets, Ebony, "013962");
        registerArmour(ta, Gauntlets, Dragonplate, "013967");
        registerArmour(ta, Gauntlets, Stalhrim, "01cda0", "Dragonborn.esm");
        registerArmour(ta, Gauntlets, Daedric, "01396c");

        registerArmour(ta, Helmet, Hide, "013913");
        registerArmour(ta, Helmet, Fur, "06f39e");
        registerArmour(ta, Helmet, Leather, "013922");
        registerArmour(ta, Helmet, Elven, "01391d");
        registerArmour(ta, Helmet, Chitin, "01cd89", "Dragonborn.esm");
        registerArmour(ta, Helmet, Scaled, "01b3a1");
        registerArmour(ta, Helmet, Glass, "01393b");
        registerArmour(ta, Helmet, Stalhrim, "01cda3", "Dragonborn.esm");
        registerArmour(ta, Helmet, Dragonscale, "013940");

        registerArmour(ta, Helmet, Iron, "012e4d");
        registerArmour(ta, Helmet, Steel, "013954");
        registerArmour(ta, Helmet, Steel, "0f6f24");
        registerArmour(ta, Helmet, Bonemold, "01cd95", "Dragonborn.esm");
        registerArmour(ta, Helmet, ImprovedBonemold, "03ab23", "Dragonborn.esm");
        registerArmour(ta, Helmet, Dwarven, "01394f");
        registerArmour(ta, Helmet, Chitin, "01cd8c", "Dragonborn.esm");
        registerArmour(ta, Helmet, SteelPlate, "01395e");
        registerArmour(ta, Helmet, Orcish, "013959");
        registerArmour(ta, Helmet, NordicCarved, "01cd99", "Dragonborn.esm");
        registerArmour(ta, Helmet, Ebony, "013963");
        registerArmour(ta, Helmet, Chitin, "012e8a", "Dragonborn.esm"); // Shellbug
        registerArmour(ta, Helmet, Dragonplate, "013969");
        registerArmour(ta, Helmet, Stalhrim, "01cda1", "Dragonborn.esm");
        registerArmour(ta, Helmet, Daedric, "01396d");

        registerArmour(ta, Shield, Hide, "013914");
        registerArmour(ta, Shield, Elven, "01391e");
        registerArmour(ta, Shield, Chitin, "026235", "Dragonborn.esm");
        registerArmour(ta, Shield, Glass, "01393c");
        registerArmour(ta, Shield, Dragonscale, "013941");
        registerArmour(ta, Shield, Stalhrim, "026237", "Dragonborn.esm");

        registerArmour(ta, Shield, Iron, "012eb6");
        registerArmour(ta, Shield, Bonemold, "026234", "Dragonborn.esm");
        registerArmour(ta, Shield, BandedIron, "01394b");
        registerArmour(ta, Shield, Steel, "013955");
        registerArmour(ta, Shield, ImprovedBonemold, "03ab24", "Dragonborn.esm");
        registerArmour(ta, Shield, Dwarven, "013950");
        registerArmour(ta, Shield, NordicCarved, "026236", "Dragonborn.esm");
        registerArmour(ta, Shield, Orcish, "013946");
        registerArmour(ta, Shield, Ebony, "013964");
        registerArmour(ta, Shield, Dragonplate, "013968");
        registerArmour(ta, Shield, Daedric, "01396e");

        ta.trimToSize();

        return ImmutableList.copyOf(ta);
    }

    public final ImmutableList<ArmourTemplate> template;

    public final Map<Piece, List<ArmourTemplate>> templateMap2;

    private Template(Mod merger) {
        ParallelProgressBar pb = new ParallelProgressBar(baseTemplate.size(), "Template");
        
        template = ImmutableList.copyOf(baseTemplate.stream()
            .peek(a -> pb.increment())
            .map(bat -> ArmourTemplate.get(bat, merger))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(i -> new ImmutablePair<Integer, ArmourTemplate>(i.getArmourRating(), i))
            .sorted()
            .map(i -> i.right)
            .collect(toList()));
        
        templateMap2 = template.stream()
            .collect(groupingBy(e -> e.baseArmourTemplate.piece));
        
        pb.done();
    }

    private static final ConcurrentMap<Mod, Template> templateCache = new ConcurrentHashMap<Mod, Template>();

    public static Template get(Mod merger) {
        return templateCache.computeIfAbsent(merger, Template::new);
    }

    public List<ArmourTemplate> get2(Piece piece) {
        return templateMap2.get(piece);
    }
}
