package com.example.foodmanagementapplication.crud;

import android.content.Context;

import com.example.foodmanagementapplication.utils.Util;

public class FridgeCRUD extends ParentCRUD {
    private static final String FILE_NAME = Util.FRIDGE_FILE_NAME;

    public FridgeCRUD(final Context context) {
        super(context, FILE_NAME);
    }
}
