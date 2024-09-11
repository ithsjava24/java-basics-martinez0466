package org.example;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class App {
    static final int TIMMAR = 24;
    static int[] priser = new int[TIMMAR];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String val;

        do {
            visaMeny();
            val = scanner.nextLine().trim().toLowerCase();

            switch (val) {
                case "1":
                    inmatning(scanner);
                    break;
                case "2":
                    minMaxMedel();
                    break;
                case "3":
                    sortera();
                    break;
                case "4":
                    bastaLaddningstid();
                    break;
                case "e":
                    System.out.println("Programmet avslutas.");
                    break;
                default:
                    System.out.print("Ogiltigt val, försök igen.\n");
            }
        } while (!val.equals("e"));

        scanner.close();
    }

    private static void visaMeny() {
        System.out.println("""
                Elpriser
                ========
                1. Inmatning
                2. Min, Max och Medel
                3. Sortera
                4. Bästa Laddningstid (4h)
                5. Visualisering
                e. Avsluta""");
    }

    private static void inmatning(Scanner scanner) {
        System.out.print("Ange elpriser för varje timme (00-01 till 23-24).\n");

        for (int i = 0; i < TIMMAR; i++) {
            System.out.printf("%02d-%02d:", i, i + 1);
            priser[i] = scanner.nextInt();
        }
        scanner.nextLine(); // Clear the newline
    }

    private static void minMaxMedel() {
        Locale.setDefault(new Locale("sv", "SE"));
        if (priser.length == 0) {
            System.out.print("Inga elpriser inmatade.\n");
            return;
        }

        int min = Arrays.stream(priser).min().orElse(Integer.MAX_VALUE);
        int max = Arrays.stream(priser).max().orElse(Integer.MIN_VALUE);
        double avg = Arrays.stream(priser).average().orElse(Double.NaN);

        // Hitta första förekomsten av timme för min och max priser
        String minHour = "";
        String maxHour = "";
        for (int i = 0; i < priser.length; i++) {
            if (priser[i] == min && minHour.isEmpty()) {
                minHour = formatTime(i);
            }
            if (priser[i] == max && maxHour.isEmpty()) {
                maxHour = formatTime(i);
            }
            // Bryt loopen om båda min och max timmar har hittats
            if (!minHour.isEmpty() && !maxHour.isEmpty()) {
                break;
            }
        }

        // Uppdaterad utskrift
        System.out.printf("Lägsta pris: %s, %d öre/kWh\n", minHour, min);
        System.out.printf("Högsta pris: %s, %d öre/kWh\n", maxHour, max);
        System.out.printf("Medelpris: %.2f öre/kWh\n", avg);
    }

    private static void sortera() {


        Integer[] timmar = new Integer[TIMMAR];
        for (int i = 0; i < TIMMAR; i++) {
            timmar[i] = i;
        }

        Arrays.sort(timmar, (a, b) -> Integer.compare(priser[b], priser[a]));

        for (int i : timmar) {
            System.out.printf("%02d-%02d %d öre\n", i, i + 1, priser[i]);
        }
    }

    private static void bastaLaddningstid() {
        Locale.setDefault(new Locale("sv", "SE"));


        int startTid = 0;
        double minMedelPris = Double.MAX_VALUE;

        for (int i = 0; i <= TIMMAR - 4; i++) {
            int summa = 0;
            for (int j = 0; j < 4; j++) {
                summa += priser[i + j];
            }
            double medelPris = (double) summa / 4;
            if (medelPris < minMedelPris) {
                minMedelPris = medelPris;
                startTid = i;
            }
        }
        System.out.print("Påbörja laddning klockan " + startTid + "\n");
        System.out.printf("Medelpris 4h: %.1f öre/kWh\n", minMedelPris);

    }

    private static String formatTime(int hour) {
        return String.format("%02d-%02d", hour, hour + 1);
    }
}