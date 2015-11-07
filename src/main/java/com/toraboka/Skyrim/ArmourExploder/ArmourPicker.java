/* To change this template, choose Tools | Templates and open the template in
 * the editor. */
package com.toraboka.Skyrim.ArmourExploder;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import lev.gui.LButton;
import lev.gui.LUserSetting;
import lev.gui.Setting;
import skyproc.BodyTemplate;
import skyproc.BodyTemplate.BodyTemplateType;
import skyproc.BodyTemplate.GeneralFlags;
import skyproc.FormID;
import skyproc.Mod;
import skyproc.Proxy;

/**
 * @author Justin Swanson
 */
@SuppressWarnings("serial")
public class ArmourPicker extends LUserSetting<List<String>> {

    LButton add;

    LButton remove;

    JComboBox<ArmourPickerEntry> addPicker;

    DefaultListModel<ArmourPickerEntry> model;

    JList<ArmourPickerEntry> addedList;

    JScrollPane scroll;

    /**
     * @param title
     * @param font
     * @param c
     */
    public ArmourPicker(String title, Font font, Color c) {
        super(title, font, c);
        titleLabel.addShadow();
        addPicker = new JComboBox<ArmourPickerEntry>();
        addPicker.setSize(addPicker.getPreferredSize());
        addPicker.setLocation(0, titleLabel.getHeight() + 5);
        add(addPicker);

        add = new LButton("Add FormID  /\\");
        add.setLocation(0, addPicker.getY() + addPicker.getHeight() + 5);
        add.addActionListener(arg0 -> Arrays.asList(addPicker.getSelectedObjects())
            .stream()
            .filter(o -> !model.contains(o))
            .forEach(o -> model.addElement((ArmourPickerEntry) o)));
        add(add);

        remove = new LButton("\\/  Remove FormID");
        remove.setLocation(add.getWidth() + 5, add.getY());
        remove.addActionListener(arg0 -> addedList.getSelectedValuesList()
            .stream()
            .forEach(o -> model.removeElement(o)));
        add(remove);

        model = new DefaultListModel<ArmourPickerEntry>();
        addedList = new JList<ArmourPickerEntry>(model);
        scroll = new JScrollPane(addedList);
        scroll.setLocation(0, add.getY() + add.getHeight() + 5);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll);

        setSize(300, 150);
    }

    /**
     * @param arg0
     * @param arg1
     */
    @Override
    final public void setSize(int arg0, int arg1) {
        super.setSize(arg0, arg1);
        // titleLabel.setLocation(arg0 / 2 - titleLabel.getWidth() / 2,
        // titleLabel.getY());
        addPicker.setSize(arg0, addPicker.getHeight());
        add.setSize((addPicker.getWidth() - 6) / 2, add.getHeight());
        remove.setSize(addPicker.getWidth() - add.getWidth() - 6, remove.getHeight());
        remove.setLocation(add.getX() + add.getWidth() + 5, remove.getY());
        scroll.setSize(arg0, arg1 - add.getY() - add.getHeight() - 5);
    }

    @Override
    protected void addUpdateHandlers() {
        add.addActionListener(new UpdateHandler());
        remove.addActionListener(new UpdateHandler());
    }

    @Override
    public boolean revertTo(Map<Enum, Setting> m) {
        if (isTied()) {
            List<String> list = m.get(saveTie)
                .getStrings();
            model.removeAllElements();
            // FIXME this used to be model.addElement(list);
            for (String e : list) {
                model.addElement(ArmourPickerEntry.get(new FormID(e)));
            }
        }
        return true;
    }

    @Override
    public List<String> getValue() {
        ArrayList<String> out = new ArrayList<String>(addedList.getModel()
            .getSize());
        for (int i = 0; i < addedList.getModel()
            .getSize(); i++) {
            out.add(addedList.getModel()
                .getElementAt(i)
                .getFormID()
                .getFormStr());
        }
        return out;
    }

    @Override
    public void highlightChanged() {
    }

    @Override
    public void clearHighlight() {
    }

    /**
     * @param hoverListener
     */
    @Override
    public void addHelpHandler(boolean hoverListener) {
        FocusListener f = new HelpFocusHandler();
        addPicker.addFocusListener(f);
        addedList.addFocusListener(f);
        if (hoverListener) {
            MouseListener l = new HelpMouseHandler();
            addPicker.addMouseListener(l);
            addedList.addMouseListener(l);
            add.addMouseListener(l);
            remove.addMouseListener(l);
        }
    }

    /**
     * @param ids
     */
    public void load(ArrayList<FormID> ids) {
        for (FormID id : ids) {
            addForm(id);
        }
    }

    /**
     * @param ids
     */
    public void load(FormID[] ids) {
        for (FormID id : ids) {
            addForm(id);
        }
    }

    /**
     * @param id
     */
    public void addForm(FormID id) {
        // FIXME this used to be model.addElement(add);
        model.addElement(ArmourPickerEntry.get(id));
    }

    /**
     * @param ids
     */
    public void load(String[][] ids) {
        ArrayList<FormID> out = new ArrayList<FormID>(ids.length);
        for (String[] id : ids) {
            out.add(new FormID(id[0], id[1]));
        }
        load(out);
    }

    /**
     * @return
     */
    public ArrayList<FormID> getPickedIDs() {
        ArrayList<FormID> out = new ArrayList<FormID>(model.getSize());
        for (int i = 0; i < model.getSize(); i++) {
            out.add(model.getElementAt(i)
                .getFormID());
        }
        return out;
    }

    public void setMerger(Mod merger) {
        ArmourPickerEntry.setMerger(merger);

        merger.getArmors()
            .getRecords()
            .stream()
            .filter(a -> a.getTemplate()
                .equals(FormID.NULL))
            .filter(a -> !a.getName()
                .isEmpty())
            .filter(a -> !a.getName()
                .equals("<NO TEXT>"))
            .filter(a -> {
                BodyTemplate bt = a.getBodyTemplate();
                if (Proxy.getBodyTemplateType(bt) == BodyTemplateType.Biped) {
                    return true;
                }
                return !bt.get(GeneralFlags.NonPlayable);
            })
            .filter(a -> Piece.getPiece(a)
                .isPresent())
            .map(a -> ArmourPickerEntry.get(a.getForm()))
            .sorted()
            .forEach(a -> addPicker.addItem(a));
    }

}
