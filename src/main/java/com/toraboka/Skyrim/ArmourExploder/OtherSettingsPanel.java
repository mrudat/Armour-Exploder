/* To change this template, choose Tools | Templates and open the template in
 * the editor. */
package com.toraboka.Skyrim.ArmourExploder;

import skyproc.Mod;
import skyproc.gui.SPMainMenuPanel;
import skyproc.gui.SPSettingPanel;
import skyproc.gui.SUMGUI;

/**
 * @author Justin Swanson
 */
@SuppressWarnings("serial")
public class OtherSettingsPanel extends SPSettingPanel {

    ArmourPicker armourSet;

    private ArmourExploder armourExploder;

    public OtherSettingsPanel(SPMainMenuPanel parent_, ArmourExploder armourExploder) {
        super(parent_, "Other Settings", ArmourExploder.headerColor);
        this.armourExploder = armourExploder;
    }

    @Override
    protected void initialize() {
        super.initialize();

        armourSet = new ArmourPicker("Armour Sets", ArmourExploder.settingsFont, ArmourExploder.settingsColor);
        armourSet.tie(YourSaveFile.Settings.ARMOUR_SET, ArmourExploder.save, SUMGUI.helpPanel, true);

        AddSetting(armourSet);

        alignRight();

        SUMGUI.startImport(() -> {
            Mod merger = armourExploder.getMerger();
            armourSet.setMerger(merger);
            setPlacement(armourSet);
        });
    }
}
