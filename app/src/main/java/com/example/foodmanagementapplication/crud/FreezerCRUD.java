package com.example.foodmanagementapplication.crud;

import android.content.Context;

import com.example.foodmanagementapplication.beans.Ingredient;
import com.example.foodmanagementapplication.utils.JsonUtil;
import com.example.foodmanagementapplication.utils.Util;

import java.io.IOException;
import java.util.List;

public class FreezerCRUD extends ParentCRUD {
    private static final String FILE_NAME = Util.FREEZER_FILE_NAME;

    public FreezerCRUD(final Context context) {
        super(context, FILE_NAME);
    }
}
