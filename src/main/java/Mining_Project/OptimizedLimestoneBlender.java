package Mining_Project;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;



    class Faces {
        String id;
        int c3sPotential;
        // Not used in this C3S-specific calculation, but kept for context
        double totalAlkalis;
        double mgo;

        public Faces(String id, int c3sPotential, double totalAlkalis, double mgo) {
            this.id = id;
            this.c3sPotential = c3sPotential;
            this.totalAlkalis = totalAlkalis;
            this.mgo = mgo;
        }

        @Override
        public String toString() {
            // Simplified for BlendSolution display
            return id + " (C3S: " + c3sPotential + ")";
        }
    }

    class BlendSolution {
        List<String> faceDetailsInBlend; // Store as String for simpler display
        List<Integer> coefficients;
        double averageC3S;

        public BlendSolution(List<Faces> facesInBlendInput, int[] coefficientsInput, double averageC3S) {
            this.faceDetailsInBlend = new ArrayList<>();
            this.coefficients = new ArrayList<>();
            for (int i = 0; i < facesInBlendInput.size(); i++) {
                // Storing a custom string representation to avoid issues with Faces object references if they were mutable
                this.faceDetailsInBlend.add(facesInBlendInput.get(i).id);
                this.coefficients.add(coefficientsInput[i]);
            }
            this.averageC3S = averageC3S;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format(Locale.US, "Achieved Avg C3S: %.2f", averageC3S));
            sb.append(" | Blend: ");
            for (int i = 0; i < faceDetailsInBlend.size(); i++) {
                sb.append(faceDetailsInBlend.get(i)).append(" [Runs: ").append(coefficients.get(i)).append("]");
                if (i < faceDetailsInBlend.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
    }


    public class OptimizedLimestoneBlender {

        public static void main(String[] args) {
            Locale.setDefault(Locale.US);
            Scanner scanner = new Scanner(System.in);

            double targetC3sMin;
            double targetC3sMax;
            int maxTruckRunsPerFaces;

            System.out.println("--- Limestone Blend Optimizer ---");

            // Get user inputs
            while (true) {
                System.out.print("Enter Minimum Target C3S Potential (e.g., 30): ");
                if (scanner.hasNextDouble()) {
                    targetC3sMin = scanner.nextDouble();
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // consume invalid input
                }
            }

            while (true) {
                System.out.print("Enter Maximum Target C3S Potential (e.g., 60): ");
                if (scanner.hasNextDouble()) {
                    targetC3sMax = scanner.nextDouble();
                    if (targetC3sMax >= targetC3sMin) {
                        break;
                    } else {
                        System.out.println("Max C3S must be greater than or equal to Min C3S. Please re-enter.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next();
                }
            }

            while (true) {
                System.out.print("Enter Maximum Truck Runs (coefficient) per Faces (e.g., 3): ");
                if (scanner.hasNextInt()) {
                    maxTruckRunsPerFaces = scanner.nextInt();
                    if (maxTruckRunsPerFaces > 0) {
                        break;
                    } else {
                        System.out.println("Max truck runs must be a positive integer.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter an integer.");
                    scanner.next();
                }
            }
            scanner.close();


            List<Faces> allFacess = new ArrayList<>();
            allFacess.add(new Faces("F1", 270, 0.07, 0.5));
            allFacess.add(new Faces("F2", 230, 0.10, 0.6));
            allFacess.add(new Faces("F3", 175, 0.28, 1.3));
            allFacess.add(new Faces("F4", 120, 0.38, 2.5));
            allFacess.add(new Faces("F5", 95, 0.42, 1.8));
            allFacess.add(new Faces("F6", 65, 0.18, 4.8));
            allFacess.add(new Faces("F7", -123, 0.25, 1.1));
            allFacess.add(new Faces("F8", -150, 0.30, 2.2));
            allFacess.add(new Faces("F9", 205, 1.50, 0.8));
            allFacess.add(new Faces("F10", 150, 0.20, 3.8));

            System.out.println("\nSearching for limestone blends with C3S Potential between " +
                    targetC3sMin + " and " + targetC3sMax + ".");
            System.out.println("Number of truck runs (coefficients) per face in a blend: 1 to " + maxTruckRunsPerFaces + ".\n");

            List<BlendSolution> twoFacesSolutions = new ArrayList<>();
            List<BlendSolution> threeFacesSolutions = new ArrayList<>();

            // --- Find solutions for 2 faces ---
            System.out.println("--- Evaluating blends of 2 faces ---");
            List<Faces> currentCombination2Facess = new ArrayList<>(2);
            findFacesCombinationsRecursive(allFacess, 2, 0, currentCombination2Facess,
                    maxTruckRunsPerFaces, targetC3sMin, targetC3sMax, twoFacesSolutions);

            // --- Find solutions for 3 faces ---
            System.out.println("\n--- Evaluating blends of 3 faces ---");
            List<Faces> currentCombination3Facess = new ArrayList<>(3);
            findFacesCombinationsRecursive(allFacess, 3, 0, currentCombination3Facess,
                    maxTruckRunsPerFaces, targetC3sMin, targetC3sMax, threeFacesSolutions);

            // --- Display stored results ---
            System.out.println("\n--- Results ---");
            System.out.println("\nSolutions with 2 Facess:");
            if (twoFacesSolutions.isEmpty()) {
                System.out.println("No solutions found for 2-face blends within the given parameters.");
            } else {
                for (BlendSolution sol : twoFacesSolutions) {
                    System.out.println(sol);
                }
                System.out.println("Total 2-face solutions: " + twoFacesSolutions.size());
            }

            System.out.println("\nSolutions with 3 Facess:");
            if (threeFacesSolutions.isEmpty()) {
                System.out.println("No solutions found for 3-face blends within the given parameters.");
            } else {
                for (BlendSolution sol : threeFacesSolutions) {
                    System.out.println(sol);
                }
                System.out.println("Total 3-face solutions: " + threeFacesSolutions.size());
            }
        }

        private static void findFacesCombinationsRecursive(List<Faces> allFacess, int k, int startFacesIndex,
                                                          List<Faces> currentCombination,
                                                          int maxTruckRunsPerFacesInput,
                                                          double targetC3sMin, double targetC3sMax,
                                                          List<BlendSolution> solutionsList) {
            if (currentCombination.size() == k) {
                solveForCoefficients(currentCombination, new int[k], 0,
                        maxTruckRunsPerFacesInput, targetC3sMin, targetC3sMax, solutionsList);
                return;
            }

            if (startFacesIndex >= allFacess.size()) {
                return;
            }

            // Include current face
            currentCombination.add(allFacess.get(startFacesIndex));
            findFacesCombinationsRecursive(allFacess, k, startFacesIndex + 1, currentCombination,
                    maxTruckRunsPerFacesInput, targetC3sMin, targetC3sMax, solutionsList);
            currentCombination.remove(currentCombination.size() - 1); // Backtrack

            // Exclude current face (if there are enough remaining faces)
            if (allFacess.size() - (startFacesIndex + 1) >= k - currentCombination.size()) {
                findFacesCombinationsRecursive(allFacess, k, startFacesIndex + 1, currentCombination,
                        maxTruckRunsPerFacesInput, targetC3sMin, targetC3sMax, solutionsList);
            }
        }

        private static void solveForCoefficients(List<Faces> chosenFacess, int[] coefficients, int faceIndex,
                                                 int maxTruckRunsPerFacesInput,
                                                 double targetC3sMin, double targetC3sMax,
                                                 List<BlendSolution> solutionsList) {
            if (faceIndex == chosenFacess.size()) {
                double totalWeightedC3S = 0;
                double sumOfCoefficients = 0;

                for (int i = 0; i < chosenFacess.size(); i++) {
                    totalWeightedC3S += coefficients[i] * chosenFacess.get(i).c3sPotential;
                    sumOfCoefficients += coefficients[i];
                }

                if (sumOfCoefficients > 0) {
                    double averageC3S = totalWeightedC3S / sumOfCoefficients;
                    if (averageC3S >= targetC3sMin && averageC3S <= targetC3sMax) {
                        // Create a new list for facesInBlendInput to avoid modification issues with currentCombination
                        solutionsList.add(new BlendSolution(new ArrayList<>(chosenFacess), coefficients.clone(), averageC3S));
                    }
                }
                return;
            }

            for (int runs = 1; runs <= maxTruckRunsPerFacesInput; runs++) {
                coefficients[faceIndex] = runs;
                solveForCoefficients(chosenFacess, coefficients, faceIndex + 1,
                        maxTruckRunsPerFacesInput, targetC3sMin, targetC3sMax, solutionsList);
            }
        }
    }

