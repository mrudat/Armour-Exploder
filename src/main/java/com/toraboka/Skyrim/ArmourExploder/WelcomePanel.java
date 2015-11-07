/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toraboka.Skyrim.ArmourExploder;

import lev.gui.LTextPane;
import skyproc.gui.SPMainMenuPanel;
import skyproc.gui.SPSettingPanel;

/**
 *
 * @author Justin Swanson
 */
public class WelcomePanel extends SPSettingPanel {

    private static final long serialVersionUID = 1394067200797849449L;
    
    LTextPane introText;

    public WelcomePanel(SPMainMenuPanel parent_) {
	super(parent_, ArmourExploder.myPatchName, ArmourExploder.headerColor);
    }

    @Override
    protected void initialize() {
	super.initialize();

	introText = new LTextPane(settingsPanel.getWidth() - 40, 400, ArmourExploder.settingsColor);
	introText.setText(ArmourExploder.welcomeText);
	introText.setEditable(false);
	introText.setFont(ArmourExploder.settingsFont);
	introText.setCentered();
	setPlacement(introText);
	Add(introText);

	alignRight();
    }
}
