package me.rina.hyperpop.impl.module.management;

import com.google.gson.*;
import me.rina.hyperpop.api.feature.Feature;
import me.rina.hyperpop.api.module.Module;
import me.rina.hyperpop.api.module.overlay.OverlayElement;
import me.rina.hyperpop.api.preset.management.PresetManager;
import me.rina.hyperpop.api.value.Value;
import me.rina.hyperpop.api.value.type.*;
import me.rina.hyperpop.impl.module.impl.client.ModuleUserInterface;
import me.rina.hyperpop.impl.module.impl.combat.ModuleKillAura;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author SrRina
 * @since 06/09/2021 at 19:01
 **/
public class ModuleManager extends Feature {
    public static ModuleManager INSTANCE;

    private final List<Module> moduleList = new ArrayList<>();

    public ModuleManager() {
        super("Module", "Process all modules from Solaros client.");

        INSTANCE = this;
    }

    public void preInitAll() {
        this.add(new ModuleKillAura());
        this.add(new ModuleUserInterface());

        // Dev thing.
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
        this.add(new ModuleKillAura());
    }

    public void onKeyboard(int key) {
        for (Module modules : this.getModuleList()) {
            if (modules.equalsKeyBind(key)) {
                modules.reload(!modules.isEnabled());
            }
        }
    }

    public void onSetting() {
        for (Module modules : this.getModuleList()) {
            modules.onSetting();
        }
    }

    public void onWorldRender(float partialTicks) {
        for (Module modules : this.getModuleList()) {
            if (modules.isEnabled()) {
                modules.onWorldRender(partialTicks);
            }
        }
    }

    public void onOverlayRender(float partialTicks) {
        for (Module modules: this.getModuleList()) {
            if (modules.isEnabled()) {
                modules.onOverlayRender(partialTicks);
            }
        }
    }

    public void add(Module module) {
        try {
            for (Field fields : module.getClass().getDeclaredFields()) {
                if (Value.class.isAssignableFrom(fields.getType())) {
                    if (!fields.isAccessible()) {
                        fields.setAccessible(true);
                    }

                    module.registry((Value) fields.get(module));
                }
            }

            this.moduleList.add(module);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public void remove(Module module) {
        this.moduleList.remove(module);
    }

    public static Module get(String tag) {
        Module module = null;

        for (Module modules : INSTANCE.getModuleList()) {
            if (modules.getTag().equalsIgnoreCase(tag)) {
                module = modules;

                break;
            }
        }

        return module;
    }

    // lol
    public static Module get(Module module) {
        return get(module.getTag());
    }

    public List<Module> getModuleList() {
        return moduleList;
    }

    public void saveModuleList() {
        for (Module modules : this.moduleList) {
            try {
                this.createFileJson(modules);
            } catch (IOException | IllegalMonitorStateException exc) {}
        }
    }

    public void loadModuleList() {
        for (Module modules : this.moduleList) {
            try {
                this.readFileJson(modules);
            } catch (IOException | IllegalMonitorStateException exc) {}

            modules.refresh();
        }
    }

    public void createFileJson(Module module) throws IOException {
        Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
        JsonParser parser = new JsonParser();

        String superiorFile = PresetManager.officialCurrentPath() + "modules/" + module.getTag() + ".json";
        String superiorFolder = PresetManager.officialCurrentPath() + "modules/";

        if (!Files.exists(Paths.get(superiorFolder))) {
            Files.createDirectories(Paths.get(superiorFolder));
        }

        if (Files.exists(Paths.get(superiorFile))) {
            java.io.File file = new java.io.File(superiorFile);
            file.delete();
        }

        Files.createFile(Paths.get(superiorFile));

        JsonObject data = new JsonObject();

        for (Map.Entry<String, Value> entry : module.getRegister().entrySet()) {
            Value value = entry.getValue();

            if (value instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) value;

                data.add(checkBox.getTag(), new JsonPrimitive(checkBox.getValue()));
            } else if (value instanceof Entry) {
                Entry entryValue = (Entry) value;

                data.add(entryValue.getTag(), new JsonPrimitive(entryValue.getValue()));
            } else if (value instanceof Slider) {
                Slider slider = (Slider) value;

                data.add(slider.getTag(), new JsonPrimitive(slider.getValue()));
            } else if (value instanceof Combobox) {
                Combobox combobox = (Combobox) value;

                data.add(combobox.getTag(), new JsonPrimitive(combobox.getValue()));
            } else if (value instanceof BindBox) {
                BindBox bindBox = (BindBox) value;
                JsonObject objectBindBox = new JsonObject();

                objectBindBox.add("key", new JsonPrimitive(bindBox.getKey() == -1 ? "NONE" : Keyboard.getKeyName(bindBox.getKey())));
                objectBindBox.add("state", new JsonPrimitive(bindBox.getValue()));

                data.add(bindBox.getTag(), objectBindBox);
            }
        }

        String string = gsonBuilder.toJson(parser.parse(data.toString()));
        OutputStreamWriter fileOutputStream = new OutputStreamWriter(new FileOutputStream(superiorFile), "UTF-8");

        fileOutputStream.write(string);
        fileOutputStream.close();
    }

    public void readFileJson(Module module) throws IOException {
        String thePath = PresetManager.officialCurrentPath() + "modules/" + module.getTag() + ".json";

        JsonParser parser = new JsonParser();
        InputStream input = Files.newInputStream(Paths.get(thePath));

        JsonObject object = parser.parse(new InputStreamReader(input)).getAsJsonObject();

        for (Map.Entry<String, Value> entry : module.getRegister().entrySet()) {
            Value value = entry.getValue();
            JsonElement valueObject = object.get(entry.getKey());

            if (valueObject == null) {
                continue;
            }

            if (value instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) value;

                checkBox.setValue(valueObject.getAsBoolean());
            } else if (value instanceof Entry) {
                Entry entryValue = (Entry) value;

                entryValue.setValue(valueObject.getAsString());
            } else if (value instanceof Slider) {
                Slider slider = (Slider) value;

                if (slider.getValue() instanceof Integer) {
                    slider.setValue(valueObject.getAsInt());
                } else if (slider.getValue() instanceof Double) {
                    slider.setValue(valueObject.getAsDouble());
                } else if (slider.getValue() instanceof Float) {
                    slider.setValue(valueObject.getAsFloat());
                }
            } else if (value instanceof Combobox) {
                Combobox combobox = (Combobox) value;

                combobox.setValue(valueObject.getAsString());
            } else if (value instanceof BindBox) {
                BindBox bindBox = (BindBox) value;

                if (valueObject.getAsJsonObject().get("key") != null) {
                    String keyName = valueObject.getAsJsonObject().get("key").getAsString();

                    bindBox.setKey(keyName.equalsIgnoreCase("NONE") ? -1 : Keyboard.getKeyIndex(keyName));
                }

                if (valueObject.getAsJsonObject().get("state") != null) {
                    bindBox.setValue(valueObject.getAsJsonObject().get("state").getAsBoolean());
                }
            }
        }

        input.close();

        if (module instanceof OverlayElement) {
            OverlayElement overlayElement = (OverlayElement) module;
            overlayElement.resetPosition$Dock();
        }
    }
}
