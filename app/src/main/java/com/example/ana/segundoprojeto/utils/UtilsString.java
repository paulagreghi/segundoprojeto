package com.example.ana.segundoprojeto.utils;

public class UtilsString {

    public static boolean stringVazia(String texto){

        if (texto == null || texto.trim().length() == 0){
            return true;
        }else{
            return false;
        }
    }
}
