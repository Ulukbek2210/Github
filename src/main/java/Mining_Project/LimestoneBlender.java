package Mining_Project;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

    class Face {
        String id;
        int c3sPotential;
        // Not used in this C3S-specific calculation, but kept for context
        double totalAlkalis;
        double mgo;

        public Face(String id, int c3sPotential, double totalAlkalis, double mgo) {
            this.id = id;
            this.c3sPotential = c3sPotential;
            this.totalAlkalis = totalAlkalis;
            this.mgo = mgo;
        }

        @Override
        public String toString() {
            return "Face{" +
                    "id='" + id + '\'' +
                    ", c3sPotential=" + c3sPotential +
                    '}';
        }
    }

    public class LimestoneBlender {

        static final double TARGET_C3S_MIN = 30.0;
        static final double TARGET_C3S_MAX = 60.0;
        static final int MAX_TRUCK_RUNS_PER_FACE = 3; // Max coefficient for a single face in a combination
        static final int MIN_FACES_IN_BLEND = 2;
        static final int MAX_FACES_IN_BLEND = 4; // Iterate for blends of 2, 3, and 4 faces

        public static void main(String[] args) {
            Locale.setDefault(Locale.US); // To ensure dot is used as decimal separator in output

            List<Face> allFaces = new ArrayList<>();
            allFaces.add(new Face("F1", 270, 0.07, 0.5));
            allFaces.add(new Face("F2", 230, 0.10, 0.6));
            allFaces.add(new Face("F3", 175, 0.28, 1.3));
            allFaces.add(new Face("F4", 120, 0.38, 2.5));
            allFaces.add(new Face("F5", 95, 0.42, 1.8));
            allFaces.add(new Face("F6", 65, 0.18, 4.8));
            allFaces.add(new Face("F7", -123, 0.25, 1.1));
            allFaces.add(new Face("F8", -150, 0.30, 2.2));
            allFaces.add(new Face("F9", 205, 1.50, 0.8)); // High alkali, not directly checked here but C3S is used
            allFaces.add(new Face("F10", 150, 0.20, 3.8)); // High MgO, not directly checked here but C3S is used

            System.out.println("Searching for limestone blends with C3S Potential between " +
                    TARGET_C3S_MIN + " and " + TARGET_C3S_MAX + ".");
            System.out.println("Considering blends of " + MIN_FACES_IN_BLEND + " to " + MAX_FACES_IN_BLEND + " faces.");
            System.out.println("Number of truck runs (coefficients) per face in a blend: 1 to " + MAX_TRUCK_RUNS_PER_FACE + ".\n");

            int solutionsFound = 0;

            for (int k = MIN_FACES_IN_BLEND; k <= MAX_FACES_IN_BLEND; k++) {
                System.out.println("--- Trying combinations of " + k + " faces ---");
                List<Face> currentCombination = new ArrayList<>(k);
                solutionsFound += findFaceCombinationsRecursive(allFaces, k, 0, currentCombination);
            }

            if (solutionsFound == 0) {
                System.out.println("\nNo solutions found within the defined parameters.");
            } else {
                System.out.println("\nFound a total of " + solutionsFound + " solution(s).");
            }
        }

        // Recursive function to find combinations of faces
        private static int findFaceCombinationsRecursive(List<Face> allFaces, int k, int startFaceIndex,
                                                         List<Face> currentCombination) {
            int solutionsFound = 0;
            if (currentCombination.size() == k) {
                // We have a combination of k faces, now try coefficient combinations
                solutionsFound += solveForCoefficients(currentCombination, new int[k], 0);
                return solutionsFound;
            }

            if (startFaceIndex >= allFaces.size()) {
                return 0; // Not enough faces left to form a combination of size k
            }

            // Include current face
            currentCombination.add(allFaces.get(startFaceIndex));
            solutionsFound += findFaceCombinationsRecursive(allFaces, k, startFaceIndex + 1, currentCombination);
            currentCombination.remove(currentCombination.size() - 1); // Backtrack

            // Exclude current face (if there are enough remaining faces to still form a combination of size k)
            if (allFaces.size() - (startFaceIndex + 1) >= k - currentCombination.size()) {
                solutionsFound += findFaceCombinationsRecursive(allFaces, k, startFaceIndex + 1, currentCombination);
            }
            return solutionsFound;
        }


        // Recursive function to try different coefficients for a given set of chosen faces
        private static int solveForCoefficients(List<Face> chosenFaces, int[] coefficients, int faceIndex) {
            int solutionsFound = 0;
            if (faceIndex == chosenFaces.size()) {
                // All faces in the current combination have been assigned a coefficient
                double totalWeightedC3S = 0;
                double sumOfCoefficients = 0;

                for (int i = 0; i < chosenFaces.size(); i++) {
                    totalWeightedC3S += coefficients[i] * chosenFaces.get(i).c3sPotential;
                    sumOfCoefficients += coefficients[i];
                }

                if (sumOfCoefficients > 0) {
                    double averageC3S = totalWeightedC3S / sumOfCoefficients;
                    if (averageC3S >= TARGET_C3S_MIN && averageC3S <= TARGET_C3S_MAX) {
                        solutionsFound++;
                        System.out.print("Solution found: Average C3S = " + String.format("%.2f", averageC3S) + ". Blend: ");
                        for (int i = 0; i < chosenFaces.size(); i++) {
                            System.out.print(chosenFaces.get(i).id + " (Runs: " + coefficients[i] + (i == chosenFaces.size() - 1 ? ")" : "), "));
                        }
                        System.out.println();
                    }
                }
                return solutionsFound;
            }

            // Assign coefficients (truck runs) from 1 to MAX_TRUCK_RUNS_PER_FACE
            for (int runs = 1; runs <= MAX_TRUCK_RUNS_PER_FACE; runs++) {
                coefficients[faceIndex] = runs;
                solutionsFound += solveForCoefficients(chosenFaces, coefficients, faceIndex + 1);
            }
            return solutionsFound;
        }
    }

