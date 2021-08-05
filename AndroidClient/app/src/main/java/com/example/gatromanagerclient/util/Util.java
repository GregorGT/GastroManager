package com.example.gatromanagerclient.util;

import android.content.Context;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class Util {

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isEmptyOrNull(TextInputEditText textInputEditText){
        if(Objects.requireNonNull(textInputEditText.getText()).length() == 0 )
            return true;
        if(textInputEditText.getText() == null)
            return true;
        if(textInputEditText.getText().toString().equals(""))
            return true;
        return false;
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
