package Week4;

public class Main {
    public static void main(String[] args){
        Dog Bobby = new Dog();
        Bobby.name = "Bobby ";

        Bobby.bark();


        Dog buddy = new Dog();
        buddy.name = "Buddy ";

        buddy.bark();

        System.out.println(Dog.count);
        System.out.println(Dog.getCount());
    }


}

