package Week4.zoo;

public class Main {
    public static void main(String[] args) {
        Cat Alba = new Cat("Alba");
        Alba.meow();


        Cat Jose = new Cat("Jose");
        Jose.meow();

        System.out.println((Cat.count));
        System.out.println(Cat.getCount());
    }
}