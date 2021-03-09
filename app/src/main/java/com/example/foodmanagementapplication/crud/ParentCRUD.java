package com.example.foodmanagementapplication.crud;

import android.content.Context;

import com.example.foodmanagementapplication.beans.Ingredient;
import com.example.foodmanagementapplication.utils.JsonUtil;
import com.example.foodmanagementapplication.utils.Util;

import java.io.IOException;
import java.util.List;

public class ParentCRUD {
    private Context context;
    private static List<Ingredient> ingredientsList;
    private String fileName;
    private boolean active;

    public ParentCRUD(final Context context, final String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public List<Ingredient> read() {
        this.ingredientsList = JsonUtil.readFromFile(this.context, this.fileName);
        this.active = true;
        return this.ingredientsList;
    }

    public void setIngredientsList(final List<Ingredient> mData) {
        this.ingredientsList = mData;
    }

    public void save() throws IOException {
        JsonUtil.writeFile(this.context, this.ingredientsList, this.fileName);
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
