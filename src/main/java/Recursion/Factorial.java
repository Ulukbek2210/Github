package Recursion;

public class Factorial {
    // n! = 1*2*3...*n
    // factorial = | factorial(n-1)*n, if n>1
    //             | 1, if n==1
    public static double factorial(double n) {
        if((n == 1) || (n == 0)){
            return 1;
        } else {
            return factorial(n-1)*n;
        }
    }

    public static void main(String[] args) {
        System.out.println(factorial(89));
    }
}
