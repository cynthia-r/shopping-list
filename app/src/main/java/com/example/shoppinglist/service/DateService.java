package com.example.shoppinglist.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateService {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE d MMMM", Locale.FRENCH);

    public String getNextDate(String currentList) {
        Calendar date = Calendar.getInstance();

        int day = Calendar.TUESDAY;
        switch (currentList) {
            case "Mardi":
                day = Calendar.TUESDAY;
                break;
            case "Vendredi":
                day = Calendar.FRIDAY;
            default:
                break;
        }

        while (date.get(Calendar.DAY_OF_WEEK) != day) {
            date.add(Calendar.DATE, 1);
        }

        return simpleDateFormat.format(date.getTime());
    }
}
