
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;


public class LeapYearCheck {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int x = sc.nextInt();






        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 1);

        long days = ChronoUnit.DAYS.between(start, end); // if (once in 4 years February is full, else it's not ( OR))

        if (days > 365) {
            System.out.println("YES Вискосный год"); // Високосный
        } else {
            System.out.println("NO Обычный год");  // Обычный год
        }


    }
}
