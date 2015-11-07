package com.toraboka.Skyrim.ArmourExploder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.ImmutablePair;

import skyproc.ARMO;
import skyproc.FormID;

import com.google.common.collect.Sets;

enum Piece {
    Armour(new String[] {
        "06C0EC",
        "06bbe8"
    }),
    Boots("06C0ED"),
    Gauntlets("06C0EF"),
    Helmet("06C0EE"),
    Shield("0965B2");

    FormID[] armourTypeKeyword;

    Piece(String armourTypeKeywordId) {
        this(new String[] { armourTypeKeywordId
        });
    }

    Piece(String[] armourTypeKeywordIdList) {
        List<FormID> temp = new ArrayList<FormID>(armourTypeKeywordIdList.length);
        for (String armourTypeKeywordId : armourTypeKeywordIdList) {
            temp.add(new FormID(armourTypeKeywordId, "Skyrim.esm"));
        }
        armourTypeKeyword = temp.toArray(new FormID[] {});
    }

    private static Map<FormID, Piece> buildMap() {
        Stream<ImmutablePair<FormID, Piece>> foo = Arrays.asList(values())
            .stream()
            .flatMap(i -> Arrays.asList(i.armourTypeKeyword)
                .stream()
                .map(j -> new ImmutablePair<FormID, Piece>(j, i)));
        return foo.collect(Collectors.toMap(i -> i.left, i -> i.right));
    }

    static final EnumSet<Piece> set = Sets.newEnumSet(Arrays.asList(values()), Piece.class);

    static final Map<FormID, Piece> armourTypeKeywordMap = buildMap();

    static final Set<FormID> armourTypeKeywordSet = armourTypeKeywordMap.keySet();

    public static Piece get(FormID formID) {
        return armourTypeKeywordMap.get(formID);
    }

    public static Optional<Piece> getPiece(ARMO a) {
        return a.getKeywordSet()
            .getKeywordRefs()
            .stream()
            .filter(k -> armourTypeKeywordSet.contains(k))
            .map(k -> armourTypeKeywordMap.get(k))
            .findFirst();
    }

}