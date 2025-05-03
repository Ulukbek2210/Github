package Blending;

import java.util.Scanner;

public class ClosestToTarget {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Define the samples
        double[][] samples = {
                {257, 0.34, 0.82},
                {279, 0.43, 0.46},
                {131, 0.48, 1.12},
                {190, 0.35, 0.93},
                {133, 0.48, 1.25},
                {112, 0.62, 2},
                {-123, 1.121, 1.5},
                {23, 0.57, 1.74},
                {96, 0.72, 1.61},
                {-95, 2.12, 1}
        };

        // Target values
        double targetCalcium = 45.0;
        double targetAlkali = 0.65;
        double targetMagnesium = 3.0;

        // Variables to store the best combination and its difference for both 2-shot and 3-shot samples
        int[] bestCombination2Shots = new int[2];
        double smallestDifference2Shots = Double.MAX_VALUE;

        int[] bestCombination3Shots = new int[3];
        double smallestDifference3Shots = Double.MAX_VALUE;

        // Iterate over all possible combinations of samples (2 samples)
        for (int i = 0; i < samples.length; i++) {
            for (int j = i + 1; j < samples.length; j++) {
                double averageCalcium = calculateAverage(samples[i][0], samples[j][0]);
                double averageAlkali = calculateAverage(samples[i][1], samples[j][1]);
                double averageMagnesium = calculateAverage(samples[i][2], samples[j][2]);

                // Ensure that the alkali value does not exceed the target
                if (averageAlkali > targetAlkali) {
                    continue;
                }

                double calciumDifference = Math.abs(averageCalcium - targetCalcium);
                double alkaliDifference = Math.abs(averageAlkali - targetAlkali);
                double magnesiumDifference = Math.abs(averageMagnesium - targetMagnesium);

                double totalDifference = calciumDifference + alkaliDifference + magnesiumDifference;

                if (totalDifference < smallestDifference2Shots) {
                    smallestDifference2Shots = totalDifference;
                    bestCombination2Shots[0] = i;
                    bestCombination2Shots[1] = j;
                }
            }
        }

        // Iterate over all possible combinations of samples (3 samples)
        for (int i = 0; i < samples.length; i++) {
            for (int j = i + 1; j < samples.length; j++) {
                for (int k = j + 1; k < samples.length; k++) {
                    double averageCalcium = calculateAverage(samples[i][0], samples[j][0], samples[k][0]);
                    double averageAlkali = calculateAverage(samples[i][1], samples[j][1], samples[k][1]);
                    double averageMagnesium = calculateAverage(samples[i][2], samples[j][2], samples[k][2]);

                    // Ensure that the alkali value does not exceed the target
                    if (averageAlkali > targetAlkali) {
                        continue;
                    }

                    double calciumDifference = Math.abs(averageCalcium - targetCalcium);
                    double alkaliDifference = Math.abs(averageAlkali - targetAlkali);
                    double magnesiumDifference = Math.abs(averageMagnesium - targetMagnesium);

                    double totalDifference = calciumDifference + alkaliDifference + magnesiumDifference;

                    if (totalDifference < smallestDifference3Shots) {
                        smallestDifference3Shots = totalDifference;
                        bestCombination3Shots[0] = i;
                        bestCombination3Shots[1] = j;
                        bestCombination3Shots[2] = k;
                    }
                }
            }
        }

        // Display the results for 2-shot samples
        System.out.println("Target values:");
        System.out.println("Calcium: " + targetCalcium);
        System.out.println("Alkali: " + targetAlkali);
        System.out.println("Magnesium: " + targetMagnesium);

        System.out.println("\nBest combination of samples (2 shots):");
        for (int i = 0; i < bestCombination2Shots.length; i++) {
            System.out.println("Sample " + (bestCombination2Shots[i] + 1) + ": Calcium = " + samples[bestCombination2Shots[i]][0] + ", Alkali = " + samples[bestCombination2Shots[i]][1] + ", Magnesium = " + samples[bestCombination2Shots[i]][2]);
        }

        double averageCalcium2Shots = calculateAverage(samples[bestCombination2Shots[0]][0], samples[bestCombination2Shots[1]][0]);
        double averageAlkali2Shots = calculateAverage(samples[bestCombination2Shots[0]][1], samples[bestCombination2Shots[1]][1]);
        double averageMagnesium2Shots = calculateAverage(samples[bestCombination2Shots[0]][2], samples[bestCombination2Shots[1]][2]);

        System.out.println("\nAverages of the best combination (2 shots):");
        System.out.println("Average Calcium: " + averageCalcium2Shots);
        System.out.println("Average Alkali: " + averageAlkali2Shots);
        System.out.println("Average Magnesium: " + averageMagnesium2Shots);

        // Display the results for 3-shot samples
        System.out.println("\nBest combination of samples (3 shots):");
        for (int i = 0; i < bestCombination3Shots.length; i++) {
            System.out.println("Sample " + (bestCombination3Shots[i] + 1) + ": Calcium = " + samples[bestCombination3Shots[i]][0] + ", Alkali = " + samples[bestCombination3Shots[i]][1] + ", Magnesium = " + samples[bestCombination3Shots[i]][2]);
        }

        double averageCalcium3Shots = calculateAverage(samples[bestCombination3Shots[0]][0], samples[bestCombination3Shots[1]][0], samples[bestCombination3Shots[2]][0]);
        double averageAlkali3Shots = calculateAverage(samples[bestCombination3Shots[0]][1], samples[bestCombination3Shots[1]][1], samples[bestCombination3Shots[2]][1]);
        double averageMagnesium3Shots = calculateAverage(samples[bestCombination3Shots[0]][2], samples[bestCombination3Shots[1]][2], samples[bestCombination3Shots[2]][2]);

        System.out.println("\nAverages of the best combination (3 shots):");
        System.out.println("Average Calcium: " + averageCalcium3Shots);
        System.out.println("Average Alkali: " + averageAlkali3Shots);
        System.out.println("Average Magnesium: " + averageMagnesium3Shots);
    }

    // Method to calculate the average of two values
    public static double calculateAverage(double a, double b) {
        return (a + b) / 2.0;
    }

    // Method to calculate the average of three values
    public static double calculateAverage(double a, double b, double c) {
        return (a + b + c) / 3.0;
    }
}
