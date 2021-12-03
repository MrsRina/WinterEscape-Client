package me.rina.winterescape.api.preset.management;

import com.google.gson.JsonObject;
import me.rina.winterescape.Client;
import me.rina.winterescape.api.feature.Feature;
import me.rina.winterescape.api.preset.Preset;
import me.rina.winterescape.api.preset.util.PresetUtil;
import me.rina.winterescape.api.value.ValueGeneric;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SrRina
 * @since 20/10/2021 at 13:11
 **/
public class PresetManager extends Feature {
    public static PresetManager INSTANCE;

    public static final int TASK_SAVE = 280;
    public static final int TASK_LOAD = 260;

    public static final int TASK_DATA = 140;
    public static final int TASK_SYNC = 132;

    public static ValueGeneric<String> PATH = new ValueGeneric<>("Path", "The path for save presets.", "hyperpop/data/");
    public static Preset DEFAULT = new Preset("Default", "Default preset.", "Today", "default", new JsonObject());

    private final List<Preset> presetList = new ArrayList<>();

    protected Preset current = DEFAULT;
    protected String path = DEFAULT.getTag();

    public PresetManager() {
        super("PresetManager", "Every system need a manager!");

        INSTANCE = this;
    }

    public void preInitAll() {
        task(TASK_SYNC);
        task(TASK_LOAD);
    }

    public static String officialCurrentPath() {
        return PATH.getValue() + "/" + INSTANCE.path + "/";
    }

    public void verify(boolean clearList) {
        if (clearList) {
            this.presetList.clear();
        }

        final Path path = Paths.get(PATH.getValue());

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException exc) {
                exc.printStackTrace();
            }

            return;
        }

        File directory = new File(PATH.getValue());

        if (directory.listFiles() == null || !directory.exists() || directory.listFiles().length == 0) {
            return;
        }

        try {
            for (File files : directory.listFiles()) {
                if (!files.isDirectory()) {
                    continue;
                }

                final Preset preset = new Preset(files.getName(), "No-description.");

                PresetUtil.updatePreset(preset, PATH.getValue());

                if (preset.getData() == null) {
                    continue;
                }

                boolean current = PresetUtil.readPresetData(preset);

                this.add(preset, true);

                if (current) {
                    this.current = preset;
                }
            }
        } catch (IOException exc) {
            Client.log("Corrupted verification of presets.");
        }
    }

    public void refresh() {
        try {
            for (Preset presets : this.presetList) {
                PresetUtil.savePresetData(presets, this.current.getTag().equals(presets.getTag()), PATH.getValue());
            }
        } catch (IOException exc) {
            Client.log("Unknown error occurred.");
        }
    }

    public void reload() {
        this.path = this.current.getTag();
    }

    public void setSave(Preset preset) {
        if (preset != null) {
            this.current = preset;
        }

        // Save shit.
        Client.save();
    }

    public void setLoad() {
        if (this.current == null) {
            return;
        }

        // Load shit.
        Client.load();
    }

    public void setSave$Current(Preset preset) {
        Preset lastCurrentPreset = this.current;
        this.current = preset;

        this.reload();

        // Save shit.
        Client.save();

        if (lastCurrentPreset != null) {
            this.current = lastCurrentPreset;
            this.reload();
        }
    }

    public void add(Preset preset, boolean createFolder) {
        this.presetList.add(preset);

        if (createFolder) {
            try {
                PresetUtil.createDirectory(preset, PATH.getValue());
            } catch (IOException exc) {
                Client.log("Failed to create a new directory, the local needs a admin request.");
            }
        }
    }

    public void remove(Preset preset, boolean deleteFolder) {
        this.presetList.remove(preset);

        if (deleteFolder) {
            try {
                PresetUtil.deleteFolder(preset, PATH.getValue());
            } catch (IOException exc) {
                Client.log("Failed to erase preset folder, not existent or invalid path.");
            }
        }
    }

    public static void task(int task) {
        if (task == TASK_SAVE) {
            INSTANCE.reload();
            INSTANCE.setSave(null);
        } else if (task == TASK_LOAD) {
            INSTANCE.reload();
            INSTANCE.setLoad();
        } else if (task == TASK_DATA) {
            INSTANCE.refresh();
        } else if (task == TASK_SYNC) {
            INSTANCE.verify(true);
            INSTANCE.reload();
        } else {
            throw new IllegalStateException();
        }
    }
}
