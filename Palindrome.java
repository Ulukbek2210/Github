package JavaPracticeExapmles;

import java.util.Scanner;

public class Palindrome {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String reversed = "";

        for (int i = s.length() - 1; i >= 0; i--) {
            char ch = s.charAt(i);
            reversed += ch;
        }
        if (s.equals(reversed)) {
            System.out.println("Yes it is Palindrome");

        }   else {
            System.out.println("No it is not Palindrome");
        }
    }
}
