package Java_Intensive;

import java.util.Random;
import java.util.Scanner;

public class RandomNumberGuess {
    public static void main(String[] args) {

        Random rand = new Random();
        int number = Math.abs(rand.nextInt()) % 100;
        System.out.println(number);


        Scanner sc = new Scanner(System.in);

        int guess = -1;
        while (guess != number) {
            guess = sc.nextInt();
            if (number == guess) {
                System.out.println("You Win!");


            } else {
                if (guess < number) {
                    System.out.println("Less");
                } else {
                    System.out.println("Greater");

                }

            }
        }
    }
}
