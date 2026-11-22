package com.fintrack.utils;

public class Formatador {

    // Formatador de dinheiro
    public static String formatarMoeda(
            double valor
    ) {

        return String.format(
                "R$ %.2f",
                valor
        );

    }

}
