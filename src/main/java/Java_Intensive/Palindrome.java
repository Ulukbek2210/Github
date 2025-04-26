package Java_Intensive;

import java.util.Scanner;

public class Palindrome {


    public static void main(String[] args ) {

        Scanner sc = new Scanner(System.in);
        String s = sc.next();
        String reversed = "";
        for (int i = s.length() - 1; i >= 0; --i)
        {
            char c = s.charAt(i);

            reversed += c;

        }
        if(s.equals(reversed)) {
            System.out.print("Palindrome");

        } else {
            System.out.println("Not Palindrome");

        }

        System.out.println(reversed);
    }




    }

