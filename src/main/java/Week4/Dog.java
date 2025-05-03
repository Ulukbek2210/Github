package Week4;

public class Dog extends Animal{

    static {
        count = 100;
        System.out.println(getCount());
   }
    public String name;
    public static int count;

    public void bark() {
        System.out.println(this.name + "barked!");
        count++;
    }

    public static int getCount() {
        return count;
    }
}
