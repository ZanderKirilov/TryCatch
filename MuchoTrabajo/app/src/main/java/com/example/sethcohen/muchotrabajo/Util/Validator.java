package com.example.sethcohen.muchotrabajo.Util;

import android.graphics.Bitmap;
import android.util.Patterns;

import java.io.ByteArrayOutputStream;


public class Validator {

    public static boolean validateName(String name) {
        String pattern = "[a-zA-Zа-яА-Я][a-zA-Zа-яА-Я ][a-zA-Zа-яА-Я][a-zA-Zа-яА-Я ]*";
        return name.matches(pattern);
    }

    public static boolean validatePassword(String password) {
        String pattern = "^(?=.*[a-zA-Z])(?=.*[0-9]).{6,}";
        return password.matches(pattern);
    }

    public static boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
}
