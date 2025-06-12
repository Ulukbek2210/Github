package Week4.zoo;

public class Main {
    public static void makeItSound(Animal animal){
        animal.makeSound();

    }
    public static void main(String[] args) {
        Cat Alba = new Cat("Alba");
        Alba.makeSound();


        Cat Jose = new Cat("Jose");
        Jose.makeSound();

    }
}