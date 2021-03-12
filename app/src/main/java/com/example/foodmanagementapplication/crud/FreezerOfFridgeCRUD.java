package com.example.foodmanagementapplication.crud;

import android.content.Context;

import com.example.foodmanagementapplication.utils.Util;

public class FreezerOfFridgeCRUD extends ParentCRUD {
    private static final String FILE_NAME = Util.FREEZER_OF_FRIDGE_FILE_NAME;

    public FreezerOfFridgeCRUD(final Context context) {
        super(context, FILE_NAME);
    }
}
