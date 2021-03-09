package com.example.foodmanagementapplication;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodmanagementapplication.beans.Ingredient;
import com.example.foodmanagementapplication.crud.ParentCRUD;
import com.example.foodmanagementapplication.utils.DateUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class IngredientRecyclerViewAdapter extends RecyclerView.Adapter<IngredientRecyclerViewAdapter.ViewHolder> {

    private List<Ingredient> mData;
    private LayoutInflater mInflater;
    private MainActivity mainActivity;
    private static final LocalDate actualDate = LocalDate.now();

    // data is passed into the constructor
    IngredientRecyclerViewAdapter(final MainActivity mainActivity) {
        this.mInflater = LayoutInflater.from(mainActivity);
        this.mainActivity = mainActivity;
        this.mData = new ArrayList<>();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Ingredient ingredient = this.mData.get(position);
        final TextView myTextView = holder.myTextView;

        holder.itemView.setOnClickListener(itemView -> {
            final AlertDialog.Builder popup = new AlertDialog.Builder(mainActivity);
            popup.setMessage("Suprimer " + ingredient.getName() + " ?")
                .setPositiveButton("OUI", (dialog, which) -> {
                final ParentCRUD crud = this.mainActivity.getCrud();
                try {
                    removeItem(position);
                    crud.setIngredientsList(this.mData);
                    crud.save();
                } catch (IOException e) {
                    this.mainActivity.setMessage("Erreur au moment de la sauvegarde");
                    e.printStackTrace();
                }
                Toast.makeText(this.mainActivity, "Suppression effectuée", Toast.LENGTH_SHORT).show();
            }).setNegativeButton("NON", (dialog, which) -> {
                Toast.makeText(this.mainActivity, "Suppresion annulée", Toast.LENGTH_SHORT).show();
            }).show();
        });

        if (DateUtil.checkPeriodicity(actualDate, ingredient.getExpirationDate())) {
            holder.itemView.setBackgroundColor(Color.RED);
            myTextView.setTextColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
            myTextView.setTextColor(Color.GRAY);
        }
        final String stringIngredient = ingredient.toString();
        myTextView.setText(stringIngredient);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.ingredientItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) { }
    }

    public void setAllIngredients(final List<Ingredient> ingredientsList) {
        this.mData = this.sortList(ingredientsList);
        this.notifyDataSetChanged();
    }

    List<Ingredient> getAllIngredients() {
        return this.mData;
    }

    void addItems(final List<Ingredient> ingredientsList) {
        this.mData.addAll(ingredientsList);
        this.mData = this.sortList(this.mData);
        this.notifyDataSetChanged();
    }

    void removeItem(final int id) {
        this.mData.remove(id);
        this.notifyDataSetChanged();
    }

    private List<Ingredient> sortList(final List<Ingredient> ingredientsList){
        return ingredientsList.stream().sorted((i1, i2) -> i1.getExpirationDate().compareTo(i2.getExpirationDate())).collect(Collectors.toList());
    }
}
