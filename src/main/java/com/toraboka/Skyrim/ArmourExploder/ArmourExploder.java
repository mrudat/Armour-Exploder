package com.toraboka.Skyrim.ArmourExploder;

import static java.util.stream.Collectors.toList;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import lev.gui.LSaveFile;

import org.apache.commons.lang3.tuple.ImmutablePair;

import skyproc.ARMO;
import skyproc.AltTextures.AltTexture;
import skyproc.BodyTemplate;
import skyproc.BodyTemplate.BodyTemplateType;
import skyproc.BodyTemplate.GeneralFlags;
import skyproc.COBJ;
import skyproc.FormID;
import skyproc.GRUP;
import skyproc.GRUP_TYPE;
import skyproc.Mod;
import skyproc.ModListing;
import skyproc.NIF;
import skyproc.NIF.TextureSet;
import skyproc.Proxy;
import skyproc.SPGlobal;
import skyproc.SkyProcSave;
import skyproc.TXST;
import skyproc.exceptions.BadParameter;
import skyproc.genenums.FirstPersonFlags;
import skyproc.genenums.Gender;
import skyproc.gui.SPMainMenuPanel;
import skyproc.gui.SUM;
import skyproc.gui.SUMGUI;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.toraboka.Skyrim.ArmourExploder.YourSaveFile.Settings;

/**
 * @author Your Name Here
 */
public class ArmourExploder implements SUM {

    GRUP_TYPE[] importRequests = new GRUP_TYPE[] {
        GRUP_TYPE.ARMO,
        GRUP_TYPE.KYWD,
        GRUP_TYPE.COBJ,
        GRUP_TYPE.TXST,
        GRUP_TYPE.MISC
    };

    private Mod merger;

    private Mod patch;

    public static final String myPatchName = "Armour Exploder";

    public static final String authorName = "Martin Rudat";

    public static final String version = "0.0.1";

    public static final String welcomeText = "Duplicates an existing armour record with statistics taken from base armour records";

    public static final String descriptionToShowInSUM = "Duplicates an existing armour record with statistics taken from base armour records";

    public static final Color headerColor = new Color(66, 181, 184); // Teal

    public static final Color settingsColor = new Color(72, 179, 58); // Green

    public static final Font settingsFont = new Font("Serif", Font.BOLD, 15);

    public static final SkyProcSave save = new YourSaveFile();

    private Path dataPath;

    private Path meshesPath;

    private Path texturesPath;

    private Map<ARMO, List<COBJ>> getRecipesCache = new ConcurrentHashMap<ARMO, List<COBJ>>();

    private Template template;

    public static void main(String[] args) {
        try {
            SPGlobal.createGlobalLog();
            SUMGUI.open(new ArmourExploder(), args);
        } catch (final Exception e) {
            System.err.println(e.toString());
            SPGlobal.logException(e);
            JOptionPane.showMessageDialog(null, "There was an exception thrown during program execution: '" + e + "'  Check the debug logs or contact the author.");
            SPGlobal.closeDebug();
        }
    }

    @Override
    public String getName() {
        return myPatchName;
    }

    @Override
    public GRUP_TYPE[] dangerousRecordReport() {
        return new GRUP_TYPE[] { GRUP_TYPE.ARMO
        };
    }

    @Override
    public GRUP_TYPE[] importRequests() {
        return importRequests;
    }

    @Override
    public boolean importAtStart() {
        return false;
    }

    @Override
    public boolean hasStandardMenu() {
        return true;
    }

    @Override
    public SPMainMenuPanel getStandardMenu() {
        final SPMainMenuPanel settingsMenu = new SPMainMenuPanel(getHeaderColor());

        settingsMenu.setWelcomePanel(new WelcomePanel(settingsMenu));
        settingsMenu.addMenu(new OtherSettingsPanel(settingsMenu, this), false, save, Settings.OTHER_SETTINGS);

        return settingsMenu;
    }

    @Override
    public boolean hasCustomMenu() {
        return false;
    }

    @Override
    public JFrame openCustomMenu() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasLogo() {
        return false;
    }

    @Override
    public URL getLogo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasSave() {
        return true;
    }

