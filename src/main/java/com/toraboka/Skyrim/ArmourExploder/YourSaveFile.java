/* To change this template, choose Tools | Templates and open the template in
 * the editor. */
package com.toraboka.Skyrim.ArmourExploder;

import java.util.ArrayList;

import skyproc.SkyProcSave;

/**
 * @author Justin Swanson
 */
public class YourSaveFile extends SkyProcSave {

    @Override
    protected void initSettings() {
        Add(Settings.ARMOUR_SET, new ArrayList<String>(), false);
    }

    @Override
    protected void initHelp() {
        helpInfo.put(Settings.ARMOUR_SET, "Those armours to process.");
    }

    public enum Settings {
        ARMOUR_SET, OTHER_SETTINGS;
    }
}
