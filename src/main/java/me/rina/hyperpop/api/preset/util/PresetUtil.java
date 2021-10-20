package me.rina.hyperpop.api.preset.util;

import com.google.gson.*;
import me.rina.hyperpop.api.feature.Feature;
import me.rina.hyperpop.api.preset.Preset;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * @author SrRina
 * @since 20/10/2021 at 13:07
 **/
public class PresetUtil extends Feature {
    public PresetUtil() {
        super("PresetUtil", "Utilities functions for preset system.");
    }

    /**
     * Today.
     * @return Today, "Today".
     */
    public static String getToday() {
        return "today";
    }

    public static void savePresetData(Preset preset, boolean current, String optionPath) throws IOException {
        if (preset.getPath() == null) {
            preset.setPath(optionPath);
        }

        Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
        JsonParser parser = new JsonParser();

        String superiorFile = preset.getPath() + "/" + preset.getTag() + "/" + "preset.json";
        String superiorFolder = preset.getPath() + "/" + preset.getTag() + "/";

        if (!Files.exists(Paths.get(superiorFolder))) {
            Files.createDirectories(Paths.get(superiorFolder));
        }

        if (Files.exists(Paths.get(superiorFile))) {
            java.io.File file = new java.io.File(superiorFile);
            file.delete();
        }

        Files.createFile(Paths.get(superiorFile));

        JsonObject data = new JsonObject();

        data.add("tag", new JsonPrimitive(preset.getTag()));
        data.add("description", new JsonPrimitive(preset.getDescription()));
        data.add("last time handled", new JsonPrimitive(preset.getLastTimeHandled()));
        data.add("current", new JsonPrimitive(current));

        String string = gsonBuilder.toJson(parser.parse(data.toString()));
        OutputStreamWriter fileOutputStream = new OutputStreamWriter(new FileOutputStream(superiorFile), "UTF-8");

        fileOutputStream.write(string);
        fileOutputStream.close();
    }

    public static boolean readPresetData(Preset preset) {
        if (preset.getData() == null) {
            return false;
        }

        final JsonObject copiedObjectDataFromPreset = preset.getData();

        if (copiedObjectDataFromPreset.get("description") != null) {
            preset.setDescription(copiedObjectDataFromPreset.get("description").getAsString());
        }

        if (copiedObjectDataFromPreset.get("last time handled") != null) {
            preset.setLastTimeHandled(copiedObjectDataFromPreset.get("last time handled").getAsString());
        }

        return copiedObjectDataFromPreset.get("current") != null && copiedObjectDataFromPreset.get("current").getAsBoolean();
    }

    public static void updatePreset(Preset preset, String path) throws IOException {
        if (preset == null) {
            return;
        }

        if (preset.getPath() == null) {
            preset.setPath(path);
        }

        String thePath = preset.getPath() + "/" + preset.getTag() + "/" + "Preset.json";

        if (!Files.exists(Paths.get(path))) {
            return;
        }

        JsonParser parser = new JsonParser();
        InputStream input = Files.newInputStream(Paths.get(thePath));

        preset.setData(parser.parse(new InputStreamReader(input)).getAsJsonObject());

        input.close();
    }

    public static void createDirectory(Preset preset, String path) throws IOException {
        if (preset.getPath() == null) {
            preset.setPath(path);
        }

        Path formatted = Paths.get(preset.getPath() + "/" + preset.getTag());

        if (Files.exists(formatted)) {
            return;
        }

        Files.createDirectories(formatted);
    }

    public static void deleteFolder(Preset preset, String optionPath) throws IOException {
        if (preset == null) {
            return;
        }

        String thePath = preset.getPath() == null ? optionPath : preset.getPath();
        String path = thePath + "/" + preset.getTag();

        File file = new File(path);

        boolean doubleVerify = false;
        boolean found = Files.exists(Paths.get(path));

        if (!found) {
            doubleVerify = preset.getPath() != null;
        } else {
            FileUtils.deleteDirectory(file);

            return;
        }

        if (doubleVerify) {
            path = optionPath + "/" + preset.getTag();
            file = new File(path);

            if (Files.exists(Paths.get(path))) {
                FileUtils.deleteDirectory(file);
            }
        }
    }
}
