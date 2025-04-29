package Strings;

import java.util.Scanner;

public class Palindrome {
    public static void main(String[] args) {
        // "Palindrome"
        // "emord...."

        Scanner sc = new Scanner(System.in);
        String s = sc.next();
        String reversed = "";
        for(int i = s.length()-1; i>=0; --i ) {
            reversed +=  s.charAt(i);

        }
        if(s.equals(reversed)) {
            System.out.println("Palindrome");

        }
        else {
            System.out.println("Not Palindrome");
        }
        System.out.println(reversed);

    }

}
