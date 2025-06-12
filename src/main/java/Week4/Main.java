package Week4;

import Week4.zoo.Animal;
import Week4.zoo.Cat;
import Week4.zoo.Dog;

import static Week4.zoo.Main.makeItSound;

public class Main {
    public static void main(String[] args){
        Dog bobby = new Dog("Bobby ");
        Cat dong = new Cat("Dong");

        makeItSound(bobby);
        makeItSound(dong);


    }



}

