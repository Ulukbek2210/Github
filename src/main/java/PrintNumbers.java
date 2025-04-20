public class PrintNumbers {

    public static void printTree(int n) {
        for (int i = 1; i <= n; i++) {
            // Print leading spaces
            for (int k = 1; k <= n - i; k++) {
                System.out.print(" ");
            }

            // Print numbers
            for (int j = 1; j <= i; j++) {
                System.out.print(j + " ");
            }

            System.out.println(); // Newline
        }
    }

    public static void main(String[] args) {
        printTree(5);
        printTree(10);
    }
}
