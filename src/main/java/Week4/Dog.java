package Week4;

public class Dog {

    static {
        count = 100;
        System.out.println(getCount());
    }



    public String name;
    public static int count;

    public Dog (String name) {
        this.name = name;
    }

    public void bark() {

        System.out.println(this.name + " barked!");
        count++;
    }

    public static int getCount(){
        return count;

    }
}