package Week4.zoo;

import java.security.PublicKey;

public class Cat {

    static {
        count = 100;
        System.out.println(getCount());

    }
    public String name;

    public static int count;

    public Cat(String name){
        this.name = name;

    }


    public void meow(){

        System.out.println(this.name + " Meowed");
        count++;
    }

    public static int getCount() {
        return count;
    }
}