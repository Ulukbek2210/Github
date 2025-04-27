package Java_Intensive;

import java.sql.SQLOutput;

public class Addition {
    public static int sum(int a, int b) {
        return a+b;

    }

    public static double sum (double a, double b) {
        return a+b;

    }
    public static void main(String[] args) {
        int a = 1;
        int b = 2;
        int c = a+b;
        double r=2.5;
        double t= 3.3;
        double y = r+t;


        System.out.println(c);
        System.out.println(y);

    }
}

