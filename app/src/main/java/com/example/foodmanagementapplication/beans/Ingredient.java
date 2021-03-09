package com.example.foodmanagementapplication.beans;

import com.example.foodmanagementapplication.utils.DateUtil;

import java.time.LocalDate;

public class Ingredient {
    private String name;
    private LocalDate expirationDate;

    public Ingredient(final String name, final LocalDate expirationDate) {
        this.name = name;
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(final LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        final StringBuilder res = new StringBuilder(name).append("\nExp : ");
        if(expirationDate.getDayOfMonth() < 10) {
            res.append(0);
        }
        res.append(expirationDate.getDayOfMonth()).append(DateUtil.SEPARATOR);
        if(expirationDate.getMonthValue() < 10) {
            res.append(0);
        }
        return res.append(expirationDate.getMonthValue()).append(DateUtil.SEPARATOR).append(expirationDate.getYear()).toString();
    }
}
