package Java_Intensive;

import java.util.Scanner;

public class Tree {

    public static void main(String[] args) {


        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of rows: ");
        int rows = sc.nextInt();

        for (int i = 1; i <= rows; i++) {
            // Calculate spaces needed for centering
            int spaces = rows - i;

            // Print spaces
            for (int j = 0; j < spaces; j++) {
                System.out.print(" ");
            }

            // Print stars
            for (int k = 0; k < ( 2 * i - 1); k++) {
                System.out.print("*");
            }

            // Move to the next line
            System.out.println();
        }

        sc.close();
    }
}