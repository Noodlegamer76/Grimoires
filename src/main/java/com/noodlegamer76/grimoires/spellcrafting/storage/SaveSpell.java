package com.noodlegamer76.grimoires.spellcrafting.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.noodlegamer76.grimoires.spellcrafting.Spell;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SaveSpell {

    public static void saveToFile(Spell spell) {
        String instanceLocation = SpellFileConstants.CLIENT_INSTANCE_LOCATION == null ?
                SpellFileConstants.SERVER_INSTANCE_LOCATION :
                SpellFileConstants.CLIENT_INSTANCE_LOCATION;

        Path directoryPath = Path.of(instanceLocation + SpellFileConstants.SPELL_LOCATION);
        Path fileName = directoryPath.resolve(spell.getName() + SpellFileConstants.FILE_EXTENSION);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        JsonObject jsonObject = new JsonObject();

        jsonObject.add(spell.getName(), spell.saveSpell());

        try {
            // Ensure the directory exists
            Files.createDirectories(directoryPath);

            // Create the FileWriter to write the file
            Path filePath = directoryPath.resolve(fileName);
            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                gson.toJson(jsonObject, writer);
                System.out.println("JSON file saved successfully to: " + filePath);
            } catch (IOException e) {
                System.err.println("Error writing JSON to file: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Error creating directories: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Spell loadFromFile(String name) {
        String instanceLocation = SpellFileConstants.CLIENT_INSTANCE_LOCATION == null ?
                SpellFileConstants.SERVER_INSTANCE_LOCATION :
                SpellFileConstants.CLIENT_INSTANCE_LOCATION;

        Path directoryPath = Path.of(instanceLocation + SpellFileConstants.SPELL_LOCATION);
        Path filePath = directoryPath.resolve(name + SpellFileConstants.FILE_EXTENSION);

        Spell spell = null;

        try (FileReader reader = new FileReader(filePath.toFile())) {
            Gson gson = new Gson();

            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            JsonObject spellObject = jsonObject.getAsJsonObject(name);

            if (spellObject == null) {
                return null;
            }

            spell = new Spell(name);
            spell.loadSpell(spellObject);

        } catch (IOException e) {
            //e.printStackTrace();
        }
        return spell;
    }

    public static List<Spell> getAllSpells() {
        List<Spell> spells = new ArrayList<>();
        String instanceLocation = SpellFileConstants.CLIENT_INSTANCE_LOCATION == null ?
                SpellFileConstants.SERVER_INSTANCE_LOCATION :
                SpellFileConstants.CLIENT_INSTANCE_LOCATION;

        Path directoryPath = Path.of(instanceLocation + SpellFileConstants.SPELL_LOCATION);

        List<File> jsonFiles = getAllJsonFiles(directoryPath.toString());

        for (File file : jsonFiles) {
            Spell spell = loadFromFile(file.getName().replace(".json", ""));

            if (spell != null) {
                spells.add(spell);
            }
        }

        return spells;
    }

    public static List<File> getAllJsonFiles(String path) {
        List<File> jsonFiles = new ArrayList<>();
        File root = new File(path);

        if (!root.exists() || !root.isDirectory()) {
            return jsonFiles;
        }

        File[] files = root.listFiles();

        if (files == null) {
            return jsonFiles;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                jsonFiles.add(file);
            }
        }

        return jsonFiles;
    }
}
