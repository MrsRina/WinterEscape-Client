package me.rina.zyra.impl.module.management;

import com.google.gson.*;
import me.rina.zyra.api.feature.Feature;
import me.rina.zyra.api.module.Module;
import me.rina.zyra.api.module.overlay.OverlayElement;
import me.rina.zyra.api.preset.management.PresetManager;
import me.rina.zyra.api.value.Value;
import me.rina.zyra.api.value.type.*;
import me.rina.zyra.impl.module.impl.client.ModuleHUDEditor;
import me.rina.zyra.impl.module.impl.client.ModuleUserInterface;
import me.rina.zyra.impl.module.impl.combat.ModuleFastBow;
import me.rina.zyra.impl.module.impl.combat.ModuleKillAura;
import me.rina.zyra.impl.module.impl.player.FastUse;
import me.rina.zyra.impl.module.internal.OverlayElementWelcome;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        // Client.
        this.add(new ModuleHUDEditor());
        this.add(new ModuleUserInterface());

        // Combat.
        this.add(new ModuleKillAura());
        this.add(new ModuleFastBow());
        this.add(new ModuleFastBow());
        this.add(new ModuleFastBow());
        this.add(new ModuleFastBow());
        this.add(new ModuleFastBow());
        this.add(new ModuleFastBow());
        this.add(new ModuleFastBow());
        this.add(new ModuleFastBow());

        this.add(new ModuleFastBow());
        this.add(new ModuleFastBow());
        this.add(new ModuleFastBow());
        this.add(new ModuleFastBow());
        this.add(new ModuleFastBow());
        this.add(new ModuleFastBow());
        this.add(new ModuleFastBow());
        this.add(new ModuleFastBow());
        this.add(new ModuleFastBow());
        this.add(new ModuleFastBow());
        this.add(new ModuleFastBow());
        this.add(new ModuleFastBow());

        // Player.
        this.add(new FastUse());

        // Overlay.
        this.add(new OverlayElementWelcome());
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
        final boolean editorIsEnabled = ModuleHUDEditor.INSTANCE.isEnabled();

        for (Module modules : this.getModuleList()) {
            if (modules.isEnabled() && !editorIsEnabled) {
                modules.onOverlayRender(partialTicks);

                GL11.glPushMatrix();

                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_BLEND);

                GlStateManager.enableBlend();

                GL11.glPopMatrix();

                GlStateManager.enableCull();
                GlStateManager.depthMask(true);
                GlStateManager.enableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.enableDepth();
            }
        }
    }

    public void onShutdown() {
        for (Module modules : this.getModuleList()) {
            modules.onShutdown();
        }
    }

    public void add(Module module) {
        try {
            for (Field fields : module.getClass().getDeclaredFields()) {
                if (Value.class.isAssignableFrom(fields.getType())) {
                    if (!fields.isAccessible()) {
                        fields.setAccessible(true);
                    }

                    final Value valueDeclared = (Value) fields.get(module);

                    module.registry(valueDeclared);
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

        for (Value values : module.getValueList()) {
            if (values instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) values;

                data.add(checkBox.getTag(), new JsonPrimitive(checkBox.getValue()));
            } else if (values instanceof Entry) {
                Entry entryValue = (Entry) values;

                data.add(entryValue.getTag(), new JsonPrimitive(entryValue.getValue()));
            } else if (values instanceof Slider) {
                Slider slider = (Slider) values;

                data.add(slider.getTag(), new JsonPrimitive(slider.getValue()));
            } else if (values instanceof Combobox) {
                Combobox combobox = (Combobox) values;

                data.add(combobox.getTag(), new JsonPrimitive(combobox.getValue()));
            } else if (values instanceof BindBox) {
                BindBox bindBox = (BindBox) values;
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

        for (Value values : module.getValueList()) {
            JsonElement valueObject = object.get(values.getTag());

            if (valueObject == null) {
                continue;
            }

            if (values instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) values;

                checkBox.setValue(valueObject.getAsBoolean());
            } else if (values instanceof Entry) {
                Entry entryValue = (Entry) values;

                entryValue.setValue(valueObject.getAsString());
            } else if (values instanceof Slider) {
                Slider slider = (Slider) values;

                if (slider.getValue() instanceof Integer) {
                    slider.setValue(valueObject.getAsInt());
                } else if (slider.getValue() instanceof Double) {
                    slider.setValue(valueObject.getAsDouble());
                } else if (slider.getValue() instanceof Float) {
                    slider.setValue(valueObject.getAsFloat());
                }
            } else if (values instanceof Combobox) {
                Combobox combobox = (Combobox) values;

                combobox.setValue(valueObject.getAsString());
            } else if (values instanceof BindBox) {
                BindBox bindBox = (BindBox) values;

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
