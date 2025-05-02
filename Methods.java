package JavaPracticeExapmles;

public class Methods {

    public static String fulName(String name, String surname){
        return name + " " + surname;


    }
    public static void greet(String name, String surname) {
        System.out.println("Hello " + fulName(name, surname) + "!");
    }

    public static void main(String[] args) {
        String surname;

        greet("Ulukbek", "Avasbekov");
        greet("Aisultan",  "KALI");



    }
}
