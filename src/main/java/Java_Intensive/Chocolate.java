package Java_Intensive;

import java.util.Scanner;

public class Chocolate {
    public static void main(String[] args){

Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
        int m = sc.nextInt();
        int k = sc.nextInt();





    int area = n*m;

    if(((area>k) && (k%m==0)) || (k%n==0) && (k<area))
        System.out.println("Yes");
    else
        System.out.println("NO");

}
}
