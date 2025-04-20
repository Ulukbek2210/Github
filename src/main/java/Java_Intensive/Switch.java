package Java_Intensive;

import java.util.Scanner;

public class Switch {

    public static   void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int dayNumber = sc.nextInt();


        switch (dayNumber) {
            case 1:
                System.out.println("Monday - Start of the work week.");
                break;
            case 2:
                System.out.println("Tuesday - Keep going!");
                break;
            case 3:
                System.out.println("Wednesday - ");
        }
    }
}
