package com.toraboka.Skyrim.ArmourExploder;

import skyproc.FormID;

public class BaseArmourTemplate {

    public final FormID formid;

    public final Material material;

    public final Piece piece;

    public BaseArmourTemplate(Piece piece, Material material, FormID formid) {
        this.formid = formid;
        this.material = material;
        this.piece = piece;
    }

    public BaseArmourTemplate(Piece piece, Material material, String formid) {
        this(piece, material, new FormID(formid, "Skyrim.esp"));
    }

    public BaseArmourTemplate(Piece piece, Material material, String formid,
            String mod) {
        this(piece, material, new FormID(formid, mod));
    }

}