    @Override
    public LSaveFile getSave() {
        return save;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public ModListing getListing() {
        return new ModListing(getName(), false);
    }

    @Override
    public Mod getExportPatch() {
        final Mod out = new Mod(getListing());
        out.setAuthor(authorName);
        return out;
    }

    @Override
    public Color getHeaderColor() {
        return headerColor;
    }

    @Override
    public boolean needsPatching() {
        return false;
    }

    @Override
    public void onStart() throws Exception {
    }

    @Override
    public void onExit(boolean patchWasGenerated) throws Exception {
    }

    @Override
    public ArrayList<ModListing> requiredMods() {
        return new ArrayList<>(0);
    }

    @Override
    public String description() {
        return descriptionToShowInSUM;
    }

    @Override
    public void runChangesToPatch() throws Exception {

        patch = SPGlobal.getGlobalPatch();

        getMerger();

        FileSystem fs = FileSystems.getDefault();

        dataPath = fs.getPath(SPGlobal.pathToData)
            .toAbsolutePath()
            .normalize();
        meshesPath = dataPath.resolve("meshes");
        texturesPath = dataPath.resolve("textures");

        template = Template.get(merger);

        // TODO pick armours to explode

        final HashSet<FormID> armoursToExplode_orig = Sets.newHashSet(new FormID[] {
            new FormID("013938", "Skyrim.esm") /* Glass Boots */,
            new FormID("013939", "Skyrim.esm") /* Glass Armour */,
            new FormID("01393a", "Skyrim.esm") /* Glass Gauntlets */,
            new FormID("01393b", "Skyrim.esm") /* Glass Helmet */,
            new FormID("01393c", "Skyrim.esm") /* Glass Shield */,
            new FormID("001573", "CraftableCrownHelmets.esp") /* Glass Crown */,
            new FormID("000d6d", "fox38 Armor.esp") /* FOX 38 Clothes D */,
        });

        final HashSet<FormID> armoursToExplode = save.getStrings(Settings.ARMOUR_SET)
            .stream()
            .map(s -> new FormID(s))
            .collect(Collectors.toCollection(HashSet<FormID>::new));

        GRUP<ARMO> armours = merger.getArmors();

        ParallelProgressBar.setProgressBar(SUMGUI.progress);

        ParallelProgressBar progress = new ParallelProgressBar(armoursToExplode.size(), "Armours to explode");

        armoursToExplode.stream()
            .peek(i -> progress.increment())
            .map(f -> armours.get(f))
            .filter(a -> a != null)
            .map(a -> new ImmutablePair<Optional<Piece>, ARMO>(Piece.getPiece(a), a))
            .filter(p -> p.left.isPresent())
            .forEach(p -> {
                Piece piece = p.left.get();

                ARMO armour_to_explode = p.right;

                List<ArmourTemplate> template_armours = template.get2(piece);

                List<ArmourTemplate> closest_matching_armour_list = template_armours.stream()
                    .map(a -> new ImmutablePair<Integer, ArmourTemplate>(Math.abs(a.getArmourRating() - armour_to_explode.getArmorRating()), a))
                    .sorted()
                    .map(i -> i.right)
                    .collect(toList());

                Map<Gender, ArrayList<AltTexture>> originalTextures = getOriginalTextures(armour_to_explode);

                ArmourTemplate closest_matching_base_armour = pickClosestMatchingArmour(armour_to_explode, closest_matching_armour_list);

                ParallelProgressBar template_progress = new ParallelProgressBar(template_armours.size(), "Exploding " + armour_to_explode.getName());

                /* create a new armour with the statistics of the base armour,
                 * and the appearance of the armour to explode. */
                template_armours.stream()
                    .peek(i -> template_progress.increment())
                    .filter(a -> a != closest_matching_base_armour)
                    .forEach(base_armour -> {
                        ARMO new_armour = armour_to_explode.copy(armour_to_explode.getEDID() + base_armour.armo.getForm()
                            .getFormStr());

                        setName(new_armour, base_armour);

                        copyStatistics(new_armour, base_armour);

                        copyAppearance(new_armour, armour_to_explode);

                        mangleTextures(new_armour, base_armour, originalTextures);
                    });

                template_progress.increment();
                if (armour_to_explode == closest_matching_base_armour.armo || armour_to_explode.equals(closest_matching_base_armour.armo)) {
                    /* as the armour to be exploded is the base armour, nothing
                     * needs to be done. */
                } else {
                    /* overwrite the statistics of the armour we're exploding
                     * with that of the closest vanilla armour */
                    setName(armour_to_explode, closest_matching_base_armour);

                    copyStatistics(armour_to_explode, closest_matching_base_armour);

                    mangleTextures(armour_to_explode, closest_matching_base_armour, originalTextures);

                    patch.addRecord(armour_to_explode);
                }
                template_progress.done();
            });
        progress.done();

    }

    public synchronized Mod getMerger() {
        if (merger == null) {
            merger = new Mod(getName() + "Merger", false);
            merger.addAsOverrides(SPGlobal.getDB());
        }
        return merger;
    }

    private ArmourTemplate pickClosestMatchingArmour(ARMO armour_to_explode, List<ArmourTemplate> closest_matching_armour_list) {
        for (ArmourTemplate cma : closest_matching_armour_list) {
            if (cma.armo == armour_to_explode) {
                return cma;
            }
        }
        return closest_matching_armour_list.get(0);
    }

    private void copyStatistics(ARMO target, ArmourTemplate source_template) {
        ARMO source = source_template.armo;

        BodyTemplate sourceBodyTemplate = source.getBodyTemplate();
        BodyTemplate targetBodyTemplate = target.getBodyTemplate();

        BodyTemplateType sourceBodyTemplateType = Proxy.getBodyTemplateType(sourceBodyTemplate);
        BodyTemplateType targetBodyTemplateType = Proxy.getBodyTemplateType(targetBodyTemplate);

        targetBodyTemplate.setArmorType(targetBodyTemplateType, sourceBodyTemplate.getArmorType(sourceBodyTemplateType));

        target.setEquipSlot(source.getEquipSet());
        target.setBashImpactData(source.getBashImpactData());

        // copy keywords.
        target.getKeywordSet()
            .clearKeywordRefs();
        source.getKeywordSet()
            .getKeywordRefs()
            .forEach(f -> target.getKeywordSet()
                .addKeywordRef(f));

        target.setArmorRating(source.getArmorRating());
        target.setWeight(source.getWeight());
        target.setValue(source.getValue());

        copyRecipes(target, source);
    }

    private void setName(ARMO target, ArmourTemplate source_template) {
        if (target.getName()
            .contains(source_template.getMaterialType())) {
            // the material type is already in the name...
            return;
        }
        target.setName(source_template.getMaterialType() + " " + target.getName());
    }

    private void copyAppearance(ARMO target, ARMO source) {
        BodyTemplate sourceBodyTemplate = source.getBodyTemplate();
        BodyTemplate targetBodyTemplate = target.getBodyTemplate();

        BodyTemplateType sourceBodyTemplateType = Proxy.getBodyTemplateType(sourceBodyTemplate);
        BodyTemplateType targetBodyTemplateType = Proxy.getBodyTemplateType(targetBodyTemplate);

        if (targetBodyTemplateType == BodyTemplateType.Normal && sourceBodyTemplateType == BodyTemplateType.Normal) {
            for (GeneralFlags flag : GeneralFlags.values()) {
                targetBodyTemplate.set(flag, sourceBodyTemplate.get(flag));
            }
        } else if (targetBodyTemplateType == BodyTemplateType.Normal && sourceBodyTemplateType == BodyTemplateType.Biped) {
            for (GeneralFlags flag : GeneralFlags.values()) {
                targetBodyTemplate.set(flag, false);
            }
        }

        for (FirstPersonFlags flag : FirstPersonFlags.values()) {
            if (flag != FirstPersonFlags.NONE) {
                targetBodyTemplate.set(targetBodyTemplateType, flag, sourceBodyTemplate.get(sourceBodyTemplateType, flag));
            }
        }

        target.setPickupSound(source.getPickupSound());
        target.setDropSound(source.getDropSound());

        target.setRace(source.getRace());

        copyArmatures(target, source);
    }

    private void copyArmatures(ARMO target, ARMO source) {
        ArrayList<FormID> sa = source.getArmatures();
        ArrayList<FormID> ta = target.getArmatures();

        ta.clear();
        ta.addAll(sa);
    }

    private void copyRecipes(ARMO target, ARMO source) {
        List<COBJ> template_recipes = getRecipes(source);

        for (COBJ template_recipe : template_recipes) {
            COBJ new_recipe = template_recipe.copy(target.getEDID() + template_recipe.getEDID());
            if (new_recipe.getResultFormID()
                .equals(source.getForm())) {
                new_recipe.setResultFormID(target.getForm());
            }
            new_recipe.getIngredients()
                .stream()
                .filter(sfi -> sfi.getForm()
                    .equals(source.getForm()))
                .forEach(sfi -> sfi.setForm(target.getForm()));
        }
    }

    private List<COBJ> getRecipes(ARMO source) {
        return getRecipesCache.computeIfAbsent(source, s -> merger.getConstructibleObjects()
            .getRecords()
            .stream()
            .filter(c -> c.getResultFormID()
                .equals(source.getForm()) || c.getIngredients()
                .stream()
                .anyMatch(sfi -> sfi.getForm()
                    .equals(source.getForm())))
            .collect(Collectors.toList()));
    }

    private Map<Gender, ArrayList<AltTexture>> getOriginalTextures(ARMO target) {
        Map<Gender, ArrayList<AltTexture>> originalTextures = new HashMap<Gender, ArrayList<AltTexture>>();

        // TODO process ARMA records

        for (Gender g : Gender.values()) {
            ArrayList<AltTexture> altTextureList = new ArrayList<AltTexture>();
            originalTextures.put(g, altTextureList);
            if (target.getAltTextures(g) != null) {
                altTextureList.addAll(target.getAltTextures(g));
            }

            ImmutableMap<String, AltTexture> altTexturesByName = Maps.uniqueIndex(altTextureList, at -> at.getName());
            ImmutableMap<Integer, AltTexture> altTexturesByIndex = Maps.uniqueIndex(altTextureList, at -> at.getIndex());

            String modelPathName = target.getModel(g);
            if (modelPathName.equals("")) {
                continue;
            }
            Path modelPath = meshesPath.resolve(modelPathName);
            NIF model;
            try {
                model = new NIF(modelPath.toFile());
            } catch (IOException | BadParameter | NullPointerException e) {
                SPGlobal.logError("mangleTextures", "Couldn't load texture information from " + modelPathName);
                SPGlobal.logException(e);
                continue;
            }

            ArrayList<TextureSet> textureSetList = model.extractTextureSets();
            for (TextureSet textureSet : textureSetList) {
                if (altTexturesByName.containsKey(textureSet.getName()) || altTexturesByIndex.containsKey(textureSet.getIndex())) {
                    continue;
                }

                // TODO mark this as having come from the NIF?

                ArrayList<String> textureList = textureSet.getTextures();

                // FiXME calculate reasonable edid for this.
                TXST txst = merger.getTextureSets()
                    .getRecords()
                    .get(0)
                    .getNew("", merger);

                AltTexture altTexture = new AltTexture(textureSet.getName(), txst.getForm(), textureSet.getIndex());
                altTextureList.add(altTexture);

                for (TextureMaps textureMap : TextureMaps.values()) {
                    if (textureList.contains(textureMap.ordinal())) {
                        txst.setNthMap(textureMap.ordinal(), textureList.get(textureMap.ordinal()));
                    }
                }
            }

        }

        return originalTextures;
    }

    enum TextureMaps {
        COLOR_MAP,
        NORMAL_MAP,
        MASK,
        TONE_MAP,
        DETAIL_MAP,
        ENVIRONMENT_MAP,
        UNUSED,
        SPECULARITY_MAP;
    }

    private void mangleTextures(ARMO target, ArmourTemplate source, Map<Gender, ArrayList<AltTexture>> originalTextures) {
        String materialType = source.getMaterialType();
        // TODO where to look for textures?
        Path materialTexturePath = texturesPath.resolve(materialType);
        for (Gender g : Gender.values()) {
            ArrayList<AltTexture> ot = originalTextures.get(g);
            for (AltTexture at : ot) {
                // at.getName();
                TXST txst = getTextureSet(at.getTexture());
                TXST newTxst = null;
                for (Enum<TextureMaps> e : TextureMaps.values()) {
                    String texturePathName = txst.getNthMap(e.ordinal());
                    if (texturePathName == null) {
                        continue;
                    }
                    Path texturePath = materialTexturePath.resolve(texturePathName);
                    if (!Files.exists(texturePath)) {
                        // TODO automatically build texture.
                        continue;
                    }
                    /* TODO do we need to create a unique name to get the
                     * texture to load? */
                    if (newTxst == null) {
                        newTxst = txst.copy(txst.getEDID() + materialType);
                        target.getAltTextures(g)
                            .add(new AltTexture(at.getName(), newTxst.getForm(), at.getIndex()));
                    }
                    String newTexturePathName = texturesPath.relativize(texturePath)
                        .toString();
                    newTxst.setNthMap(e.ordinal(), newTexturePathName);
                }
            }
        }
    }

    private TXST getTextureSet(FormID texture) {
        return merger.getTextureSets()
            .get(texture);
    }

}
