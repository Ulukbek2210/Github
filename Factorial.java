package JavaPracticeExapmles;

public class Factorial {

    public static int factorial(int n) {

        if (n == 1) {

            return 1;
        } else {
            return n * factorial(n - 1);
        }


        // public static int factorialWithLoops(int n) {
        //   int result = 1;
        // for (int i = 1; i <= n; i++) {
        //   result *= i;

        //}
        //return result;


    }

    public static void main(String[] args) {
        System.out.println(factorial(11));
    }
}


