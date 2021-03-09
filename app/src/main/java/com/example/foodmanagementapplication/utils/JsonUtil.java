package com.example.foodmanagementapplication.utils;

import android.content.Context;

import com.example.foodmanagementapplication.jsondate.LocalDateDeserializer;
import com.example.foodmanagementapplication.jsondate.LocalDateSerializer;
import com.example.foodmanagementapplication.beans.Ingredient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonUtil {
    public JsonUtil() {
        throw new IllegalStateException("Never instantiate utility classes");
    }

    private static String getDirectory(final Context context, final String fileName) {
        return new StringBuilder(context.getObbDir().toString()).append("/").append(fileName).toString();
    }

    public static List<Ingredient> readFromFile(final Context context, final String fileName) {
        String jsonString;
        try (InputStream is = new FileInputStream(createFile(getDirectory(context, fileName)))) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        if ("".equals(jsonString)) {
            return new ArrayList<>();
        }

        return new ArrayList<>(Arrays.asList(gsonBuilder().fromJson(jsonString, Ingredient[].class)));
    }

    public static File createFile(final String fileName) {
        final File file = new File(fileName);
        try {
            if (file.createNewFile()) {
                System.out.println("File has been created.");
            } else {
                System.out.println("File already exist.");
            }
        } catch (IOException e) {
            System.err.println("Error closing files : " + e);
        }

        return file;
    }

    public static void writeFile(final Context context, final List<Ingredient> ingredientsList, final String fileName) throws IOException {
        try(FileWriter file = new FileWriter(getDirectory(context, fileName))) {
            file.write(gsonBuilder().toJson(ingredientsList));
            file.flush();
        }
    }

    private static Gson gsonBuilder() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .setPrettyPrinting().create();
    }
}
