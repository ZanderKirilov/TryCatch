package com.example.sethcohen.muchotrabajo.Util;


public class Util {

    public static double twoDecimalPlaces(double salary) {

        return (double) Math.round(salary * 100) / 100;
    }
}
