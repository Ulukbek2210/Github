package Week4;

public class Dog {
    public String name;


    public Dog (String name) {
        this.name = name;
    }

    public void bark() {
        System.out.println(this.name + " barked!");
    }
}