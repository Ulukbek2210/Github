package Week4.zoo;

public class Cat extends Animal{
    public String name;
    public Cat(String name){
        this.name = name;
    }
    public void makeSound(){
        System.out.println(this.name + " Meowed");
    }

}