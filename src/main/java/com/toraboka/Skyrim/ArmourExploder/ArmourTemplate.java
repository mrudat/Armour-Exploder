package com.toraboka.Skyrim.ArmourExploder;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import skyproc.ARMO;
import skyproc.Mod;
import skyproc.Proxy;

public class ArmourTemplate implements Comparable<ArmourTemplate> {

    public static ConcurrentMap<Pair<BaseArmourTemplate, Mod>, ArmourTemplate> armourTemplateCache = new ConcurrentHashMap<Pair<BaseArmourTemplate, Mod>, ArmourTemplate>();

    public final BaseArmourTemplate baseArmourTemplate;

    public final ARMO armo;

    public int getArmourRating() {
        return armo.getArmorRating();
    }

    public String getMaterialType() {
        return baseArmourTemplate.material.displayName();
    }
    
    private ArmourTemplate(BaseArmourTemplate baseArmourTemplate, ARMO armo) {
        this.baseArmourTemplate = baseArmourTemplate;
        this.armo = armo;
    }

    public static Optional<ArmourTemplate> get(BaseArmourTemplate bat,
            Mod merger) {
        if (merger.getArmors().contains(bat.formid)) {
            return Optional.of(get2(bat, merger));
        } else {
            return Optional.empty();
        }
    }

    private static ArmourTemplate get2(BaseArmourTemplate bat, Mod merger) {
        ARMO foo = merger.getArmors().get(bat.formid);
        Mod bar = Proxy.getSrcMod(foo);

        return armourTemplateCache.computeIfAbsent(
                new ImmutablePair<BaseArmourTemplate, Mod>(bat, bar),
                p -> new ArmourTemplate(bat, foo));
    }

    public int compareTo(ArmourTemplate other) {
        return new CompareToBuilder().append(armo.getName(),
                other.armo.getName()).toComparison();
    }

}
